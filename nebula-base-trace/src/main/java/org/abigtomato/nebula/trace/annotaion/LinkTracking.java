package org.abigtomato.nebula.trace.annotaion;

import java.lang.annotation.*;

/**
 * 链路跟踪注解
 *
 * @author abigtomato
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LinkTracking {
}