package com.and.yzy.frame.config;


import com.and.yzy.frame.util.SPUtils;


public abstract class UserInfoManger {

	public static final String FILE = "userConfig";
	public static final String TOKEN = "token";
	public static final String UUID = "uuid";
	/**
	 * 获得登陆状态
	 */
	public static boolean isLogin() {
		SPUtils spUtils = new SPUtils("userConfig");
		return (Boolean) spUtils.get("isLogin", false);
	}

	/**
	 * 设置登陆状态
	 */
	public static void setIsLogin( boolean b) {
		SPUtils spUtils = new SPUtils("userConfig");
		spUtils.put("isLogin", b);
	}


	public static void setToken(String token) {
		SPUtils spUtils = new SPUtils(FILE);
		spUtils.put(TOKEN, token);
	}

	public static String getToken() {
		SPUtils spUtils = new SPUtils(FILE);
		return (String) spUtils.get(TOKEN, "");
	}


	public static void setUUid(String uuid) {
		SPUtils spUtils = new SPUtils(FILE);
		spUtils.put(UUID, uuid);
	}

	public static String getUUid() {
		SPUtils spUtils = new SPUtils(FILE);
		return (String) spUtils.get(UUID, "");
	}
	

}
