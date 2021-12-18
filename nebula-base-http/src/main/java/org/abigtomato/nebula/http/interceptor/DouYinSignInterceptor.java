package org.abigtomato.nebula.http.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.BasePathMatchInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.abigtomato.nebula.http.retrofit.DouYinOAuth2Retrofit;
import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

@Slf4j
//@Component
@RequiredArgsConstructor
public class DouYinSignInterceptor extends BasePathMatchInterceptor {

//    @Value("${douyin.access-keyId}")
    private String accessKeyId;
//    @Value("${douyin.access-keySecret}")
    private String accessKeySecret;

    private final DouYinOAuth2Retrofit oAuth2Retrofit;

    private String v;

    private String method;

    private String signMethod;

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    @SneakyThrows
    @Override
    public Response doIntercept(Chain chain) throws IOException {
        Request request = chain.request();
        String shopId = request.header("douyin-shop-id");
        HttpUrl requestHttpUrl = request.url();
//        final String timestamp = DateUtils.formatFullTime(LocalDateTime.now(), TimeFormat.FULL_TIME_SPLIT_PATTERN);
        //构建签名字段的值
        TreeMap<String, Object> signParams = new TreeMap<>();
        signParams.put("method", method);
//        signParams.put("timestamp", timestamp);
        signParams.put("app_key", accessKeyId);
        signParams.put("v", v);
        Set<String> nameList = requestHttpUrl.queryParameterNames();
        nameList.forEach(name -> {
            List<String> valueList = requestHttpUrl.queryParameterValues(name);
            if (CollectionUtils.isNotEmpty(valueList)) {
                signParams.put(name, valueList.get(0));
            } else {
                signParams.put(name, null);
            }
        });
//        final String signature = SignUtils.sign(signParams, accessKeySecret);
        // 获取访问令牌
        String accessToken = this.catchNextGenerationToken(shopId);
        //新增签名字段
        HttpUrl url = requestHttpUrl.newBuilder()
                .addQueryParameter("method", method)
//                .addQueryParameter("timestamp", timestamp)
                .addQueryParameter("access_token", accessToken)
                .addQueryParameter("app_key", accessKeyId)
                .addQueryParameter("v", v)
//                .addQueryParameter("sign", signature)
                .addQueryParameter("sign_method", signMethod)
                .build();
        Request.Builder requestBuilder = request.newBuilder().header("Connection", "close")
                .url(url).cacheControl(CacheControl.FORCE_NETWORK);
        return chain.proceed(requestBuilder.build());
    }

    /**
     * 同步请求方式，获取最新的Token
     *
     * @return Token
     */
    private String catchNextGenerationToken(String shopId) throws Exception {
        final String encodedPassKey = this.getPassWord();
        String cacheKey = this.getAuthCacheKey(shopId, encodedPassKey);
//        final String cacheToken = this.redisService.get(cacheKey) + EMPTY;
//        if (StringUtils.isBlank(cacheToken)||"null".equals(cacheToken)) {
//            // 如果不为空则刷新认证，如果为空则直接请求登录
//            return this.loginAuth(encodedPassKey, shopId);
//        } else {
//            JSONObject jsonObject = JSON.parseObject(cacheToken);
//            return jsonObject.getString("access_token");
//        }
        return cacheKey;
    }

    /**
     * 刷新令牌
     *
     * @param cacheToken     缓存中的TOKEN
     * @param shopId         店鋪ID
     * @param encodedPassKey 加密后密码
     * @return {
     * "code": "0",
     * "msg": "Success",
     * "data": {
     * "access_token": "a129e054-b4c3-4e31-83f7-814c415cf8e0",
     * "refresh_token": "f6a92cb5-d7a5-4bf8-b760-c42311ebeaa6",
     * "expires": 700
     * }
     * }
     */
    private String refreshAuth(String cacheToken, String shopId, String encodedPassKey) throws Exception {
        JSONObject data = JSONObject.parseObject(cacheToken);
        retrofit2.Response<ResponseBody> response = this.oAuth2Retrofit.refreshToken(accessKeyId, accessKeySecret, data.getString("refresh_token"));
        assert response != null;
        return this.getAccessToken(response, shopId, encodedPassKey);
    }

    /**
     * 获取access_token
     *
     * @param encodedPassKey 加密后密码
     * @param shopId         店鋪ID
     * @return TOKEN
     */
    private String loginAuth(String encodedPassKey, String shopId) throws IOException {
        retrofit2.Response<ResponseBody> response = this.oAuth2Retrofit.login(accessKeyId, accessKeySecret, shopId, "authorization_self");
        assert response != null;
        return this.getAccessToken(response, shopId, encodedPassKey);
    }

    /**
     * 设置获取访问令牌
     *
     * @param response       {@link retrofit2.Response}
     * @param shopId         店鋪ID
     * @param encodedPassKey 令牌缓存Key
     * @return 令牌
     */
    private String getAccessToken(retrofit2.Response<ResponseBody> response, String shopId, String encodedPassKey) throws IOException {
        if (response != null && response.isSuccessful()) {
            ResponseBody body = response.body();
            assert body != null;
            JSONObject headerToken = JSONObject.parseObject(body.string()).getJSONObject("data");
            final String encodedAuthKey = this.getAuthCacheKey(shopId, encodedPassKey);
//            redisService.set(encodedAuthKey, headerToken.toString(), headerToken.getLongValue("expires_in"));
            return headerToken.getString("access_token");
        }
        return "";
    }

    /**
     * 获取请求token key
     *
     * @return password
     */
    private String getPassWord() {
        final Base64.Encoder encoder = Base64.getEncoder();
//        final String passKey = accessKeyId + StringPool.COLON + accessKeySecret;
//        return encoder.encodeToString(passKey.getBytes(StandardCharsets.UTF_8));
        return encoder.toString();
    }

    /**
     * 获取认证Key
     */
    private String getAuthCacheKey(String shopId, String password) {
        return "auth_to_access:" + shopId + ":douyin:" + password;
    }
}

