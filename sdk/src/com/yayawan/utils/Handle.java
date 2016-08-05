package com.yayawan.utils;

import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONObject;

import com.yayawan.sdk.jfutils.Sputils;
import com.yayawan.sdk.jfutils.Yayalog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Handle {

	/**
	 * 注册回调
	 */
	public static void register_handler(final Context context,
			final String uid, final String userName) {
		// 访问网络,在线程中执行
		new Thread() {

			public void run() {
				HashMap<String, String> params = new HashMap<String, String>();

				try {
					params.put("data",
							encryptRegisterData(context, uid, userName));
					HttpUtil.post(URL.REGISTER_HANDLER, params, "UTF-8");
				} catch (Exception e) {
					e.printStackTrace();
				}
			};

		}.start();
	}

	/**
	 * 登录回调
	 */
	public static void login_handler(final Context context, final String uid,
			final String userName) {
		// 访问网络,在线程中执行
		new Thread() {

			public void run() {
				HashMap<String, String> params = new HashMap<String, String>();

				try {
					params.put("data",
							encryptRegisterData(context, uid, userName));
					HttpUtil.post(URL.LOGIN_HANDLER, params, "UTF-8");
				} catch (Exception e) {
					e.printStackTrace();
				}
			};

		}.start();
	}

	/**
	 * 激活回调
	 */
	public static void active_handler(final Context context) {
		// 访问网络,在线程中执行
		new Thread() {

			public void run() {
				HashMap<String, String> params = new HashMap<String, String>();

				params.put("game_id", DeviceUtil.getGameId(context));
				params.put("union_id", DeviceUtil.getUnionid(context));
				params.put("device", DeviceUtil.getIMEI(context));
				try {
					Yayalog.loger("我在激活");
					String post = HttpUtil.post(URL.ACTIVE_HANDLER, params,
							"UTF-8");

					JSONObject object = new JSONObject(post);
					Yayalog.loger("激活信息:" + post);
					if (object.optInt("success") == 0) {

						int login_type = object.optInt("login_pay", 0);
						Sputils.putSPint("login_type", login_type, context);

					} else {
						// throw new Exception("game_id或source_id错误");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 加密注册回调信息
	 * 
	 * @param context
	 * @throws Exception
	 */
	private static String encryptRegisterData(Context context, String uid,
			String userName) throws Exception {
		StringBuffer infoBuffer = new StringBuffer();
		String info = infoBuffer.append("game_id=")
				.append(DeviceUtil.getGameId(context)).append("&uid=")
				.append(uid).append("&union_id=")
				.append(DeviceUtil.getUnionid(context)).append("&brand=")
				.append(DeviceUtil.getBrand()).append("&device=")
				.append(DeviceUtil.getIMEI(context)).append("&mac=")
				.append(DeviceUtil.getMAC(context)).append("&model=")
				.append(DeviceUtil.getModel()).append("&username=")
				.append(userName).toString();
		String hexString = CryptoUtil.encodeHexString(RSACoder
				.encryptByPublicKey(info.getBytes()));
		return URLEncoder.encode(hexString, "UTF-8");

	}

}
