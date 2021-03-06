package com.capgemini.omnichannel.omnisession.model.service.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.capgemini.omnichannel.omnisession.model.service.CacheService;

@Service
public class InfinispanCacheService implements CacheService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private DefaultCacheManager cacheManager;

	@Value("${omnisession.clusterName}")
	private String clusterName;

	@Value("${omnisession.maxIdle}")
	private long maxIdle;

	@Value("${omnisession.expirationTimeInSeconds}")
	private long expirationTimeInSeconds;

	public InfinispanCacheService() {
		super();

	}

	@PostConstruct
	public void init() {
		logger.info("Initiating Infinispan");

		GlobalConfigurationBuilder global = GlobalConfigurationBuilder.defaultClusteredBuilder();
		global.transport().clusterName(clusterName);
		ConfigurationBuilder config = new ConfigurationBuilder();
		config.expiration().lifespan(expirationTimeInSeconds, TimeUnit.SECONDS).maxIdle(maxIdle, TimeUnit.SECONDS)
				.clustering().cacheMode(CacheMode.DIST_SYNC);
		cacheManager = new DefaultCacheManager(global.build(), config.build());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.capgemini.omnichannel.omnisession.model.service.CacheService#get(java.lang.String)
	 */
	@Override
	public <T> T get(String key) {
		T result = null;

		result = get(key, null);

		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.capgemini.omnichannel.omnisession.model.service.CacheService#get(java.lang.String, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(String key, String cacheName) {

		T result = null;

		Cache<Object, Object> cache;
		cache = getCache(cacheName);

		if (cache != null) {
			result = (T) cache.get(key);
		}

		return result;

	}

	@Override
	public <T> T getOrDefault(String key, Object defaultValue) {
		T result = null;

		result = getOrDefault(key, null);

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getOrDefault(String key, String cacheName, Object defaultValue) {
		T result = null;

		Cache<Object, Object> cache;
		cache = getCache(cacheName);

		if (cache != null) {
			result = (T) cache.getOrDefault(key, defaultValue);
		}

		return result;
	}

	private Cache<Object, Object> getCache(String cacheName) {
		Cache<Object, Object> cache;
		if (cacheName != null) {
			cache = cacheManager.getCache(cacheName);
		} else {
			cache = cacheManager.getCache();
		}
		return cache;
	}

	@Override
	public boolean containsKey(String key, String cacheName) {

		Cache<Object, Object> cache = getCache(cacheName);

		boolean result = cache.containsKey(key);
		return result;
	}

	@Override
	public boolean containsKey(String key) {
		return containsKey(key, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.capgemini.omnichannel.omnisession.model.service.CacheService#put(java.lang.String, java.lang.Object)
	 */
	@Override
	public <T> T put(String key, T value) {
		return put(key, null, value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.capgemini.omnichannel.omnisession.model.service.CacheService#put(java.lang.String, java.lang.String,
	 * java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T put(String key, String cacheName, T value) {

		Cache<Object, Object> cache = getCache(cacheName);
		T result = (T) cache.put(key, value);

		logger.debug(String.format("Cache updated, entrySet: %s", cache.values()));
		return result;

	}
	
	

	@Override
	public <T> T putIfAbsent(String key, T value) {
		return putIfAbsent(key, null, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T  putIfAbsent(String key, String cacheName, T value) {
		Cache<Object, Object> cache = getCache(cacheName);
		T  result = (T) cache.putIfAbsent(key, value);

		logger.debug(String.format("Cache updated, entrySet: %s", cache.values()));
		
		return result;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public long getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(long maxIdle) {
		this.maxIdle = maxIdle;
	}

	public long getExpirationTimeInSeconds() {
		return expirationTimeInSeconds;
	}

	public void setExpirationTimeInSeconds(long expirationTimeInSeconds) {
		this.expirationTimeInSeconds = expirationTimeInSeconds;
	}

}
