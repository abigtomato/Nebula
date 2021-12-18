package org.abigtomato.nebula.trace.filter;

import lombok.extern.slf4j.Slf4j;
import org.abigtomato.nebula.trace.context.TraceContext;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.springframework.stereotype.Component;

/**
 * 链路跟踪rpc过滤器
 *
 * @author abigtomato
 */
@Slf4j
@Component
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER})
public class DubboTraceFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcServiceContext context = RpcContext.getServiceContext();
        if (context.isConsumerSide()) {
            // 调用方将TRACE信息传递到链路下游
            TraceContext.putRpcContextInfo(context);
            try {
                return invoker.invoke(invocation);
            } finally {
                // 这里清空RpcContext即可，MDC中的信息由Filter清除，否则调用方日志打印不全
                RpcContext.removeContext();
            }
        } else if (context.isProviderSide()) {
            // 提供方将TRACE信息存入自己的MDC中
            TraceContext.putMDCInfo(context);
            try {
                return invoker.invoke(invocation);
            } finally {
                // 提供方要清空MDC中的数据
                TraceContext.removeMDCTraceId();
                TraceContext.removeMDCServiceName();
                TraceContext.removeMDCUri();
            }
        }
        return invoker.invoke(invocation);
    }
}