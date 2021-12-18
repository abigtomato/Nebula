package org.abigtomato.nebula.http.annotaion;

import com.github.lianjiatech.retrofit.spring.boot.annotation.InterceptMark;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.BasePathMatchInterceptor;
import org.abigtomato.nebula.http.interceptor.DouYinSignInterceptor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@InterceptMark
public @interface DouYinSign {

    /**
     * 密钥key
     * 支持占位符形式配置。
     */
    String method();

    /**
     * 密钥
     * 支持占位符形式配置。
     */
    String v() default "2";

    /**
     * 密钥
     * 支持占位符形式配置。
     */
    String signMethod() default "md5";

    /**
     * 拦截器匹配路径
     */
    String[] include() default {"/**"};

    /**
     * 拦截器排除匹配，排除指定路径拦截
     */
    String[] exclude() default {};

    /**
     * 处理该注解的拦截器类
     * 优先从spring容器获取对应的Bean，如果获取不到，则使用反射创建一个！
     */
    Class<? extends BasePathMatchInterceptor> handler() default DouYinSignInterceptor.class;
}
