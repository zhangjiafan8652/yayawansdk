package com.yayawan.implyy;

import java.lang.reflect.Method;
import java.math.BigInteger;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.yayawan.callback.YYWPayCallBack;
import com.yayawan.domain.YYWOrder;
import com.yayawan.domain.YYWUser;
import com.yayawan.main.YYWMain;
import com.yayawan.proxy.YYWCharger;
import com.yayawan.sdk.base.AgentApp;
import com.yayawan.sdk.callback.YayaWanPaymentCallback;
import com.yayawan.sdk.domain.Order;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.jfutils.Sputils;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.main.YayaWan;
import com.yayawan.sdk.utils.HttpUtil;
import com.yayawan.sdk.utils.UrlUtil;

public class ChargerImplyy implements YYWCharger {

    @Override
    public void charge(Activity paramActivity, YYWOrder order,
            YYWPayCallBack callback) {
    }

    @Override
    public void pay(final Activity paramActivity, final YYWOrder order, YYWPayCallBack callback) {


    	new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
            			Yayalog.loger("yy支付");
				    	Order order2 = new Order();

				        order2.orderId = order.orderId;
				        order2.goods = order.goods;
				        order2.money = order.money;
				        order2.ext = order.ext;
				        
				        int loca_login_type = Sputils.getSPint("loca_login_type", 4,
								paramActivity);
				        if (loca_login_type==3) {
				        	//单机
				        	AgentApp.mUser=new User();
				        	AgentApp.mUser.setUid(new BigInteger(YYWMain.mUser.uid));
					        AgentApp.mUser.setUser_uid(YYWMain.mUser.uid);
					        AgentApp.mUser.setUserName(YYWMain.mUser.userName);
						}

				        YayaWan.payment(paramActivity, order2, new YayaWanPaymentCallback() {

				            @Override
				            public void onCancel() {
				                if (YYWMain.mPayCallBack != null) {
				                    YYWMain.mPayCallBack.onPayCancel("cancel", "");
				                }
				            }

				            @Override
				            public void onError(int arg0) {
				                if (YYWMain.mPayCallBack != null) {
				                    YYWMain.mPayCallBack.onPayFailed("failed", "");
				                }
				            }

				            @Override
				            public void onSuccess(User user, Order order, int arg2) {
				                if (YYWMain.mPayCallBack != null) {
				                    YYWUser yywUser = new YYWUser();

				                    yywUser.uid = user.uid + "";
				                    yywUser.icon = user.icon;
				                    yywUser.body = user.body;
				                    yywUser.lasttime = user.lasttime;
				                    yywUser.money = user.money;
				                    yywUser.nick = user.nick;
				                    yywUser.password = user.password;
				                    yywUser.phoneActive = user.phoneActive;
				                    yywUser.success = user.success;
				                    yywUser.token = user.token;
				                    yywUser.userName = user.userName;

				                    YYWOrder yywOrder = new YYWOrder();

				                    yywOrder.orderId = order.orderId;
				                    yywOrder.ext = order.ext;
				                    yywOrder.gameId = order.gameId;
				                    yywOrder.goods = order.goods;
				                    yywOrder.id = order.id;
				                    yywOrder.mentId = order.mentId;
				                    yywOrder.money = order.money;
				                    yywOrder.paytype = order.paytype;
				                    yywOrder.serverId = order.serverId;
				                    yywOrder.status = order.status;
				                    yywOrder.time = order.time;
				                    yywOrder.transNum = order.transNum;
				                    
				                  
				                    pushUmengdata(order.money/100+"");
				                    
				                    YYWMain.mPayCallBack.onPaySuccess(yywUser, yywOrder, "success");
				                }
				            }

				        });

            }

    	});

    }

    //支付成功调用友盟
    public void pushUmengdata(String money){
    	try {
			Class<?> subclass = Class.forName("com.yayawan.impl.ActivityStubImpl");
			Method[] methods = subclass.getMethods();
			for (int i = 0; i < methods.length; i++) {
				if (methods[i].equals("payumenSucceed")) {
					 new   com.yayawan.impl.ActivityStubImpl().payumenSucceed(money);
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Yayalog.loger("未找到ActivityStubImpl");
		}
    }
}
