package com.yayawan.sdkdemo;
import java.io.File;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.yayawan.callback.YYWAnimCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YaYaWan;
import com.yayawan.sdk.callback.YayaWanUpdateCallback;
import com.yayawan.sdk.callback.YayaWanUserCallback;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.jfother.Exit_dialog;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.jfxml.GetAssetsutils;
import com.yayawan.sdk.main.YayaWan;
import com.yayawan.sdk.utils.DeviceUtil;
import com.yayawan.sdkdemo2.Savebitmap;
import com.yayawan.utils.JSONUtil;
import com.yjqy.gzlj.yyw.CesActivity;

/**
 * 接入丫丫玩只需八步哦...复制粘贴几步搞点
 * 1.复写生命周期方法,里面调用
 * YaYaWan.getInstance().onCreate(this);
 * YaYaWan.getInstance().onRestart(this);
 * YaYaWan.getInstance().onResume(this);
 * YaYaWan.getInstance().onPause(this);
 * YaYaWan.getInstance().onStop(this);
 * YaYaWan.getInstance().onDestroy(this);
 * YaYaWan.getInstance().onActivityResult(this, requestCode, resultCode, data);
 * YaYaWan.getInstance().onNewIntent(intent);
 * 
 * 2.闪屏动画,在游戏启动时的第一个页面出现..只要保证游戏画面出来前调用即可
 * YaYaWan.getInstance().anim(this, new YYWAnimCallBack(){})
 * 注意:闪屏回调有三种情况..不管闪屏失败还是成功.都应该进入游戏画面..也就是说,不管闪屏失败还是成功.游戏继续进行
 * 
 * 3.登录接口.
 * YaYaWan.getInstance().login(this, new YYWUserCallBack()} 
 * 如下面的例子:
 * 注意:登录有三个回调..第一个成功,第二个取消...注意第三个是登出..登出则是用户点击了sdk中的切换账号按钮,游戏应该注销账户
 * 回到游戏的登录页面..
 * 
 * 4.设置角色数据
 * YaYaWan.getInstance().setRoleData(MainActivity.this, user.uid, user.userName, "11", "1", "无尽之海");
 * 注意:在角色登录成功后调用,上传角色信息
 * 
 * 5.支付接口
 * YaYaWan.getInstance().pay(this, order, new YYWPayCallBack())
 * 看例子:
 * 
 * 6.退出接口
 * YaYaWan.getInstance().exit(this, new YYWExitCallback() )
 * 注意:回调执行自己的退出方法
 * 
 * 7.游戏更新接口(可选)
 * YaYaWan.getInstance().update(this, new YayaWanUpdateCallback())
 * 有热更的不需要接入
 * 
 * 8.游戏内注销按钮...(可选)应用场景,游戏内有注销账号的按钮..点击按钮后触发此事件
 * 
 * 
 * 
 * 8.复制拷贝androidmanifest文件配置
 * 
 * @author Administrator
 *
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("oncreate");
		YaYaWan.getInstance().initSdk(this);
		YaYaWan.getInstance().onCreate(this);
		
		
		
		
		
		anim(null);
		//YaYaWan.getInstance().initSdk(paramActivity);
		//upDate();
		// setContentView(R.layout.sdk_activity_main);
		final LinearLayout mLinearLayout = new LinearLayout(this);

		mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		mLinearLayout.setOrientation(LinearLayout.VERTICAL);
		mLinearLayout.setPadding(10, 10, 10, 10);

		Button animButton = new Button(this);
		animButton.setText("anim");
		animButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				anim(mLinearLayout);

			}
		});

		mLinearLayout.addView(animButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button updateButton = new Button(this);
		updateButton.setText("update");
		updateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				upDate();
			}

		});
/*
		mLinearLayout.addView(updateButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
*/
		Button lgoinButton = new Button(this);
		lgoinButton.setText("login");
		lgoinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				login(mLinearLayout);

			}
		});
		mLinearLayout.addView(lgoinButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button payButton = new Button(this);
		payButton.setText("pay");
		payButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pay(mLinearLayout);

			}
		});
		mLinearLayout.addView(payButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		Button extButton = new Button(this);
		extButton.setText("exit");
		extButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				exit(mLinearLayout);

			}
		});
		mLinearLayout.addView(extButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setContentView(mLinearLayout);
		
		
		Button versionButton = new Button(this);
		versionButton.setText("获取sdk版本号");
		versionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getversion(mLinearLayout);

			}

			
		});
		
		Button inintButton = new Button(this);
		inintButton.setText("无闪屏时的init接口");
		inintButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initsdk(mLinearLayout);

			}

		
		});
		mLinearLayout.addView(versionButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		

		Button logoutButton = new Button(this);
		logoutButton.setText("注销账号");
		logoutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Logout();

			}

		
		});
		mLinearLayout.addView(logoutButton, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		

		Button demologinButton = new Button(this);
		demologinButton.setText("demologin");
		demologinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Demologin();

			}

		
		});
	
		setContentView(mLinearLayout);

	}

	
	public void Demologin(){
		YayaWan.loginDemo(this, new YayaWanUserCallback() {
			
			@Override
			public void onSuccess(User paramUser, int paramInt) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLogout() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(int paramInt) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	/**
	 * 注销账号...使用场景,如果游戏内有注销账号的功能按钮,则点击按钮后,.调用此方法注销账号...回调成功后在onlogout中进行回到登录页面的操作
	 */
	protected void Logout() {
		// TODO Auto-generated method stub
		YaYaWan.getInstance().logout(null, new YYWUserCallBack() {
			
			@Override
			public void onLogout(Object arg0) {
				// TODO Auto-generated method stub
				//在此做回到登录页面的操作
			}
			
			@Override
			public void onLoginSuccess(YYWUser arg0, Object arg1) {
				// TODO Auto-generated method stub
				//此方法无用
			}
			
			@Override
			public void onLoginFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				//此方法无用
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				//此方法无用
			}
		});
	}

	/**
	 * 更新接口
	 */
	public void upDate() {
		YaYaWan.getInstance().update(this, new YayaWanUpdateCallback() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -1857087088399956782L;

			@Override
			public void onSuccess(File arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}
		});
	}

	public void anim(View v) {
		System.out.println("登录");
		YaYaWan.getInstance().setData(this, "", "", "", "", "", "", "");
		
		YaYaWan.getInstance().anim(this, new YYWAnimCallBack() {

			@Override
			public void onAnimSuccess(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "播放动画回调", Toast.LENGTH_SHORT)
						.show();

			}

			@Override
			public void onAnimFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimCancel(String arg0, Object arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void login(View v) {
		System.out.println("登录");
		YaYaWan.getInstance().login(this, new YYWUserCallBack() {

			@Override
			public void onLogout(Object arg0) {
				Toast.makeText(MainActivity.this, "退出",
						Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onLoginSuccess(YYWUser user, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("登录成功" +user);
				Toast.makeText(MainActivity.this, "登录成功" + user,
						Toast.LENGTH_SHORT).show();
				//登录成功后设置角色数据
				YaYaWan.getInstance().setRoleData(MainActivity.this, user.uid, user.userName, "11", "1", "无尽之海");
				YaYaWan.getInstance().setData(MainActivity.this, user.uid,
						 user.userName, "11", "1", "无尽之海",System.currentTimeMillis()/1000+"","1");
			}

			@Override
			public void onLoginFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("失败" );
				Toast.makeText(MainActivity.this, "失败",
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				System.out.println("取消" );
				Toast.makeText(MainActivity.this, "取消",
						Toast.LENGTH_SHORT).show();
			
				
			}
			
		});
	}

	public void pay(View v) {
		
		
		YYWOrder order = new YYWOrder(UUID.randomUUID().toString(), "霜之哀伤", 10l,
				"");
		
		YaYaWan.getInstance().pay(this, order, new YYWPayCallBack() {
			@Override
			public void onPaySuccess(YYWUser arg0, YYWOrder arg1, Object arg2) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "充值成功回调", Toast.LENGTH_SHORT)
						.show();
			}
			@Override
			public void onPayFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("支付失败");
			}
			@Override
			public void onPayCancel(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				System.out.println("支付退出");
			}
		});
	}
	
	/**
	 * 获取sdk版本号
	 * @param mLinearLayout
	 */
	private void getversion(LinearLayout mLinearLayout) {
		// TODO Auto-generated method stub
		
		Toast.makeText(this, YayaWan.getSdkversion(), 0).show();
		
	//	new Exit_dialog(this).dialogShow();
		
YaYaWan.getInstance().update(this, new YayaWanUpdateCallback() {
			
			@Override
			public void onSuccess(File file) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * 退出接口
	 * @param v
	 */
	public void exit(View v) {
		System.out.println("登录");
		Intent intent = new Intent(this,CesActivity.class);
		startActivityForResult(intent, 10);
	
		YaYaWan.getInstance().exit(this, new YYWExitCallback() {

			@Override
			public void onExit() {
				Toast.makeText(MainActivity.this, "退出回调", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
	
	/**
	 * 无法接闪屏时的init接口
	 * @param mLinearLayout
	 */
	private void initsdk(LinearLayout mLinearLayout) {
		// TODO Auto-generated method stub
		YaYaWan.getInstance().initSdk(this);
	}

	//游戏中调出sdk小助手可选
	public void accountManage(View v) {
		YaYaWan.getInstance().manager(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		System.out.println("onrestart");
		YaYaWan.getInstance().onRestart(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onresume");
		YaYaWan.getInstance().onResume(this);
		//YaYaWan.getInstance().onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		YaYaWan.getInstance().onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		YaYaWan.getInstance().onStop(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		YaYaWan.getInstance().onDestroy(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Yayalog.loger("demoonActivityResult");
		YaYaWan.getInstance().onActivityResult(this, requestCode, resultCode, data);
		
	}
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		YaYaWan.getInstance().onNewIntent(intent);
	}

	
	public void onWindowFocusChanged(boolean hasFocus) {
		
	};
}