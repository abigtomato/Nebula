package org.abigtomato.nebula.redis.utils;

import org.apache.commons.lang3.RandomUtils;

/**
 * Redis过期时间工具类
 *
 * @author abigtomato
 */
public class ExpireRandomUtil {

    /**
     * 一小时 3600s
     */
    private static final Long HOUR_SECOND = 3600L;

    /**
     * 一天
     */
    private static final Long DAY_SECOND = 86400L;

    /**
     * 一周
     */
    private static final Long WEEK_SECOND = 604800L;

    /**
     * 一月
     */
    private static final Long MONTH_SECOND = 2592000L;

    /**
     * 一周~一个月之间的随机秒数
     *
     * @return
     */
    public static Long randomWM() {
        return RandomUtils.nextLong(WEEK_SECOND, MONTH_SECOND);
    }

    /**
     * 获取随机数，单位秒
     *
     * @param min
     * @param max
     * @return
     */
    public static Long random(long min, long max) {
        return RandomUtils.nextLong(min, max);
    }
}
