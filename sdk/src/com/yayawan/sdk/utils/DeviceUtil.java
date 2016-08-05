package com.yayawan.sdk.utils;

import java.util.ArrayList;

import com.yayawan.sdk.base.AgentApp;
import com.yayawan.sdk.domain.PayMethod;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class DeviceUtil {

    private static final String GAME_ID = "yayawan_game_id";

    private static final String SOURCE_ID = "yayawan_source_id";

    private static final String GAME_KEY = "yayawan_game_key";

    private static final String YAYAWAN_HELPER = "yayawan_helper";

    private static final String YAYAWAN_ORIENTATION = "yayawan_orientation";

    private static final String[] YAYAWANPAYMETHOD = { "yaya_alipay",
            "yaya_visa", "yaya_yayabi", "yaya_cash", "yaya_yidong",
            "yaya_liantong", "yaya_dianxin", "yaya_shengda", "yaya_junwang",
            "yaya_qq","yaya_wxpay","yaya_yinlian"};

    private static final int ALIPAYCODE = 1;

    private static final int ALIPAYMSPCODE = 8;

    private static final int YAYABICODE = 4;

    private static final int YIDONGCODE = 11;

    private static final int DIANXINCODE = 15;

    private static final int LIANTONGCODE = 12;

    private static final int YIBAOCODE = 7;

    private static final int JUNWANGCODE = 16;

    private static final int SHENGDACODE = 13;

    private static final int QQCODE = 20;
    
    private static final int WXPAYCODE = 10;
    private static final int YINLIAN = 30;

    /**
     * 获取清单文件中的<meta-data>标签信息
     * 
     * @param paramContext
     * @return
     */
    public static Bundle getMetaDataInfo(Context paramContext) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = paramContext.getPackageManager()
                    .getApplicationInfo(paramContext.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appInfo.metaData == null)
            return null;
        return appInfo.metaData;
    }

    /**
     * 获取gameid信息
     * 
     * @param paramContext
     * @return
     */
    public static String getGameId(Context paramContext) {
        Bundle dataInfo = getMetaDataInfo(paramContext);
        if (((dataInfo = getMetaDataInfo(paramContext)) == null)
                || (dataInfo.get(GAME_ID) == null)) {
            throw new IllegalArgumentException("must set the yayawan_game_id");
        }
        return dataInfo.getString(GAME_ID).replace("yaya", "");
    }

    /**
     * 获取渠道id
     * 
     * @param paramContext
     * @return
     */
    public static String getSourceId(Context paramContext) {
        
        if (AgentApp.mSourceId != null && !"".equals(AgentApp.mSourceId.trim())) {
            return AgentApp.mSourceId;
        }
        Bundle dataInfo;
        if (((dataInfo = getMetaDataInfo(paramContext)) == null)
                || (dataInfo.get(SOURCE_ID) == null)) {
            return null;
        }
        return dataInfo.get(SOURCE_ID).toString();
    }

    /**
     * 获取gamekey
     * 
     * @param paramContext
     * @return
     */
    public static String getGameKey(Context paramContext) {
        Bundle dataInfo;
        if (((dataInfo = getMetaDataInfo(paramContext)) == null)
                || (dataInfo.get(GAME_KEY) == null)) {
            return null;
        }
        return dataInfo.get(GAME_KEY).toString();
    }

    /**
     * 获取是否可以隐藏小助手信息
     * 
     * @param paramContext
     * @return
     */
    public static boolean getYayaWanHelper(Context paramContext) {
        Bundle dataInfo;
        if (((dataInfo = getMetaDataInfo(paramContext)) == null)
                || (dataInfo.get(YAYAWAN_HELPER) == null)) {
            return true;
        }
        return dataInfo.getBoolean(YAYAWAN_HELPER);
    }

    /**
     * 获取支付方式   只有清单文件AndroidManifest.xml中设置了并且为true的支付方式才显示出来
     * 
     * @param paramContext
     * @return
     */
    public static ArrayList<PayMethod> getYayaWanMethod(Context paramContext) {
        Bundle dataInfo;
        ArrayList<PayMethod> paymethods = initPayMethod(paramContext);
        
        ArrayList<PayMethod> exclude = new ArrayList<PayMethod>();
        int size = paymethods.size();
        for (String method : YAYAWANPAYMETHOD) {
        	//dataInfo是清单文件中该支付的值。为true或者false
        	
            if (((dataInfo = getMetaDataInfo(paramContext)) == null)
                    || (dataInfo.get(method) == null)
                    || !dataInfo.getBoolean(method)) {
            	 for (int i = 0; i < size; i++) {
                     PayMethod payMethod = paymethods.get(i);
                     if (method.equals(payMethod.payName)) {
                         exclude.add(payMethod);
                     }
                 }
            } 
        }
        paymethods.removeAll(exclude);
        return paymethods;
    }

    /**
     * 获取横竖屏信息
     * 
     * @param paramContext
     * @return
     */
    public static String getOrientation(Context paramContext) {
        Bundle dataInfo;
        if (((dataInfo = getMetaDataInfo(paramContext)) == null)
                || (dataInfo.get(YAYAWAN_ORIENTATION) == null)) {
            return "";
        }
        return dataInfo.getString(YAYAWAN_ORIENTATION);
    }

    /**
     * 获取手机IMEI
     * 
     * @param paramContext
     * @return
     */
    public static String getIMEI(Context paramContext) {
        // 获取设备的imei号
        String deviceId = ((TelephonyManager) paramContext
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        // 如果imei为空,获取mac地址
        if (deviceId == null || "0".equals(deviceId)) {
            WifiManager wifi = (WifiManager) paramContext
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            deviceId = getDEC(info.getMacAddress());
        }
        return deviceId;
    }

    /**
     * 判断是否是手机
     * 
     * @param paramContext
     * @return
     */
    public static boolean isPhone(Context paramContext) {
        // 获取设备的imei号
        String deviceId = ((TelephonyManager) paramContext
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        // 如果imei为空,获取mac地址
        if (deviceId == null || "0".equals(deviceId)) {
            return false;
        }
        return true;
    }

    /**
     * 获取程序包名
     * 
     * @param paramContext
     * @return
     */
    public static String getPackageName(Context paramContext) {
        PackageManager localPackageManager = paramContext.getPackageManager();
        try {
            return localPackageManager.getPackageInfo(
                    paramContext.getPackageName(), 0).packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取sim卡信息
     * 
     * @param paramContext
     * @return
     */
    public static String getSim(Context paramContext) {
        return ((TelephonyManager) paramContext
                .getSystemService(Context.TELEPHONY_SERVICE))
                .getSimSerialNumber();
    }

    /**
     * 获取MAC地址
     * 
     * @param paramContext
     * @return
     */
    public static String getMAC(Context paramContext) {
        return (((WifiManager) paramContext
                .getSystemService(Context.WIFI_SERVICE)).getConnectionInfo())
                .getMacAddress();
    }

    /**
     * 获取手机型号
     * 
     * @return
     */
    public static String getModel() {
        String model = Build.MODEL;
        return model;
    }

    /**
     * 获取手机品牌
     * 
     * @return
     */
    public static String getBrand() {
        return Build.BRAND;
    }

    /**
     * 获取mac地址的十进制
     * 
     * @param mac
     * @return
     */
    public static String getDEC(String mac) {
        String[] split = mac.split(":");
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < split.length; i++) {
            int ii = Integer.parseInt(split[i], 16);
            buffer.append(ii);
        }
        return buffer.toString();
    }

    /**
     * 初始化支付方式列表
     * 
     * @param context
     * @return
     */
    public static ArrayList<PayMethod> initPayMethod(Context context) {
        ArrayList<PayMethod> paymethods = new ArrayList<PayMethod>();
       // if (Utils.isExistMsp(context)) {
            paymethods.add(new PayMethod("yaya_alipay",  ALIPAYMSPCODE));
       // } else {
         //   paymethods.add(new PayMethod("yaya_alipay",  ALIPAYCODE));
      //  }
//        paymethods.add(new PayMethod("yaya_alipay", ResourceUtil
//                .getDrawableId(context, "alipay_icon"), ALIPAYCODE));
        paymethods.add(new PayMethod("yaya_visa", YIBAOCODE));
        paymethods.add(new PayMethod("yaya_yayabi", YAYABICODE));
        paymethods.add(new PayMethod("yaya_cash",  YIBAOCODE));
        paymethods.add(new PayMethod("yaya_yidong", YIDONGCODE));
        paymethods.add(new PayMethod("yaya_liantong",  LIANTONGCODE));
        paymethods.add(new PayMethod("yaya_dianxin",  DIANXINCODE));
        paymethods.add(new PayMethod("yaya_shengda", SHENGDACODE));
        paymethods.add(new PayMethod("yaya_junwang",  JUNWANGCODE));
        paymethods.add(new PayMethod("yaya_qq", QQCODE));
        paymethods.add(new PayMethod("yaya_wxpay", WXPAYCODE));
        paymethods.add(new PayMethod("yaya_yinlian", YINLIAN));
        return paymethods;
    }

}
