package com.yunmall.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化Jedis工厂，用于单ip连接
 * @author Harley 2019-01-02
 *
 */
@Configuration
@EnableCaching
@ConfigurationProperties(prefix = "redis.pool")//取下面配的路径中前缀的值
@PropertySource("classpath:/properties/redis.properties")// 路径要写对
public class JedisPoolFactory extends CachingConfigurerSupport {
	protected final Log log = LogFactory.getLog(getClass());
	/**
	 * 初始化Jedis池
	 */
	@Bean(name="jedisPool")
	public JedisPool initJedisClint(){
		String[] hostAndPassWord = hostAndPass.split(":");
		String host = hostAndPassWord[0];
		if (hostAndPassWord.length < 2) {
			jedisPool = new JedisPool(getJedisPoolConfig(), host, port.intValue(), tiemOut.intValue());
		} else {
			String pass = hostAndPassWord[1];
			// timeout，这里既是连接超时又是读写超时，从Jedis 2.8开始有区分connectionTimeout和soTimeout的构造函数
	        jedisPool = new JedisPool(getJedisPoolConfig(), host, port.intValue(), tiemOut.intValue(), pass);
		}
        beforeInstantiationJedis();
        log.info("Created JedisPoolFactory Success.");
        return jedisPool;
	}
//    /**
//     * 自定义生成redis-key策略
//     * 用于springboot 集成
//     */
//    @Override
//    public KeyGenerator keyGenerator() {
//        return new KeyGenerator() {
//            @Override
//            public Object generate(Object o, Method method, Object... objects) {
//                StringBuilder sb = new StringBuilder();
//                sb.append(o.getClass().getName()).append(".");
//                sb.append(method.getName()).append(".");
//                for (Object obj : objects) {
//                    sb.append(obj.toString());
//                }
//                log.info("keyGenerator = " + sb.toString());
//                return sb.toString();
//            }
//        };
//    }
//    /**
//     * 定义springboot集成redis 模板
//     * 用于springboot 集成
//     */
//	@Bean(name="redisTemplate")
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//		// 1.创建连接工厂
//		JedisConnectionFactory jedisConnectionFactory = createJedisConnectionFactory();
//		jedisConnectionFactory.afterPropertiesSet();
//		// 2.定义redis模板和序列化规则
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
//        FastJson2JsonRedisSerializer<Object> serializer = new FastJson2JsonRedisSerializer<Object>(Object.class);
//        //如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to String！  
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        // 3.开启事务
////        redisTemplate.setEnableTransactionSupport(true);
//        redisTemplate.setConnectionFactory(jedisConnectionFactory);
//        log.info("Created JedisPoolFactory Success.");
//        return redisTemplate;
//    }
//	/**
//     * 定义springboot集成redis 模板
//     * 用于springboot 集成
//     */
//	@Bean
//	public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
//		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
//		cacheManager.setDefaultExpiration(60 * 60);//设置缓存保留时间（seconds）
//		log.info("SpringBoot Init RedisTemplate Success.");
//		return cacheManager;
//	}
//	/**
//	 * 单机版配置 @Title: JedisConnectionFactory
//	 */
//	@Bean
//	public JedisConnectionFactory createJedisConnectionFactory() {
//		JedisConnectionFactory JedisConnectionFactory = new JedisConnectionFactory(getJedisPoolConfig());
//		// 配置
//		JedisConnectionFactory.setPoolConfig(getJedisPoolConfig());
//		String[] hostAndPassWord = hostAndPass.split(":");
//		String host = hostAndPassWord[0];
//		// IP地址
//		JedisConnectionFactory.setHostName(host);
//		// 端口号
//		JedisConnectionFactory.setPort(port.intValue());
//		if (hostAndPassWord.length > 1) {
//			String pass = hostAndPassWord[1];
//	        // 如果Redis设置有密码
//	        JedisConnectionFactory.setPassword(pass);
//		}
//		// 客户端超时时间单位是毫秒  timeout，这里既是连接超时又是读写超时，从Jedis 2.8开始有区分connectionTimeout和soTimeout的构造函数
//		JedisConnectionFactory.setTimeout(tiemOut.intValue());
//        beforeInstantiationJedis();
//		return JedisConnectionFactory;
//	}
	/**
	 * 获取配置
	 * @return JedisPoolConfig
	 */
	private JedisPoolConfig getJedisPoolConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		//设置最大实例总数 
		config.setMaxTotal(maxTotal); 
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。 
		config.setMaxIdle(maxIdle);
		//最小空闲连接数, 默认0
		config.setMinIdle(10); 
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException； 
		config.setMaxWaitMillis(maxWairMillis); 
        // 在borrow一个jedis实例时，是否提前进行alidate操作；如果为true，则得到的jedis实例均是可用的；  
		config.setTestOnBorrow(onBorrow); 
        // 在还会给pool时，是否提前进行validate操作  
		config.setTestOnReturn(false);
		//在空闲时检查有效性, 默认false
		config.setTestWhileIdle(true);
		//逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		config.setMinEvictableIdleTimeMillis(1800000);
		//对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
		config.setSoftMinEvictableIdleTimeMillis(1800000);
		//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		config.setTimeBetweenEvictionRunsMillis(-1);
		//每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
		config.setNumTestsPerEvictionRun(3);
		//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
		config.setBlockWhenExhausted(true);
		return config;
	}
	/**
	 * 预防刚初始化完就开始使用报无可用资源，或者并发调用造成异常。预先创建一部分Jedis实例进池
	 */
	private void beforeInstantiationJedis() {
		List<Jedis> minIdleJedisList = new ArrayList<Jedis>(5);
		for (int i = 0; i < 5; i++) {
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				minIdleJedisList.add(jedis);
				jedis.ping();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
			}
		}
		for (int i = 0; i < 5; i++) {
			Jedis jedis = null;
			try {
				jedis = minIdleJedisList.get(i);
				jedis.close();
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
			}
		}
	}
	/**
	 * 获取初始化好的Jedis池
	 * @return JedisPool
	 */
	public JedisPool getJedisPool(){
		return jedisPool;
	}
	
	protected JedisPool jedisPool;
	protected String hostAndPass;
	protected Integer port;
	protected Integer tiemOut;
	protected Integer maxIdle;
	protected Integer maxTotal;
	protected Long maxWairMillis;
	protected boolean onBorrow;

	public String getHostAndPass() {
		return hostAndPass;
	}
	
	public void setHostAndPass(String hostAndPass) {
		this.hostAndPass = hostAndPass;
	}
	
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getTiemOut() {
		return tiemOut;
	}

	public void setTiemOut(Integer tiemOut) {
		this.tiemOut = tiemOut;
	}

	public Integer getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}

	public Integer getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}

	public Long getMaxWairMillis() {
		return maxWairMillis;
	}

	public void setMaxWairMillis(Long maxWairMillis) {
		this.maxWairMillis = maxWairMillis;
	}

	public boolean isOnBorrow() {
		return onBorrow;
	}

	public void setOnBorrow(boolean onBorrow) {
		this.onBorrow = onBorrow;
	}
}
