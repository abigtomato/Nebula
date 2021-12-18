package org.abigtomato.nebula.trace.context;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.MDC;

/**
 * 链路跟踪上下文
 *
 * @author yagushou
 */
public class TraceContext {

    public final static String TRACE_ID = "TRACE_ID";
    public final static String TRACE_ID_HEADER = "Trace-Id";

    public final static String SERVICE_NAME = "SERVICE_NAME";
    public final static String URI = "URI";

    /**
     * RpcContext设置TRADE_ID
     * 服务调用方使用，将TRACE_ID传递到下游
     */
    public static void putRpcContextTraceId(RpcContext context) {
        String traceId = getTraceId();
        traceId = StringUtils.isNotBlank(traceId) ? traceId : generateTraceId();
        context.setAttachment(TRACE_ID, traceId);
    }

    public static void putRpcContextInfo(RpcContext context) {
        putRpcContextTraceId(context);
        context.setAttachment(SERVICE_NAME, getMDCServiceName());
        context.setAttachment(URI, getMDCUri());
    }

    /**
     * MDC设置TRACE_ID
     * 服务提供方调用，将从调用方远程获取的TRACE_ID存入本地MDC中
     */
    public static void putMDCTraceId(RpcContext context) {
        String traceId = context.getAttachment(TRACE_ID);
        traceId = StringUtils.isNotBlank(traceId) ? traceId : generateTraceId();
        setTraceId(traceId);
    }

    public static void putMDCInfo(RpcContext context) {
        putMDCTraceId(context);
        setMDCServiceName(context.getUrl().getParameter(CommonConstants.APPLICATION_KEY));
        setMDCUri(context.getAttachment(URI));
    }

    public static void removeMDCTraceId() {
        MDC.remove(TRACE_ID);
    }

    public static String setMDCTraceId() {
        String traceId = generateTraceId();
        MDC.put(TRACE_ID, traceId);
        return traceId;
    }

    public static String setMDCTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
        return traceId;
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    public static void setMDCServiceName(String serviceName) {
        MDC.put(SERVICE_NAME, serviceName);
    }

    public static void removeMDCServiceName() {
        MDC.remove(SERVICE_NAME);
    }

    public static String getMDCServiceName() {
        return MDC.get(SERVICE_NAME);
    }

    public static void setMDCUri(String uri) {
        MDC.put(URI, uri);
    }

    public static String getMDCUri() {
        return MDC.get(URI);
    }

    public static void removeMDCUri() {
        MDC.remove(URI);
    }

    public static String generateTraceId() {
        // traceId生成规则: 业务 + 服务器IP + ID产生的时间（纳秒级）
        String address = NetUtils.getLocalAddress().getHostAddress();
        address = address.replaceAll("\\.", "");
        long nanoTime = System.nanoTime();
        // TODO 业务类型
        return address + nanoTime;
    }
}