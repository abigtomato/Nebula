package org.abigtomato.nebula.redis.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * RedisConfig
 *
 * @author abigtomato
 */
@Configuration
public class RedisConfig {
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
        SerializerFeature[] features = {
                // 打开循环引用检测，JSONField(serialize = false)不循环
                SerializerFeature.DisableCircularReferenceDetect,
                // class全限定名
                SerializerFeature.WriteClassName,
                // 输出空置字段
                SerializerFeature.WriteMapNullValue,
                // list字段如果为null，输出为[]，而不是null
                SerializerFeature.WriteNullListAsEmpty,
                // 数值字段如果为null，输出为0，而不是null
                SerializerFeature.WriteNullNumberAsZero,
                // Boolean字段如果为null，输出为false，而不是null
                SerializerFeature.WriteNullBooleanAsFalse,
                // 字符类型字段如果为null，输出为""，而不是null
                SerializerFeature.WriteNullStringAsEmpty
        };
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(features);
        // 打开AutoTypeSupport
        ParserConfig parserConfig = new ParserConfig();
        parserConfig.setAutoTypeSupport(true);
        fastJsonConfig.setParserConfig(parserConfig);
        fastJsonRedisSerializer.setFastJsonConfig(fastJsonConfig);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        template.setValueSerializer(fastJsonRedisSerializer);
        template.setHashValueSerializer(fastJsonRedisSerializer);
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
