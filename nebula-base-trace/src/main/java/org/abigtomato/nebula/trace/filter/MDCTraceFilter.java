package org.abigtomato.nebula.trace.filter;

import lombok.extern.slf4j.Slf4j;
import org.abigtomato.nebula.trace.context.TraceContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 链路跟踪web过滤器
 *
 * @author yagushou
 */
@Slf4j
@Component
public class MDCTraceFilter implements Filter {

    @Value("${spring.application.name}")
    private String serviceName;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            // 设置uri和服务名到MDC
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            TraceContext.setMDCUri(httpServletRequest.getRequestURI());
            TraceContext.setMDCServiceName(serviceName);

            // 设置traceId到响应头
            // 这里表示从网关拿到了 traceId
            String traceId = ((HttpServletRequest) request).getHeader(TraceContext.TRACE_ID_HEADER);
            if (StringUtils.isBlank(traceId)) {
                traceId = TraceContext.setMDCTraceId();
            } else {
                TraceContext.setMDCTraceId(traceId);
            }
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader(TraceContext.TRACE_ID_HEADER, traceId);

            chain.doFilter(request, response);
        } finally {
            // 清除MDC中的数据
            TraceContext.removeMDCTraceId();
            TraceContext.removeMDCServiceName();
            TraceContext.removeMDCUri();
        }
    }

    @Override
    public void destroy() {
    }
}
