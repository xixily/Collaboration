package com.lianlian.utils.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

import com.lianlian.utils.system.ResourcesUtil;


//import net.sf.ehcache.Cache;
//import net.sf.ehcache.Element;

public class CasSessionManager<K, V> {
	private CacheManager manager ;
	private Cache store;
	
	public CasSessionManager(CacheManager manager) {
		super();
		this.manager = manager;
		this.store = manager.getCache(ResourcesUtil.getSessionCacheName());
	}
	
	public CasSessionManager() {
		super();
	}

	public void init() {
		store = manager.getCache(ResourcesUtil.getSessionCacheName());
		System.out.println("初始化casSessionManager ：" +  store);
	}

	/**
	 * 插入缓存
	 * @param key
	 * @param value
	 */
	public void putCache(K key, V value) {
		store.put(key, value);
	};
	
	/**
	 * 移除缓存
	 * @param key
	 * @return
	 */
	public boolean removeCache(K key) {
		try {
			store.evict(key);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 获取缓存对象
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public V getValue(K key) {
		ValueWrapper valueW = store.get(key);
		if(null != valueW) {
			return (V) valueW.get();
		}
		return null;
	};
}
