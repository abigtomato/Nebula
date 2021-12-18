package org.abigtomato.nebula.http.manager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitServiceManager {

    private static final int DEFAULT_CONNECT_TIME = 10;
    private static final int DEFAULT_WRITE_TIME = 30;
    private static final int DEFAULT_READ_TIME = 30;
    private static final String REQUEST_PATH = "https://www.baidu.com/";
    private final Retrofit retrofit;

    private RetrofitServiceManager() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                // 连接超时时间
                .connectTimeout(DEFAULT_CONNECT_TIME, TimeUnit.SECONDS)
                // 设置写操作超时时间
                .writeTimeout(DEFAULT_WRITE_TIME, TimeUnit.SECONDS)
                // 设置读操作超时时间
                .readTimeout(DEFAULT_READ_TIME, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                // 设置使用okhttp网络请求
                .client(okHttpClient)
                // 设置服务器路径
                .baseUrl(REQUEST_PATH)
                // 添加转化库
                .addConverterFactory(GsonConverterFactory.create())
                // 添加回调库
                .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                .build();
    }

    private static class SingletonHolder {

        private static final RetrofitServiceManager INSTANCE = new RetrofitServiceManager();
    }

    public static RetrofitServiceManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }
}
