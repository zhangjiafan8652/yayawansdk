package com.yayawan.sdk.jfxml;

import java.util.Properties;

import com.yayawan.sdk.base.AgentApp;
import com.yayawan.sdk.jflogin.ViewConstants;
import com.yayawan.sdk.jfpay.Help_dialog;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.main.YayaWan;
import com.yayawan.sdk.utils.DeviceUtil;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Yayapay_mainxml_po extends Basexml implements Layoutxml {

	private LinearLayout baseLinearLayout;
	private ImageButton iv_mPre;
	private TextView tv_mYuanbao;
	private TextView tv_mMoney;
	private RelativeLayout rl_mAlipay;
	private RelativeLayout rl_mChuxuka;
	private RelativeLayout rl_mXinyongka;
	private RelativeLayout rl_mYidong;
	private RelativeLayout rl_mLiantong;
	private RelativeLayout rl_mDianxin;
	private RelativeLayout rl_mShengda;
	private RelativeLayout rl_mJunka;
	private RelativeLayout rl_mYaya;
	private RelativeLayout rl_mQbi;
	private LinearLayout ll_mPre;
	private TextView tv_mHelp;
	private TextView tv_mMoney1;
	private RelativeLayout rl_mYinlianpay;
	private RelativeLayout rl_mWxpay;
	private Button bt_mMorepay;
	private GridLayout gl_mPlaylist;
	private String mpaytostring;

	public Yayapay_mainxml_po(Activity activity) {
		super(activity);
	}

	@Override
	public View initViewxml() {
		// 基类布局
		baseLinearLayout = new LinearLayout(mContext);
		/*
		 * android.view.ViewGroup.LayoutParams layoutParams = new
		 * ViewGroup.LayoutParams( 600, 600);
		 * baseLinearLayout.setBackgroundColor(Color.WHITE);
		 * baseLinearLayout.setLayoutParams(layoutParams);
		 * baseLinearLayout.setOrientation(LinearLayout.VERTICAL);
		 * baseLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		 */

		baseLinearLayout.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		
		machineFactory.MachineView(baseLinearLayout, ViewConstants.getHoldActivityWith(mContext), ViewConstants.getHoldActivityHeight(mContext), "LinearLayout");
		baseLinearLayout.setBackgroundColor(Color.WHITE);
		baseLinearLayout.setGravity(Gravity.CENTER_VERTICAL);

		// 设置长度需要baseliner和relative两个设置
		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mContext);
		
		//ViewConstants.getHoldActivityWith(mContext)全局的所有窗口化activity的宽
		
		machineFactory.MachineView(rl_title, ViewConstants.getHoldActivityWith(mContext), 96, mLinearLayout);
		rl_title.setBackgroundColor(Color.parseColor("#fffbde"));
		

		ll_mPre = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPre, 96, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.ALIGN_PARENT_RIGHT);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mContext);
		machineFactory.MachineView(iv_mPre, 45, 45, 0, mLinearLayout, 0, 0, 0,
				0, 0);
		/*
		 * iv_mPre.setImageDrawable(GetAssetsutils.getDrawableFromAssetsFile(
		 * "yaya_pre.png", mContext));
		 */
		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya_close.png", mActivity));

		ll_mPre.addView(iv_mPre);
		iv_mPre.setClickable(false);

		// 注册textview
		TextView tv_zhuce = new TextView(mContext);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"充值", 40, mRelativeLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.parseColor("#6a6961"));
		tv_zhuce.setGravity(Gravity_CENTER);

		tv_mHelp = new TextView(mContext);
		machineFactory.MachineTextView(tv_mHelp, WRAP_CONTENT, MATCH_PARENT, 0,
				"帮助", 36, mRelativeLayout, 0, 0, 20, 0,
				RelativeLayout.ALIGN_PARENT_RIGHT);
		tv_mHelp.setTextColor(Color.parseColor("#f1f1f1"));
		tv_mHelp.setGravity(Gravity_CENTER);
		tv_mHelp.setClickable(true);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);
		
		// rl_title.addView(tv_mHelp);

		ScrollView sv_mContent = new ScrollView(mContext);
		machineFactory.MachineView(sv_mContent, MATCH_PARENT, ViewConstants.getHoldActivityHeight(mContext)-96,
				mLinearLayout);

		LinearLayout ll_mContent = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mContent, MATCH_PARENT, MATCH_PARENT,
				mLinearLayout);
		ll_mContent.setOrientation(LinearLayout.VERTICAL);
		ll_mContent.setGravity(Gravity_CENTER);

		// 顶部金额的条目
		LinearLayout ll_moneyitem = new LinearLayout(mContext);
		machineFactory.MachineView(ll_moneyitem, MATCH_PARENT, 100,
				mLinearLayout);
		ll_moneyitem.setOrientation(LinearLayout.HORIZONTAL);

		// 多少元宝
		tv_mYuanbao = new TextView(mContext);
		machineFactory.MachineTextView(tv_mYuanbao, 0, MATCH_PARENT, 1,
				"300元宝", 32, mLinearLayout, 0, 0, 20, 0);
		tv_mYuanbao.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		// tv_mYuanbao.setGravity(Gravity.RIGHT);

		// 分割线
		TextView tv_hline = new TextView(mContext);
		machineFactory.MachineView(tv_hline, 2, MATCH_PARENT, mLinearLayout);
		tv_hline.setBackgroundColor(Color.parseColor("#d5d5d5"));

		// 金额多少
		tv_mMoney1 = new TextView(mContext);
		machineFactory.MachineTextView(tv_mMoney1, WRAP_CONTENT, MATCH_PARENT,
				0, "金额:￥", 32, mLinearLayout, 20, 0, 0, 0);
		tv_mMoney1.setGravity(Gravity.CENTER_VERTICAL);

		tv_mMoney = new TextView(mContext);
		machineFactory.MachineTextView(tv_mMoney, 0, MATCH_PARENT, 1, "", 32,
				mLinearLayout, 0, 0, 0, 0);
		tv_mMoney.setGravity(Gravity.CENTER_VERTICAL);
		tv_mMoney.setTextColor(Color.parseColor("#ff9900"));

		// TODO
		ll_moneyitem.addView(tv_mYuanbao);
		ll_moneyitem.addView(tv_hline);
		ll_moneyitem.addView(tv_mMoney1);
		ll_moneyitem.addView(tv_mMoney);

		TextView tv_fastpay = markView("请选择支付方式：");
		rl_mAlipay = createItemView("支付宝支付", "yaya_zhifu.png");
		rl_mYinlianpay = createItemView("银联卡支付", "yaya_yinlian.png");

		rl_mChuxuka = createItemView("储蓄卡支付", "yaya_chuxuka.png");
		rl_mXinyongka = createItemView("信用卡支付", "yaya_xinyongka.png");
		rl_mYidong = createItemView("充值卡支付", "yaya_yidong.png");

		rl_mLiantong = createItemView("联通充值卡", "yaya_liantong.png");
		rl_mDianxin = createItemView("电信充值卡", "yaya_dianxin.png");
		rl_mShengda = createItemView("盛大充值卡", "yaya_shengda.png");

		rl_mJunka = createItemView("骏卡一网通", "yaya_junka.png");
		rl_mYaya = createItemView("丫丫币充值", "yaya_yayabi.png");
		rl_mQbi = createItemView("QQ卡充值", "yaya_qqpay.png");

		rl_mWxpay = createItemView("微信支付    ", "yaya_weixinpay.png");
		// TODO
		ll_mContent.addView(ll_moneyitem);
		ll_mContent.addView(createLine());
		ll_mContent.addView(tv_fastpay);

		gl_mPlaylist = new GridLayout(mContext);
		machineFactory.MachineView(gl_mPlaylist, WRAP_CONTENT, WRAP_CONTENT,
				mLinearLayout);
		gl_mPlaylist.setOrientation(GridLayout.HORIZONTAL);
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			gl_mPlaylist.setColumnCount(3);
		} else if ("portrait".equals(orientation)) {
			gl_mPlaylist.setColumnCount(2);
		}
		
		gl_mPlaylist.setPadding(10, 10, 10, 15);
		
		
		

		AgentApp.mPayMethods = DeviceUtil.getYayaWanMethod(mContext);

		mpaytostring = AgentApp.mPayMethods.toString();

		
		if (mpaytostring.contains("yaya_alipay")) {
			gl_mPlaylist.addView(rl_mAlipay);
		}
		if (mpaytostring.contains("yaya_wxpay")) {
			gl_mPlaylist.addView(rl_mWxpay);
		}
		if (mpaytostring.contains("yaya_qq")) {
			gl_mPlaylist.addView(rl_mQbi);
		}

		if (mpaytostring.contains("yaya_yinlian")) {
			gl_mPlaylist.addView(rl_mYinlianpay);
		}

		if (mpaytostring.contains("yaya_visa")) {
			gl_mPlaylist.addView(rl_mXinyongka);
		}

		if (mpaytostring.contains("yaya_cash")) {
			gl_mPlaylist.addView(rl_mChuxuka);
		}

		

		// button更多支付按钮
		bt_mMorepay = new Button(mActivity);
		bt_mMorepay = machineFactory.MachineButton(bt_mMorepay, 300, 76, 1,
				"更多支付方式", 30, mLinearLayout, 0, 10, 0, 0);
		bt_mMorepay.setTextColor(Color.WHITE);
		bt_mMorepay.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
				mActivity));
		bt_mMorepay.setGravity(Gravity_CENTER);
		
		bt_mMorepay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (mpaytostring.contains("yaya_liantong")) {
					gl_mPlaylist.addView(rl_mLiantong);
				}

				if (mpaytostring.contains("yaya_dianxin")) {
					gl_mPlaylist.addView(rl_mDianxin);
				}

				if (mpaytostring.contains("yaya_yidong")) {
					gl_mPlaylist.addView(rl_mYidong);
				}
				if (mpaytostring.contains("yaya_junwang")) {
					gl_mPlaylist.addView(rl_mJunka);
				}

				if (mpaytostring.contains("yaya_shengda")) {
					gl_mPlaylist.addView(rl_mShengda);
				}
				bt_mMorepay.setVisibility(View.GONE);
			}
		});

		ll_mContent.addView(gl_mPlaylist);
		ll_mContent.addView(bt_mMorepay);
	
		sv_mContent.addView(ll_mContent);

		baseLinearLayout.addView(rl_title);
		baseLinearLayout.addView(sv_mContent);

		return baseLinearLayout;
	}

	public RelativeLayout getRl_mWxpay() {
		return rl_mWxpay;
	}

	public void setRl_mWxpay(RelativeLayout rl_mWxpay) {
		this.rl_mWxpay = rl_mWxpay;
	}

	private RelativeLayout createItemView(String name, String iconname) {
		RelativeLayout relativeLayout = new RelativeLayout(mContext);

		machineFactory.MachineView(relativeLayout, 300, 100, 0, "GridLayout",
				15, 15, 0, 0, 0);
		relativeLayout.setGravity(Gravity_CENTER);

		ImageView iv_payicon = new ImageView(mContext);
		machineFactory.MachineView(iv_payicon, 60, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.ALIGN_PARENT_LEFT);
		iv_payicon.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				iconname, mActivity));

		TextView tv_alipay = new TextView(mContext);

		machineFactory.MachineTextView(tv_alipay, WRAP_CONTENT, MATCH_PARENT,
				0, name, 30, mRelativeLayout, 90, 0, 0, 0);
		tv_alipay.setGravity(Gravity.CENTER_VERTICAL);

		// TODO
		relativeLayout.addView(iv_payicon);
		relativeLayout.addView(tv_alipay);

		relativeLayout.setBackground(GetAssetsutils.get9DrawableFromAssetsFile(
				"yaya_paynormal_bg.9.png", mContext));

		return relativeLayout;

	}

	private TextView markView(String name) {
		TextView textview = new TextView(mContext);
		machineFactory.MachineTextView(textview, MATCH_PARENT, 70, 0, name, 30,
				mLinearLayout, 20, 0, 0, 0);
		textview.setTextColor(Color.parseColor("#737373"));
		textview.setGravity(Gravity.CENTER_VERTICAL);
		return textview;
	}

	private TextView createLine() {
		// 水平分割线
		TextView tv_vline = new TextView(mContext);
		machineFactory.MachineView(tv_vline, MATCH_PARENT, 2, mLinearLayout);
		tv_vline.setBackgroundColor(Color.parseColor("#d5d5d5"));
		return tv_vline;
	}

	public LinearLayout getBaseLinearLayout() {
		return baseLinearLayout;
	}

	public void setBaseLinearLayout(LinearLayout baseLinearLayout) {
		this.baseLinearLayout = baseLinearLayout;
	}

	public ImageButton getIv_mPre() {
		return iv_mPre;
	}

	public void setIv_mPre(ImageButton iv_mPre) {
		this.iv_mPre = iv_mPre;
	}

	public TextView getTv_mYuanbao() {
		return tv_mYuanbao;
	}

	public void setTv_mYuanbao(TextView tv_mYuanbao) {
		this.tv_mYuanbao = tv_mYuanbao;
	}

	public TextView getTv_mMoney() {
		return tv_mMoney;
	}

	public void setTv_mMoney(TextView tv_mMoney) {
		this.tv_mMoney = tv_mMoney;
	}

	public RelativeLayout getRl_mAlipay() {
		return rl_mAlipay;
	}

	public void setRl_mAlipay(RelativeLayout rl_mAlipay) {
		this.rl_mAlipay = rl_mAlipay;
	}

	public RelativeLayout getRl_mChuxuka() {
		return rl_mChuxuka;
	}

	public void setRl_mChuxuka(RelativeLayout rl_mChuxuka) {
		this.rl_mChuxuka = rl_mChuxuka;
	}

	public RelativeLayout getRl_mXinyongka() {
		return rl_mXinyongka;
	}

	public void setRl_mXinyongka(RelativeLayout rl_mXinyongka) {
		this.rl_mXinyongka = rl_mXinyongka;
	}

	public RelativeLayout getRl_mYidong() {
		return rl_mYidong;
	}

	public void setRl_mYidong(RelativeLayout rl_mYidong) {
		this.rl_mYidong = rl_mYidong;
	}

	public RelativeLayout getRl_mLiantong() {
		return rl_mLiantong;
	}

	public void setRl_mLiantong(RelativeLayout rl_mLiantong) {
		this.rl_mLiantong = rl_mLiantong;
	}

	public RelativeLayout getRl_mDianxin() {
		return rl_mDianxin;
	}

	public void setRl_mDianxin(RelativeLayout rl_mDianxin) {
		this.rl_mDianxin = rl_mDianxin;
	}

	public RelativeLayout getRl_mShengda() {
		return rl_mShengda;
	}

	public void setRl_mShengda(RelativeLayout rl_mShengda) {
		this.rl_mShengda = rl_mShengda;
	}

	public RelativeLayout getRl_mJunka() {
		return rl_mJunka;
	}

	public void setRl_mJunka(RelativeLayout rl_mJunka) {
		this.rl_mJunka = rl_mJunka;
	}

	public RelativeLayout getRl_mYaya() {
		return rl_mYaya;
	}

	public void setRl_mYaya(RelativeLayout rl_mYaya) {
		this.rl_mYaya = rl_mYaya;
	}

	public RelativeLayout getRl_mQbi() {
		return rl_mQbi;
	}

	public void setRl_mQbi(RelativeLayout rl_mQbi) {
		this.rl_mQbi = rl_mQbi;
	}

	public LinearLayout getLl_mPre() {
		return ll_mPre;
	}

	public void setLl_mPre(LinearLayout ll_mPre) {
		this.ll_mPre = ll_mPre;
	}

	public TextView getTv_mHelp() {
		return tv_mHelp;
	}

	public void setTv_mHelp(TextView tv_mHelp) {
		this.tv_mHelp = tv_mHelp;
	}

	public RelativeLayout getRl_mYinlianpay() {
		return rl_mYinlianpay;
	}

	public void setRl_mYinlianpay(RelativeLayout rl_mYinlianpay) {
		this.rl_mYinlianpay = rl_mYinlianpay;
	}

}
