package org.abigtomato.nebula.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * Twitter snowflake 算法JAVA版本，唯一序列号，理论上来说一秒钟可以产生4095000个序列号
 *
 * @author abigtomato
 */
public class UniqueIdGen {

    // 唯一ID= 时间戳+机房ID+机器ID+自增

    // 开始使用该算法的时间为: 2017-01-01 00:00:00，这个时间一旦固定不可随意改动
    private static final long START_TIME = 1483200000000L;

    // 5位数据中心码(可以理解为机房ID)
    private final long datacenterIdBits = 5L;

    // 最多32个机房
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

    // 5位机器码（可以理解为服务器ID）
    private final long workerIdBits = 5L;
    // 一个机房最多32台机器
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    // 毫秒自增位,
    private final long sequenceBits = 12L;

    // 机器码左移位
    private final long workerIdShift = sequenceBits;

    // 数据中心左移位
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    // 时间戳左移动位
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    // 自增数最大值
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    // 当前毫秒生成的序列
    private long sequence = 0L;

    // 上次生产id时间戳
    private long lastTimestamp = -1L;

    // 工作机器ID
    private long workerId;

    // 数据中心ID
    private long datacenterId;

    // 单例
    private static volatile UniqueIdGen idGen = null;

    /**
     * 实例化
     *
     * @param workerId     机器ID
     * @param datacenterId 数据中心ID
     * @return
     */
    public static UniqueIdGen getInstance(long workerId, long datacenterId) {
        if (idGen == null) {
            synchronized (UniqueIdGen.class) {
                if (idGen == null) {
                    idGen = new UniqueIdGen(workerId, datacenterId);
                }
            }
        }
        return idGen;
    }

    /**
     * 实例化
     *
     * @return
     */
    public static UniqueIdGen getInstance() {
        if (idGen == null) {
            synchronized (UniqueIdGen.class) {
                if (idGen == null) {
                    idGen = new UniqueIdGen();
                }
            }
        }
        return idGen;
    }

    /**
     * 自动获取数据中心ID和机器ID
     */
    private UniqueIdGen() {
        this.datacenterId = getDatacenterId(maxDatacenterId);
        this.workerId = getMaxWorkerId(datacenterId, maxWorkerId);
    }

    /**
     * 手动指定机房ID和机器ID
     *
     * @param workerId     机器ID
     * @param datacenterId 数据中心ID
     */
    private UniqueIdGen(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 获取数据中心ID，network mac地址
     */
    private static long getDatacenterId(long maxDatacenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                    id = id % (maxDatacenterId + 1);
                }
            }
        } catch (Exception e) {
            System.out.println("getDatacenterId: = " + e.getMessage());
        }
        return id;
    }

    /**
     * 获取机器ID，进程ID
     *
     * @param datacenterId 数据中心
     * @param maxWorkerId  最大机器ID
     * @return
     */
    protected static long getMaxWorkerId(long datacenterId, long maxWorkerId) {
        StringBuilder mpid = new StringBuilder();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!StringUtils.isEmpty(name)) {
            // GET jvmPid
            mpid.append(name.split("@")[0]);
        }
        // MAC + PID 的 hashcode 获取16个低位
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    /**
     * 利用twitter的snowflake（做了些微修改）算法来实现
     *
     * @return
     */
    public String genUniqueId() {
        return Long.toHexString(this.nextId());
    }

    /**
     * 获取下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        if (timestamp < lastTimestamp) {
            //如果当前时间小于上次时间5毫秒之内，阻塞等待 offset*2 毫秒，还是小于上次时间，则抛出异常
            long offset = lastTimestamp - timestamp;
            if (offset <= 5) {
                try {
                    wait(offset << 1);
                    timestamp = timeGen();
                    if (timestamp < lastTimestamp) {
                        throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", offset));
            }
        }

        if (lastTimestamp == timestamp) {
            // 相同毫秒内，序列号自增
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                // 同一毫秒的序列数已经达到最大，阻塞到新的毫秒产生
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 不同毫秒内,直接从0开始
            sequence = 0;
            // 不同毫秒内，序列号置为 1 - 3 随机数 第二种方案
//            sequence = ThreadLocalRandom.current().nextLong(1, 3);
        }

        lastTimestamp = timestamp;

        // 时间戳部分
        return ((timestamp - START_TIME) << timestampLeftShift)
                // 数据中心部分
                | (datacenterId << datacenterIdShift)
                // 机器标识部分
                | (workerId << workerIdShift)
                // 序列号部分
                | sequence;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
