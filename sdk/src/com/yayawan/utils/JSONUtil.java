package com.yayawan.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;

public class JSONUtil {

    /**
     * 格式化订单扩展字段
     *
     * @param wxt
     * @param id
     * @param name
     * @param uid
     * @return
     * @throws Exception
     */
    public static String formatExt(String ext, String game_id, String name,
            String uid) {

        // ext'#_yyw_{id,ui,name}

        StringBuffer buffer = new StringBuffer();

        buffer.append(ext).append("#_yyw_").append(game_id).append("|")
                .append(uid).append("|").append(name);

        String encode = "";
        try {
            encode = URLEncoder.encode(buffer.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encode;
    }

    public static String formatToken(Activity activity, String token, String name) {

        // base64_encode(token1@token@@union_id@@username@@appid)

        StringBuffer buffer = new StringBuffer();

        buffer.append(token).append("@@").append(DeviceUtil.getUnionid(activity)).append("@@").append(name).append("@@").append(DeviceUtil.getGameId(activity));

        String encode = "";
        try {
            encode = URLEncoder.encode(
                    Base64.encode(buffer.toString().getBytes()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encode;
    }
}
