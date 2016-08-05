package com.yayawan.proxy;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Intent;

import com.yayawan.callback.YYWAnimCallBack;
import com.yayawan.callback.YYWExitCallback;
import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.callback.YYWUserCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWRole;
import com.yayawan.impl.ActivityStubImpl;
import com.yayawan.impl.AnimationImpl;
import com.yayawan.impl.ChargerImpl;
import com.yayawan.impl.LoginImpl;
import com.yayawan.impl.UserManagerImpl;
import com.yayawan.implyy.ChargerImplyy;
import com.yayawan.implyy.LoginImplyy;
import com.yayawan.implyy.SingleChargerImplyy;
import com.yayawan.implyy.YYcontants;
import com.yayawan.main.YYWMain;
import com.yayawan.sdk.callback.YayaWanCallback;
import com.yayawan.sdk.callback.YayaWanUpdateCallback;
import com.yayawan.sdk.jflogin.ViewConstants;
import com.yayawan.sdk.jfother.JFupdateUtils;
import com.yayawan.sdk.jfutils.Sputils;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.main.YayaWan;
import com.yayawan.utils.DeviceUtil;

public class CommonGameProxy implements YYWGameProxy {

	private YYWLoginer mLogin;

	private YYWCharger mCharger;

	private YYWActivityStub mStub;

	private YYWUserManager mUserManager;

	private YYWAnimation mAnimation;
	
	private Activity mActivity;
	
	public CommonGameProxy() {

		this(new LoginImpl(), new ActivityStubImpl(), new UserManagerImpl(),
				new ChargerImpl());

		setAnimation(new AnimationImpl());

	}

	public CommonGameProxy(YYWLoginer login, YYWActivityStub stub,
			YYWUserManager userManager, YYWCharger charger) {
		super();
		// new CommonGameProxy();
		this.mLogin = login;
		this.mStub = stub;
		this.mUserManager = userManager;
		this.mCharger = charger;
	}

	public void setLogin(YYWLoginer login) {
		this.mLogin = login;
		
	}

	public void setCharger(YYWCharger charger) {
		this.mCharger = charger;
	}

	public void setStub(YYWActivityStub stub) {
		this.mStub = stub;
	}

	public void setUserManager(YYWUserManager userManager) {
		this.mUserManager = userManager;
	}

	public void setAnimation(YYWAnimation animation) {
		this.mAnimation = animation;
	}

	@Override
	public void login(Activity paramActivity, YYWUserCallBack userCallBack) {
		
		//检测是否调用类
		GameApitest.getGameApitestInstants(paramActivity).sendTest("login");
		
		YYWMain.mUserCallBack = userCallBack;
		
		//判断本地支付方式
		int loca_login_type = Sputils.getSPint("loca_login_type", 4,
				paramActivity);

		switch (loca_login_type) {
		case 0:
			this.mLogin.login(paramActivity, userCallBack, "login");
			break;
		case 1:
			this.mLogin = new LoginImplyy();
			this.mLogin.login(paramActivity, userCallBack, "login");
			break;
		case 2:
			this.mLogin = new LoginImplyy();
			this.mLogin.login(paramActivity, userCallBack, "login");
			break;
		case 3:
			this.mLogin.login(paramActivity, userCallBack, "login");
			break;
		case 4:
			//默认4为第一次安装。从激活处获取网络获取的支付方式
			int login_type = Sputils.getSPint("login_type", 0, paramActivity);

			switch (login_type) {

			case 0:
				this.mLogin.login(paramActivity, userCallBack, "login");
				break;
			case 1:
				this.mLogin = new LoginImplyy();
				this.mLogin.login(paramActivity, userCallBack, "login");
				break;
			case 2:
				this.mLogin = new LoginImplyy();
				this.mLogin.login(paramActivity, userCallBack, "login");
				break;
			case 3:
				this.mLogin.login(paramActivity, userCallBack, "login");
				break;

			default:
				break;
			}
			Sputils.putSPint("loca_login_type", login_type, paramActivity);
			
			break;

		default:
			break;
		}
		//如果第一次是用丫丫玩登陆则不能改变支付方式。所以出了本地登陆状态为1的还得检查一下是否为3.。如果为3.则调用单机支付
		if (loca_login_type!=1) {
			int login_type = Sputils.getSPint("login_type", 0, paramActivity);
			Yayalog.loger("我更改了loca："+loca_login_type);
			if (login_type==3||login_type==0) {
				Sputils.putSPint("loca_login_type", login_type, paramActivity);
			}
		}
		
	}

	@Override
	public void logout(Activity paramActivity, YYWUserCallBack userCallBack) {
		YYWMain.mUserCallBack = userCallBack;
		this.mLogin.relogin(paramActivity, userCallBack, "relogin");
	}
	
	public void logout(Activity paramActivity) {
	
		this.mUserManager.logout(paramActivity, null, null);
	}

	/**
	 * 检测更新
	 */
	public void update(Activity mActivity,
			YayaWanUpdateCallback mYayawanUpdatecallback) {
		GameApitest.getGameApitestInstants(mActivity).sendTest("update");
		YayaWan.update(mActivity, mYayawanUpdatecallback);

	}

	@Override
	public void charge(Activity paramActivity, YYWOrder order,
			YYWPayCallBack payCallBack) {
		YYWMain.mPayCallBack = payCallBack;
		YYWMain.mOrder = order;
		this.mCharger.charge(paramActivity, order, payCallBack);
	}

	@Override
	public void pay(Activity paramActivity, YYWOrder order,
			YYWPayCallBack payCallBack) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("pay");
		YYWMain.mPayCallBack = payCallBack;
		YYWMain.mOrder = order;
		int loca_login_type = Sputils.getSPint("loca_login_type", 4,
				paramActivity);
		Yayalog.loger("支付时候loca_login_type："+loca_login_type);
		switch (loca_login_type) {
		case 0:
			this.mCharger.pay(paramActivity, order, payCallBack);
			break;
		case 1:
			this.mCharger=new ChargerImplyy();
			this.mCharger.pay(paramActivity, order, payCallBack);
			break;
		case 2:
			this.mCharger.pay(paramActivity, order, payCallBack);
			break;
		case 3:
			//用单机支付 
			this.mCharger=new SingleChargerImplyy();
			this.mCharger.pay(paramActivity, order, payCallBack);
			break;

		default:
			break;
		}

		
	}

	@Override
	public void manager(Activity paramActivity) {
		this.mUserManager.manager(paramActivity);

	}

	@Override
	public void exit(final Activity paramActivity,
			final YYWExitCallback exitCallBack) {
		YYWMain.mExitCallback = exitCallBack;
		GameApitest.getGameApitestInstants(paramActivity).sendTest("exit");
		if (DeviceUtil.isHaveexit(paramActivity)) {
			Yayalog.loger("ishaveexit");
			paramActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					YayaWan.Exitgame(paramActivity, new YayaWanCallback() {

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							// exitCallBack.onExit();
							paramActivity.finish();
						}
					});
				}
			});
			// 这这在执行一次。防止某些游戏在某些sdk中无法完全退出
			// this.mUserManager.exit(paramActivity, exitCallBack);
		} else {
			
			int loca_login_type = Sputils.getSPint("loca_login_type", 4,
					paramActivity);
			Yayalog.loger("loca_login_type"+loca_login_type+"退出");
			if (loca_login_type == 1 ) {
				YayaWan.Exitgame(paramActivity, new  YayaWanCallback() {
					
					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						paramActivity.finish();
					}
				});
			} else {
				this.mUserManager.exit(paramActivity, exitCallBack);
			}
		}
	}

	@Override
	public void anim(Activity paramActivity, YYWAnimCallBack animCallback) {
		YYWMain.mAnimCallBack = animCallback;
		GameApitest.getGameApitestInstants(paramActivity).sendTest("anim");
		this.mAnimation.anim(paramActivity);

	}

	@Override
	public void setRoleData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName) {
		// TODO Auto-generated method stub
		GameApitest.getGameApitestInstants(paramActivity).sendTest("setRoleData");
		YYWMain.mRole = new YYWRole(roleId, roleName, roleLevel, zoneId,
				zoneName);
		this.mUserManager.setRoleData(paramActivity);
	}
	
	//3.15版兼容角色信息接口
	public void setData(Activity paramActivity, String roleId,
			String roleName, String roleLevel, String zoneId, String zoneName,String roleCTime,String ext){
		Yayalog.loger("调用了commongameproxy中setData");
		YYWMain.mRole = new YYWRole(roleId, roleName, roleLevel, zoneId,
				zoneName,roleCTime,ext);
		
		
		GameApitest.getGameApitestInstants(paramActivity).sendTest("setData"+ext,YYWMain.mRole.toString());
		// 为了兼容老sdk判断是否有setdata接口方法再执行
				Method[] methods;
				try {
					methods = Class.forName("com.yayawan.impl.UserManagerImpl").getMethods();
					for (int i = 0; i < methods.length; i++) {
						if (methods[i].getName().equals("setData")) {
							//com.yayawan.proxy.YYWActivityStub
							Yayalog.loger(methods[i].getName());
							Yayalog.loger("有3.15版本设置角色信息接口setData");
							UserManagerImpl mani=(UserManagerImpl) this.mUserManager;
							mani.setData(paramActivity, roleId, roleName, roleLevel, zoneId, zoneName, roleCTime, ext);
									
						}
						//System.out.println("没有初始化方法");
						Yayalog.loger("UserManagerImpl中没有setdata方法");
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		
	}

	@Override
	public void applicationInit(Activity paramActivity) {
		this.mStub.applicationInit(paramActivity);
	}

	boolean newactive = true;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    

	@Override
	public void onCreate(final Activity paramActivity) {
		// 进行检查更新
		// Yayalog.loger("新版更新在oncreate中启动了");
		YYcontants.ISDEBUG=DeviceUtil.isDebug(paramActivity);
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onCreate");
		mActivity=paramActivity;
		new JFupdateUtils(paramActivity).startUpdate();
		mStub.onCreate(paramActivity);

	}

	@Override
	public void onStop(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onStop");
		this.mStub.onStop(paramActivity);

	}

	@Override
	public void onResume(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onResume");
		int loca_login_type = Sputils.getSPint("loca_login_type", 4,
				paramActivity);
		if (loca_login_type == 1 || loca_login_type == 2) {
			YayaWan.init(paramActivity);
		} else {
			this.mStub.onResume(paramActivity);
		}

	}

	@Override
	public void onPause(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onPause");
		int loca_login_type = Sputils.getSPint("loca_login_type", 4,
				paramActivity);
		if (loca_login_type == 1 || loca_login_type == 2) {
			YayaWan.stop(paramActivity);
		} else {
			this.mStub.onPause(paramActivity);
		}
	}

	@Override
	public void onRestart(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onRestart");
		this.mStub.onRestart(paramActivity);
	}

	@Override
	public void onDestroy(Activity paramActivity) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onDestroy");
		this.mStub.onDestroy(paramActivity);
	}

	@Override
	public void applicationDestroy(Activity paramActivity) {
		
		this.mStub.applicationDestroy(paramActivity);
	}

	@Override
	public void onActivityResult(Activity paramActivity, int paramInt1,
			int paramInt2, Intent paramIntent) {
		GameApitest.getGameApitestInstants(paramActivity).sendTest("onActivityResult");
		int loca_login_type = Sputils.getSPint("loca_login_type", 4,
				paramActivity);

			this.mStub.onActivityResult(paramActivity, paramInt1, paramInt2,
					paramIntent);

	

	}

	@Override
	public void onNewIntent(Intent paramIntent) {
		if (mActivity!=null) {
			GameApitest.getGameApitestInstants(mActivity).sendTest("onNewIntent");
		}
		
		this.mStub.onNewIntent(paramIntent);
	}

	@Override
	public void initSdk(Activity paramActivity) {
		// TODO Auto-generated method stub
		GameApitest.getGameApitestInstants(paramActivity).sendTest("initSdk");
		//Class.forName("ActivityStubImpl").
		// 为了兼容老sdk判断是否有初始化方法再执行
		Method[] methods;
		try {
			methods = Class.forName("com.yayawan.impl.ActivityStubImpl").getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].getName().equals("initSdk")) {
					//com.yayawan.proxy.YYWActivityStub
					Yayalog.loger("有初始化方法com.yayawan.impl.ActivityStubImpl");
					
						/*	if (DeviceUtil.getUnionid(paramActivity).equals("1876218605")||
									DeviceUtil.getUnionid(paramActivity).equals("yaya1876218605")) {
								this.mStub.initSdk(paramActivity);
							}*/
								
							
				}
				//System.out.println("没有初始化方法");
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
