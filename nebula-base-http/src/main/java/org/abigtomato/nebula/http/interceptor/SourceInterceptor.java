package org.abigtomato.nebula.http.interceptor;

import com.github.lianjiatech.retrofit.spring.boot.interceptor.BaseGlobalInterceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * 全局拦截器: 对整个系统的的http请求执行统一的拦截处理
 */
//@Component
public class SourceInterceptor extends BaseGlobalInterceptor {

    @Override
    public Response doIntercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newReq = request.newBuilder()
                .addHeader("source", "test")
                .build();
        return chain.proceed(newReq);
    }
}
