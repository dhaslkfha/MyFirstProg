package com.zhouning.myfirstprog.tool;

/**
 * @ Logout.java
 * 作用 日志输出接口
 * 注意事项
 *
 * 注意： 本内容仅限于国家电网内部使用，禁止转发
 * VERSION        DATE              BY       CHANGE/COMMENT
 * 1.0          2013-8-13          zoxue         create
 */

import android.util.Log;

import com.zhouning.myfirstprog.Config;

/**
 * 
 * @Description:打印工具类
 *
 * 
 * @author <a href="mailto:snowpenglei@gmail.com">@author LP</a>
 * 
 * @data 2013-9-21
 * 
 * version 1.0
 * 
 * Copyright sgcc
 */
public class Logout {

	/**
	 * log  info
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, Object msg) {
		if (Config.isDebug)
			Log.i(tag, "msg: " + msg);
	}
	public static void i(Object msg) {
		if (Config.isDebug)
			Log.i(Config.LOGMSG, "msg: " + msg);
	}

	/**
	 * log  debug
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, Object msg) {
		if (Config.isDebug)
			// Log.d(tag, msg);
			Log.e(tag, "msg: " + msg);
	}
	public static void d(Object msg) {
		if (Config.isDebug)
			Log.d(Config.LOGMSG, "msg: " + msg);
	}

	/**
	 * log  error
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, Object msg) {
		if (Config.isDebug)
			Log.e(tag, "msg: " + msg);
	}
	public static void e(Object msg) {
		if (Config.isDebug)
			Log.e(Config.LOGMSG, "msg: " + msg);
	}

	/**
	 * log  v
	 * @param tag
	 * @param msg
	 */
	public static void v(String tag, Object msg) {
		if (Config.isDebug)
			Log.v(tag, "msg: " + msg);
	}
	public static void v(Object msg) {
		if (Config.isDebug)
			Log.v(Config.LOGMSG, "msg: " + msg);
	}
	/**
	 * print msg
	 * @param msg
	 */
	public static void printf(Object msg) {
		if (Config.isDebug)
			System.out.println("msg: " + msg);
	}

}
