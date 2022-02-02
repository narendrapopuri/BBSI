package com.bbsi.platform.user.config;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.google.common.collect.Lists;

@Configuration
public class RedisConfiguration {

	@Value("${spring.redis.host}")
	private String redisHostname;
	@Value("${spring.redis.port}")
	private int redisPort;
	@Value("${spring.redis.password}")
	private String redisPassword;

	@Value("${spring.redis.use-cluster}")
	private boolean useCluster;

	@Value("${spring.redis.pool.max-idle}")
	private int maxIdle;

	@Value("${spring.redis.pool.min-idle}")
	private int minIdle;

	@Value("${spring.redis.pool.max-total}")
	private int maxTotal;

	@Value("${spring.redis.pool.max-wait}")
	private long maxWait;

	@Value("${spring.redis.pool.block-when-exhausted}")
	private boolean blockWhenExhausted;

	@Value("${spring.redis.connection-timeout}")
	private long connectionTimeout;

	@Value("${spring.redis.read-timeout}")
	private long readTimeout;

	@Value("${spring.redis.ssl}")
	private boolean useSSL;

	@Bean
	protected JedisConnectionFactory jedisConnectionFactory() {
		org.springframework.data.redis.connection.RedisConfiguration configuration = null;
		if (useCluster) {
			String clusterNode = redisHostname + ":" + redisPort;
			List<String> clusterNodes = Lists.newArrayList(clusterNode);
			configuration = new RedisClusterConfiguration(clusterNodes);
			((RedisClusterConfiguration) configuration).setPassword(redisPassword);
		} else {
			configuration = new RedisStandaloneConfiguration(redisHostname, redisPort);
			((RedisStandaloneConfiguration) configuration).setPassword(redisPassword);
		}
		JedisClientConfiguration jedisClientConfiguration = null;
		if (useSSL) {
			jedisClientConfiguration = JedisClientConfiguration.builder().useSsl().and()
					.connectTimeout(Duration.ofSeconds(connectionTimeout)).readTimeout(Duration.ofSeconds(readTimeout))
					.usePooling().build();
		} else {
			jedisClientConfiguration = JedisClientConfiguration.builder()
					.connectTimeout(Duration.ofSeconds(connectionTimeout)).readTimeout(Duration.ofSeconds(readTimeout))
					.usePooling().build();
		}

		Optional<? extends GenericObjectPoolConfig> poolConfig = jedisClientConfiguration.getPoolConfig();
		if (poolConfig.isPresent()) {
			poolConfig.get().setMinIdle(minIdle);
			poolConfig.get().setMaxIdle(maxIdle);
			poolConfig.get().setMaxTotal(maxTotal);
			poolConfig.get().setMaxWaitMillis(maxWait);
			poolConfig.get().setBlockWhenExhausted(blockWhenExhausted);
		}
		JedisConnectionFactory factory = null;
		if (useCluster) {
			factory = new JedisConnectionFactory((RedisClusterConfiguration) configuration, jedisClientConfiguration);
		} else {
			factory = new JedisConnectionFactory((RedisStandaloneConfiguration) configuration,
					jedisClientConfiguration);
		}
		factory.afterPropertiesSet();
		return factory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new GenericToStringSerializer<Object>(Object.class));
		redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}
}