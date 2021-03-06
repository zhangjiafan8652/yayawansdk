package com.yayawan.sdk.jflogin;

import java.util.UUID;
import java.util.zip.CRC32;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yayawan.sdk.account.dao.UserDao;
import com.yayawan.sdk.account.engine.ObtainData;
import com.yayawan.sdk.base.AgentApp;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.jfutils.Basedialogview;
import com.yayawan.sdk.jfutils.Utilsjf;
import com.yayawan.sdk.jfxml.GetAssetsutils;
import com.yayawan.sdk.jfxml.MachineFactory;
import com.yayawan.sdk.utils.CryptoUtil;
import com.yayawan.sdk.utils.DeviceUtil;

public class AcountRegister_ho_dialog extends Basedialogview {

	private LinearLayout ll_mPre;
	private ImageButton iv_mPre;
	private EditText et_mUser;
	private EditText et_mPassword;
	private ImageButton ib_mAgreedbox;
	private Button bt_mOk;
	private String mName;
	private String mPassword;

	private User mUser;
	private static final int REGISTER = 3;

	private static final int FETCHSMS = 4;

	protected static final int ERROR = 5;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Utilsjf.stopDialog();
			switch (msg.what) {
			case REGISTER:
				if (mUser != null) {
					if (mUser.success == 0) {
						
						// 将用户信息保存到全局变量
						AgentApp.mUser = mUser;
						// 将base64加密的用户信息保存到数据库
						UserDao.getInstance(mContext).writeUser(mName,
								mPassword, mUser.secret);
						//System.out.println("打印一下拿到的是什么" + mUser);
						mUser.password = "";
						mUser.secret = "";
						// 开启悬浮窗服务
						//YayaWan.init(mActivity);
						allDismiss();
						Login_success_dialog login_success_dialog = new Login_success_dialog(
								mActivity);
						login_success_dialog.dialogShow();
					} else {
						Toast.makeText(mContext, mUser.body, Toast.LENGTH_SHORT)
								.show();
						//onCancel();
					}
				}
				break;
			case FETCHSMS:
				User user = (User) msg.obj;
				if (user.success == 0) {
					
					// 将用户信息保存到全局变量
					AgentApp.mUser = user;
					// 将base64加密的用户信息保存到数据库
					UserDao.getInstance(mContext).writeUser(user.userName,
							user.password, user.secret);
					user.password = "";
					user.secret = "";
					allDismiss();
				} else {
					Toast.makeText(mContext, "注册失败,请重新注册", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case ERROR:
				Toast.makeText(mContext, "网络连接错误,请重新连接", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}

	};
	private ImageButton ib_mNotAgreedbox;

	public AcountRegister_ho_dialog(Activity activity) {
		super(activity);
	}

	@Override
	public void createDialog(final Activity mActivity) {

		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		int ho_height = 650;
		int ho_with = 750;
		int po_height = 650;
		int po_with = 700;

		int height = 0;
		int with = 0;
		// 设置横竖屏
		String orientation = DeviceUtil.getOrientation(mContext);
		if (orientation == "") {

		} else if ("landscape".equals(orientation)) {
			height = ho_height;
			with = ho_with;
		} else if ("portrait".equals(orientation)) {
			height = po_height;
			with = po_with;
		}

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, with, height, "LinearLayout");
		baselin.setBackgroundColor(Color.TRANSPARENT);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 过度中间层
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, with, height, "LinearLayout",2,25);
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		// 标题栏
		RelativeLayout rl_title = new RelativeLayout(mContext);
		machineFactory.MachineView(rl_title, MATCH_PARENT, 96, mLinearLayout);
		rl_title.setBackgroundColor(Color.parseColor("#999999"));

		ll_mPre = new LinearLayout(mContext);
		machineFactory.MachineView(ll_mPre, 96, MATCH_PARENT, 0,
				mRelativeLayout, 0, 0, 0, 0, RelativeLayout.CENTER_VERTICAL);
		ll_mPre.setGravity(Gravity_CENTER);
		ll_mPre.setClickable(true);
		// 返回上一层的图片
		iv_mPre = new ImageButton(mContext);
		machineFactory.MachineView(iv_mPre, 40, 40, 0, mLinearLayout, 0, 0, 0,
				0, RelativeLayout.CENTER_VERTICAL);
		iv_mPre.setClickable(false);

		iv_mPre.setBackgroundDrawable(GetAssetsutils.getDrawableFromAssetsFile(
				"yaya_pre.png", mActivity));
		ll_mPre.addView(iv_mPre);
		// 设置点击事件.点击窗口消失
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		// 注册textview
		TextView tv_zhuce = new TextView(mContext);
		machineFactory.MachineTextView(tv_zhuce, MATCH_PARENT, MATCH_PARENT, 0,
				"账号注册", 38, mLinearLayout, 0, 0, 0, 0);
		tv_zhuce.setTextColor(Color.WHITE);
		tv_zhuce.setGravity(Gravity_CENTER);

		// TODO
		rl_title.addView(ll_mPre);
		rl_title.addView(tv_zhuce);

		// 中间内容层
		LinearLayout ll_content1 = new LinearLayout(mContext);
		ll_content1 = (LinearLayout) machineFactory.MachineView(ll_content1,
				660, MATCH_PARENT, 0, mLinearLayout, 0, 20, 0, 0,
				LinearLayout.VERTICAL);
		ll_content1.setOrientation(LinearLayout.VERTICAL);

		// 用户名
		et_mUser = new EditText(mContext);
		machineFactory.MachineEditText(et_mUser, MATCH_PARENT, 96, 0, "请输入账号",
				30, mLinearLayout, 0, 20, 0, 0);
		et_mUser.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_biankuang2.9.png", mContext));
		et_mUser.setPadding(machSize(20), 0, 0, 0);
		
		// et_mUser.setText("你懂得");

		// 密码
		et_mPassword = new EditText(mContext);
		machineFactory.MachineEditText(et_mPassword, MATCH_PARENT, 96, 0,
				"请输入您的密码(4-14位,建议数字,字母组合)", 30, mLinearLayout, 0, 20, 0, 0);
		et_mPassword.setBackgroundDrawable(GetAssetsutils
				.get9DrawableFromAssetsFile("yaya_biankuang2.9.png", mContext));
		et_mPassword.setPadding(machSize(20), 0, 0, 0);

		// 条款
		LinearLayout ll_clause = new LinearLayout(mContext);
		machineFactory.MachineView(ll_clause, MATCH_PARENT, 60, mLinearLayout,
				2, 30);
		ll_clause.setGravity(Gravity.CENTER_VERTICAL);

		// 同意服务条款
		ib_mAgreedbox = new ImageButton(mContext);
		machineFactory.MachineView(ib_mAgreedbox, 50, 50, mLinearLayout, 2, 5);
		ib_mAgreedbox.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_checkedbox.png", mActivity));
		ib_mAgreedbox.setBackgroundDrawable(null);

		// bu同意服务条款
		ib_mNotAgreedbox = new ImageButton(mContext);
		machineFactory.MachineView(ib_mNotAgreedbox, 50, 50, mLinearLayout, 2,
				5);
		ib_mNotAgreedbox.setImageBitmap(GetAssetsutils.getImageFromAssetsFile(
				"yaya_checkbox.png", mActivity));
		ib_mNotAgreedbox.setBackgroundDrawable(null);
		ib_mNotAgreedbox.setVisibility(View.GONE);

		TextView tv_agree = new TextView(mContext);
		machineFactory.MachineTextView(tv_agree, MATCH_PARENT, MATCH_PARENT, 0,
				"同意YY玩服务条款协议", 30, mLinearLayout, 6, 0, 0, 0);
		tv_agree.setTextColor(Color.GRAY);
		tv_agree.setGravity(Gravity.CENTER_VERTICAL);
		tv_agree.setClickable(true);
		tv_agree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				YYprotocol_ho_dialog yYprotocol_ho_dialog = new YYprotocol_ho_dialog(
						mActivity);
				yYprotocol_ho_dialog.dialogShow();
			}
		});

		// TODO
		ll_clause.addView(ib_mAgreedbox);
		ll_clause.addView(ib_mNotAgreedbox);
		ll_clause.addView(tv_agree);
		ib_mAgreedbox.setClickable(true);
		ib_mAgreedbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ib_mAgreedbox.setVisibility(View.GONE);
				ib_mNotAgreedbox.setVisibility(View.VISIBLE);
			}
		});
		ib_mNotAgreedbox.setClickable(true);
		ib_mNotAgreedbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ib_mNotAgreedbox.setVisibility(View.GONE);
				ib_mAgreedbox.setVisibility(View.VISIBLE);
			}
		});

		// 确定按钮
		bt_mOk = new Button(mContext);
		machineFactory.MachineButton(bt_mOk, MATCH_PARENT, 96, 0, "确认", 36,
				mLinearLayout, 0, 50, 0, 0);
		bt_mOk.setTextColor(Color.WHITE);
		bt_mOk.setBackgroundDrawable(GetAssetsutils.crSelectordraw(
				"yaya_yellowbutton.9.png", "yaya_yellowbutton1.9.png",
				mActivity));
		bt_mOk.setGravity(Gravity_CENTER);

		// TODO
		ll_content1.addView(et_mUser);
		ll_content1.addView(et_mPassword);
		ll_content1.addView(ll_clause);
		ll_content1.addView(bt_mOk);

		ll_content.addView(rl_title);

		ll_content.addView(ll_content1);

		baselin.addView(ll_content);

		dialog.setContentView(baselin);

		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.alpha = 0.9f; // 透明度

		lp.dimAmount = 0.5f; // 设置背景色对比度
		dialogWindow.setAttributes(lp);
		dialog.setCanceledOnTouchOutside(false);

		android.widget.RelativeLayout.LayoutParams ap2 = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);

		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setBackgroundDrawable(new BitmapDrawable());

		initlogic();
	}

	private void initlogic() {
		UUID uuid = UUID.randomUUID();
		CRC32 crc32 = new CRC32();
		crc32.update(uuid.toString().getBytes());
		
		et_mUser.setText("yy" + crc32.getValue());
		et_mPassword.setText(CryptoUtil.getSeed());

		bt_mOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mName = et_mUser.getText().toString().trim();
				mPassword = et_mPassword.getText().toString().trim();
				if (ib_mAgreedbox.getVisibility() == View.GONE) {
					Toast.makeText(mContext, "请同意YY玩服务条款协议", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (mName.equals("")) {
					Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT)
							.show();
				} else if (mPassword.equals("")) {
					Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT)
							.show();
				} else if (mName.length() < 6) {
					Toast.makeText(mContext, "用户名不能小于六位", Toast.LENGTH_SHORT)
							.show();
				} else if (mName.length() > 20) {
					Toast.makeText(mContext, "用户名不能大于20位", Toast.LENGTH_SHORT)
							.show();
				} else if (mPassword.length() < 6) {
					Toast.makeText(mContext, "密码不能小于六位", Toast.LENGTH_SHORT)
							.show();
				} else if (mPassword.length() > 20) {
					Toast.makeText(mContext, "密码不能大于20位", Toast.LENGTH_SHORT)
							.show();
				} else {
					Utilsjf.creDialogpro(mActivity, "正在快速注册...");
					new Thread() {

						@Override
						public void run() {
							try {
								mUser = ObtainData.register(mContext, mName,
										mPassword);
								mHandler.sendEmptyMessage(REGISTER);
							} catch (Exception e) {
								mHandler.sendEmptyMessage(ERROR);
								e.printStackTrace();
							}
						}
					}.start();
				}
			}
		});

	}

}
