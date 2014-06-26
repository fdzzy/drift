package com.baidu.bae.api.factory;

import com.baidu.bae.api.exception.BaeException;
import com.baidu.bae.api.image.BaeImageService;
import com.baidu.bae.api.image.internal.BaeImageServiceImpl;
import com.baidu.bae.api.image.local.BaeImageServiceLocalImpl;
//import com.baidu.bae.api.memcache.BaeCache;
//import com.baidu.bae.api.memcache.BaeMemcachedClient;
//import com.baidu.bae.api.memcache.local.BaeMemcachedClientLocalImpl;

/**
 * 宸ュ巶绫伙紝鑾峰彇bae鍚勭被鏈嶅姟鐨勫疄渚�
 * 
 * @author mengxiansen
 * 
 */
public class BaeFactory {	
	/**
	 *  鑾峰彇BaeImageService瀹炰緥
	 * @param ak  绠＄悊骞冲彴涓婅幏鍙栫殑ak
	 * @param sk  绠＄悊骞冲彴涓婅幏鍙栫殑sk
	 * @param host  鐧惧害image鏈嶅姟API鐨勫煙鍚嶏紝涓嶅寘鎷琱ttp://
	 * @return  BaeImageService瀹炰緥, 澶辫触鎶涘嚭BaeException寮傚父
	 * @throws BaeException
	 */
	public static BaeImageService getBaeImageService(String ak,String sk,String host) throws BaeException {
		if (System.getProperty("baejavasdk.local") != null && System.getProperty("baejavasdk.local").equals("true") ) {
			return new BaeImageServiceLocalImpl(ak,sk,host);
		} else {
			return new BaeImageServiceImpl(ak,sk,host);
		}
	}
	
/*	public static BaeCache getBaeCache(String cacheId, String memcacheAddr, String user, String password) throws BaeException {		
		if (System.getProperty("baejavasdk.local") != null && System.getProperty("baejavasdk.local").equals("true") ) {
			return new BaeMemcachedClientLocalImpl(cacheId, memcacheAddr, user, password);
		} else {
			return new BaeMemcachedClient(cacheId, memcacheAddr, user, password);
		}
		
		
	}*/
}
