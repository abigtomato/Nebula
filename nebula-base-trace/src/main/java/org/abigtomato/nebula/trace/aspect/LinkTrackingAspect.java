package org.abigtomato.nebula.trace.aspect;

import org.abigtomato.nebula.trace.context.TraceContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 链路跟踪切面
 *
 * @author yagushou
 */
@Aspect
@Component
public class LinkTrackingAspect {

    @Value("${spring.application.name}")
    private String serviceName;

    @Around("@annotation(org.abigtomato.nebula.trace.annotaion.LinkTracking)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        try {
            TraceContext.setMDCTraceId();
            TraceContext.setMDCServiceName(serviceName);
            result = pjp.proceed(pjp.getArgs());
        } finally {
            TraceContext.removeMDCTraceId();
            TraceContext.removeMDCServiceName();
            TraceContext.removeMDCUri();
        }
        return result;
    }
}
