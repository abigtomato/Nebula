package org.abigtomato.nebula.http.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

//@VenusRetrofitClient(baseUrl = "https://openapi-fxg.jinritemai.com/", poolName = "loreal", logStrategy = LogStrategy.BASIC)
public interface DouYinOAuth2Retrofit {

    /**
     * 抖音OAuth2
     *
     * @param appId     应用key ，长度19位数字字符串
     * @param secret    应用密钥字符串
     * @param grantType 授权类型，默认为authorization_code（例：authorization_self）
     */
    @GET("/oauth2/access_token")
    Response<ResponseBody> login(@Query("app_id") String appId, @Query("app_secret") String secret, @Query("shop_id") String shopId, @Query("grant_type") String grantType);

    /**
     * 刷新令牌抖音OAuth2
     *
     * @param appId  应用key ，长度19位数字字符串
     * @param secret 应用密钥字符串
     * @param token  用于刷新access_token的刷新令牌（有效期：14 天）
     *               Tips：
     *               1. 在 access_token 过期前1h之前，ISV使用 refresh_token 刷新时，会返回原来的 access_token 和 refresh_token，但是二者有效期不变；
     *               2. 在 access_token 过期前1h之内，ISV使用 refresh_token 刷新时，会返回新的 access_token 和 refresh_token，但是原来的 access_token 和 refresh_token 继续有效一个小时；
     *               3. 在 access_token 过期后，ISV使用 refresh_token 刷新时，将获得新的 acces_token 和 refresh_token，同时原来的 acces_token 和 refresh_token 失效
     */
    @GET("/oauth2/refresh_token?grant_type=refresh_token")
    Response<ResponseBody> refreshToken(@Query("app_id") String appId, @Query("app_secret") String secret, @Query("refresh_token") String token);
}

