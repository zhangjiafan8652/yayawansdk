package com.yayawan.sdk.jfsmallhelp;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.jxutils.HttpUtils;
import com.lidroid.jxutils.exception.HttpException;
import com.lidroid.jxutils.http.RequestParams;
import com.lidroid.jxutils.http.ResponseInfo;
import com.lidroid.jxutils.http.callback.RequestCallBack;
import com.lidroid.jxutils.http.client.HttpRequest.HttpMethod;
import com.yayawan.sdk.base.AgentApp;
import com.yayawan.sdk.jflogin.ViewConstants;
import com.yayawan.sdk.jfxml.Guideview_xml_po;
import com.yayawan.sdk.utils.DeviceUtil;
import com.yayawan.sdk.utils.HttpUtil;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GuideView extends BaseContentView {

	private Guideview_xml_po mThisview;
	private LinearLayout ll_activity;
	private LinearLayout ll_strategy;
	private LinearLayout ll_gamedata;
	private String shortname;

	public GuideView(Activity activity) {
		super(activity);
	}

	@Override
	public View initview() {
		mThisview = new Guideview_xml_po(mActivity);
		return mThisview.initViewxml();
	}

	@Override
	public void initdata() {
		// 活动
		ll_activity = mThisview.getLl_activity();

		// 游戏攻略
		ll_strategy = mThisview.getLl_strategy();

		// 游戏资料
		ll_gamedata = mThisview.getLl_gamedata();

		getShortname();

		// 活动
		ll_activity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Activity_ho_dialog activity_ho_dialog = new Activity_ho_dialog(
						mActivity, shortname);
				activity_ho_dialog.dialogShow();
			}
		});

		// 攻略
		ll_strategy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					Strategy_ho_dialog strategy_ho_dialog = new Strategy_ho_dialog(mActivity);
					strategy_ho_dialog.dialogShow();
			}
		});

		// 游戏数据
		ll_gamedata.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Gamedata_ho_dialog gamedata_ho_dialog = new Gamedata_ho_dialog(mActivity);
				gamedata_ho_dialog.dialogShow();
			}
		});
	}

	private void getShortname() {

		HttpUtils httpUtils = new HttpUtils();

		final String url = "http://www.yayawan.com/api/game_view?id="
				+ DeviceUtil.getGameId(mContext) + "&type=1";

		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				//System.out.println(responseInfo.result);
				
				try {
					JSONObject object = new JSONObject(responseInfo.result);
					ViewConstants.shortname = (String) object.get("shortname");
				} catch (JSONException e) {
					
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {

			}
		});

	}


}
