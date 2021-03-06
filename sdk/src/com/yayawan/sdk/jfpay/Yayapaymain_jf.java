package com.yayawan.sdk.jfpay;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ipaynow.plugin.api.IpaynowPlugin;
import com.xqt.now.paysdk.XqtPay;
import com.xqt.now.paysdk.XqtPay.XqtPayListener;
import com.yayawan.sdk.base.AgentApp;
import com.yayawan.sdk.callback.YayaWanPaymentCallback;
import com.yayawan.sdk.domain.AlipayResult;
import com.yayawan.sdk.domain.BillResult;
import com.yayawan.sdk.domain.ConfirmPay;
import com.yayawan.sdk.domain.Order;
import com.yayawan.sdk.domain.PayMethod;
import com.yayawan.sdk.domain.PayResult;
import com.yayawan.sdk.domain.PayResult_jf;
import com.yayawan.sdk.domain.User;
import com.yayawan.sdk.jflogin.BaseLogin_Activity;
import com.yayawan.sdk.jflogin.BaseView;
import com.yayawan.sdk.jflogin.ViewConstants;
import com.yayawan.sdk.jfutils.Utilsjf;
import com.yayawan.sdk.jfutils.Yayalog;
import com.yayawan.sdk.jfxml.Yayapay_mainxml_po;
import com.yayawan.sdk.main.YayaWan;
import com.yayawan.sdk.payment.engine.ObtainData;
import com.yayawan.sdk.utils.DeviceUtil;
import com.yayawan.sdk.utils.DialogUtil;
import com.yayawan.sdk.utils.ToastUtil;

public class Yayapaymain_jf extends BaseView {

	private static final int REQUESTCODE = 0;
	protected static final int RESULT = 1;

	protected static final int PAYRESULT = 2;

	protected static final int ERROR = -3;
	private static final int RQF_LOGIN = 5;
	private static final int BILLRESULT = 6;
	private static final int FIRSTRESULTKUAI = 7;
	private static final int ALIPAYERROR = 19;
	private static final int NETERROR = 18;
	private static final int DATAERROR = 17;
	private static final int FIRSTRESULT = 3;
	private static final int SDK_CHECK_FLAG = 30; // 查看终端是否有支付宝认证账户
	private static final int SDK_PAY_FLAG = 31; // 查看支付结果

	private static final int WXPAY_FIRSTRESULT = 32;

	private Yayapay_mainxml_po mThisview;
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
	private YayaWanPaymentCallback mPaymentCallback;
	private YuepayDialog mYayaDialog;
	private ConfirmPay mYayapay;
	private PayResult mFirstResult;
	private BillResult mBillResult;

	private PayResult mWFirstResult;

	private int CONTROLPAYTIME = 0;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@SuppressLint("Registered")
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case RESULT:
				Utilsjf.stopDialog();
				if (Long.valueOf(AgentApp.mUser.money) >= AgentApp.mPayOrder.money
						&& AgentApp.mPayOrder.paytype == 0) {

					mYayaDialog = new YuepayDialog(mActivity);

					mYayaDialog.getTv_mYue().setText(
							Long.valueOf(AgentApp.mUser.money) + "丫丫币");

					if (AgentApp.mPayOrder.money % 100 == 0) {

						mYayaDialog.getTv_mZhifu().setText(
								+(AgentApp.mPayOrder.money) + "丫丫币 ");
					} else {
						// 除数
						BigDecimal bd = new BigDecimal(AgentApp.mPayOrder.money);
						// 被除数
						BigDecimal bd2 = new BigDecimal(100);
						// 进行除法运算,保留2位小数,末位使用四舍五入方式,返回结果
						BigDecimal result = bd.divide(bd2, 2,
								BigDecimal.ROUND_HALF_UP);
						mYayaDialog.getTv_mZhifu().setText(
								result.toString() + "丫丫币 ");
					}
					// mYayaDialog.setBtnText("立即付款");
					mYayaDialog.getBt_mOk().setOnClickListener(
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									mYayaDialog.getBt_mOk().setEnabled(false);
									new Thread() {

										@Override
										public void run() {
											// 余额支付
											try {
												mYayapay = ObtainData.yayapay(
														mContext,
														AgentApp.mPayOrder,
														AgentApp.mUser);
												mHandler.sendEmptyMessage(PAYRESULT);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}

									}.start();
								}
							});
					mYayaDialog.dialogShow();

				} else {

				}
				break;
			case PAYRESULT:
				Utilsjf.stopDialog();
				if (mYayapay.success == 0) {
					ToastUtil.showSuccess(mContext, "付款成功");
					onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
				} else {
					ToastUtil.showError(mContext, "付款失败");
					onError(0);
				}
				break;

			case ERROR:
				Utilsjf.stopDialog();
				Toast.makeText(mContext, "网络连接错误,请重新连接", Toast.LENGTH_SHORT)
						.show();
				mActivity.finish();

				break;
			case WXPAY_FIRSTRESULT:
				
				//TODO
				Utilsjf.stopDialog();
				if (mWFirstResult.success == 0) {

					XqtPay.mhtOrderNo=mWFirstResult.params.get("mhtOrderNo");
					XqtPay.payChannelType=mWFirstResult.params.get("payChannelType");
					XqtPay.consumerId=mWFirstResult.params.get("consumerId");
					
					XqtPay.mhtOrderName=mWFirstResult.params.get("mhtOrderName");
					XqtPay.mhtOrderDetail=mWFirstResult.params.get("mhtOrderDetail");
					XqtPay.mhtOrderAmt=mWFirstResult.params.get("mhtOrderAmt");
					
					XqtPay.notifyUrl=mWFirstResult.params.get("notifyUrl");
					XqtPay.superid="100000";
					XqtPay.sign=mWFirstResult.params.get("sign");
			
					
					XqtPay.Transit(mActivity,  new XqtPayListener() {
						
						@Override
						public void success(String str) {
							Yayalog.loger("支付返回的succ结果"+str);
							IpaynowPlugin.pay(mActivity, str);
						}
						
						@Override
						public void error(String str) {
							//onError(0);
							Yayalog.loger("支付返回的error结果"+str);
						}
					});
					
				/*	RequestMsg msg1 = new RequestMsg();
					Yayalog.loger("开启微信支付传的id"+mWFirstResult.params.get("token_id"));
					msg1.setTokenId(mWFirstResult.params.get("token_id"));
					msg1.setMoney(AgentApp.mPayOrder.money/100);
					
					msg1.setTradeType(MainApplication.WX_APP_TYPE);
					msg1.setAppId("wx2a5538052969956e");
					PayPlugin.unifiedAppPay(mActivity, msg1);*/
				}else{
					onError(0);
				}
				break;

			case 1111:
				/*
				 * Utilsjf.stopDialog(); Intent intent1 = new Intent(mActivity,
				 * Yinlian_Activity.class); intent1.putExtra("url", ""+mhtml);
				 */
				// intent.putExtra("type", ViewConstants.YINLIANPAY_ACTIVITY);
				// intent.putExtra("screen", 1);

				// mActivity.finish();

				// mActivity.startActivity(intent1);

				break;
			case SDK_PAY_FLAG:

			{
				PayResult_jf payResult = new PayResult_jf((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Utilsjf.stopDialog();
					onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
					ToastUtil.showSuccess(mContext, "支付成功");
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Utilsjf.stopDialog();
						Toast.makeText(mContext, "数据异常，请到我的订单查看是否付款成功，请勿重复付款。",
								Toast.LENGTH_LONG).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误

						Utilsjf.stopDialog();
						onError(0);

						ToastUtil.showError(mContext, "付款失败");

					}
				}
				break;
			}
			case SDK_CHECK_FLAG:
				Boolean flag = (Boolean) msg.obj;
				// System.out.println("我倒这里来了呀" + flag);
				if (flag) {

					AgentApp.mentid = 8;
					// intent.setClass(mContext, AlipayKuaiActivity.class);
					AlipayKuaipayNow();

				} else {
					Utilsjf.stopDialog();

					AgentApp.mentid = 1;
					AlipaypayNow();

					// intent.setClass(mContext, AlipayActivity.class);
				}

				break;

			case FIRSTRESULT:
				Utilsjf.stopDialog();

				if (mFirstResult.success == 1) {
					// 操作失败
					// onError(0);
					ToastUtil.showError(mContext, mFirstResult.error_msg);
				} else if (mFirstResult.success == 0) {
					// 第一次操作成功,再次访问网络获取第二次结果
					// if (mpDialog != null && mpDialog.isShowing()) {
					// mpDialog.dismiss();
					// }
					if ("get".equals(mFirstResult.method)) {
						Intent intent = new Intent(mContext,
								BaseLogin_Activity.class);
						intent.putExtra("result", mFirstResult);
						// ViewConstants.PAYMENT_JF
						intent.putExtra("type", ViewConstants.PAYMENT_JF);
						ViewConstants.mPayActivity = mActivity;
						mActivity.startActivity(intent);

						// PayMainActivity.this.finish();
					} else if ("post".equals(mFirstResult.method)) {
						Intent intent = new Intent(mContext,
								BaseLogin_Activity.class);
						intent.putExtra("type", ViewConstants.PAYMENT_JF);
						intent.putExtra("result", mFirstResult);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						ViewConstants.mPayActivity = mActivity;
						mActivity.startActivity(intent);
						// PayMainActivity.this.finish();
					}
				}
				break;
			case FIRSTRESULTKUAI:
				Utilsjf.stopDialog();
				if (mFirstResult.success == 1) {
					// 操作失败
					onError(0);
					ToastUtil.showError(mContext, mFirstResult.error_msg);
				} else if (mFirstResult.success == 0) {
					// 第一次操作成功,再次访问网络获取第二次结果

					Runnable payRunnable = new Runnable() {

						@Override
						public void run() {
							// 构造PayTask 对象

							PayTask alipay = new PayTask(mActivity);
							// 调用支付接口，获取支付结果
							String result = alipay.pay(mFirstResult.action);

							Message msg = new Message();
							msg.what = SDK_PAY_FLAG;
							msg.obj = result;
							mHandler.sendMessage(msg);
						}
					};

					// 必须异步调用
					Thread payThread = new Thread(payRunnable);
					payThread.start();

				}
				break;

			case BILLRESULT:
				Utilsjf.stopDialog();
				if (mBillResult != null) {
					if (mBillResult.success == 1) {
						// 第二次确认操作失败
						onError(0);
						ToastUtil.showError(mContext, mBillResult.error_msg);
					} else if (mBillResult.success == 0) {

						// 支付操作成功等待到账
						onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
						ToastUtil.showSuccess(mContext, mBillResult.body);
					}
				}

				break;

			case ALIPAYERROR:
				Utilsjf.stopDialog();
				Toast.makeText(mContext, "支付宝快捷支付出现异常,请删除后重试",
						Toast.LENGTH_LONG).show();
				break;

			case DATAERROR:
				Utilsjf.stopDialog();
				Toast.makeText(mContext, "数据异常，请到我的订单查看是否付款成功，请勿重复付款。",
						Toast.LENGTH_LONG).show();
				// finish();
				break;

			default:
				break;
			}
		}

	};

	private ArrayList<PayMethod> mMethods;
	private TextView tv_mMoney;
	private TextView tv_mYuanbao;
	private TextView tv_mHelp;
	private LinearLayout ll_mPre;

	public Yayapaymain_jf(Activity mContext) {
		super(mContext);
		
	}

	@Override
	public View initRootview() {
		// 初始化支付页面
		
		
		mThisview = new Yayapay_mainxml_po(mActivity);
		return mThisview.initViewxml();
		
	}

	@Override
	public void initView() {

		
		// 获取页面的所有视图实例对象
		rl_mAlipay = mThisview.getRl_mAlipay();
		rl_mYinlianpay = mThisview.getRl_mYinlianpay();
		
		rl_mChuxuka = mThisview.getRl_mChuxuka();
		rl_mXinyongka = mThisview.getRl_mXinyongka();
		ll_mPre = mThisview.getLl_mPre();
		ll_mPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onCancel();
			}
		});

	
		
		tv_mMoney = mThisview.getTv_mMoney();
		Order mPayOrder = AgentApp.mPayOrder;

		if (AgentApp.mPayOrder.money % 100 == 0) {

			tv_mMoney.setText("" + (AgentApp.mPayOrder.money) / 100);
		} else {
			// 除数
			BigDecimal bd = new BigDecimal(AgentApp.mPayOrder.money);
			// 被除数
			BigDecimal bd2 = new BigDecimal(100);
			// 进行除法运算,保留2位小数,末位使用四舍五入方式,返回结果
			BigDecimal result = bd.divide(bd2, 2, BigDecimal.ROUND_HALF_UP);
			tv_mMoney.setText(result.toString());
		}

		tv_mYuanbao = mThisview.getTv_mYuanbao();
		tv_mYuanbao.setText("" + mPayOrder.goods);

		rl_mYidong = mThisview.getRl_mYidong();
		rl_mLiantong = mThisview.getRl_mLiantong();
		rl_mDianxin = mThisview.getRl_mDianxin();
		rl_mShengda = mThisview.getRl_mShengda();
		rl_mJunka = mThisview.getRl_mJunka();
		rl_mYaya = mThisview.getRl_mYaya();
		rl_mQbi = mThisview.getRl_mQbi();
		// 微信支付
		rl_mWxpay = mThisview.getRl_mWxpay();

		// 先把视图隐藏，后面逻辑部分会进行有选择显示
		/*rl_mAlipay.setVisibility(View.GONE);
		 rl_mWxpay.setVisibility(View.GONE);

		rl_mChuxuka.setVisibility(View.GONE);
		rl_mXinyongka.setVisibility(View.GONE);

		rl_mYidong.setVisibility(View.GONE);
		rl_mLiantong.setVisibility(View.GONE);
		rl_mDianxin.setVisibility(View.GONE);

		rl_mShengda.setVisibility(View.GONE);
		rl_mJunka.setVisibility(View.GONE);
		rl_mQbi.setVisibility(View.GONE);

		rl_mYaya.setVisibility(View.GONE);*/

		tv_mHelp = mThisview.getTv_mHelp();
		tv_mHelp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Help_dialog help_dialog = new Help_dialog(mActivity);
				help_dialog.dialogShow();
			}
		});
	}

	public static boolean payclickcontrol = false;// 点击支付条目控制器

	@Override
	public void logic() {
		// Utilsjf.safePaydialog(mActivity, "");
		
		payclickcontrol = false;
		AgentApp.mPayMethods = DeviceUtil.getYayaWanMethod(mContext);
		
		mPaymentCallback = YayaWan.mPaymentCallback;

		for (int i = 0; i < AgentApp.mPayMethods.size(); i++) {

			String payName = AgentApp.mPayMethods.get(i).payName;
			Yayalog.loger(payName);
			if (payName.equals("yaya_alipay")) {
				rl_mAlipay.setVisibility(View.VISIBLE);
			} else if (payName.equals("yaya_visa")) {
				rl_mXinyongka.setVisibility(View.VISIBLE);
			} else if (payName.equals("yaya_yayabi")) {
				rl_mYaya.setVisibility(View.VISIBLE);
			} else if (payName.equals("yaya_cash")) {
				rl_mChuxuka.setVisibility(View.VISIBLE);
			} else if (payName.equals("yaya_yidong")) {
				rl_mYidong.setVisibility(View.VISIBLE);
			} else if (payName.equals("yaya_liantong")) {
				rl_mLiantong.setVisibility(View.VISIBLE);
			} else if (payName.equals("yaya_dianxin")) {
				rl_mDianxin.setVisibility(View.VISIBLE);
			} else if (payName.equals("yaya_shengda")) {
				rl_mShengda.setVisibility(View.VISIBLE);
			} else if (payName.equals("yaya_junwang")) {
				rl_mJunka.setVisibility(View.VISIBLE);
			} else if (payName.equals("yaya_qq")) {
				rl_mQbi.setVisibility(View.VISIBLE);
			}else if (payName.equals("yaya_wxpay")) {
				rl_mWxpay.setVisibility(View.VISIBLE);
			}

		}

		mMethods = AgentApp.mPayMethods;

		// TODO 支付宝支付
		rl_mAlipay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// PayMethod method = AgentApp.mPayMethods.get(0);

				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;

				Utilsjf.safePaydialog(mActivity, "初始化安全支付...");
				// AgentApp.mentid = 8;
				AgentApp.mentid = 31;
				// intent.setClass(mContext, AlipayKuaiActivity.class);
				AlipayKuaipayNow();
				// mPb.setVisibility(View.VISIBLE);

			}

		});

		// 微信支付
		rl_mWxpay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;

				Utilsjf.safePaydialog(mActivity, "初始化安全支付...");
				// AgentApp.mentid = 8;
				AgentApp.mentid = 32;
				// intent.setClass(mContext, AlipayKuaiActivity.class);
				WxpayKuaipayNow();
			}
		});

		// 银联支付
		rl_mYinlianpay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ViewConstants.mPayActivity = mActivity;
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;
				CeshiYinlian();
			}
		});

		// 储蓄卡支付
		rl_mChuxuka.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// PayMethod method = AgentApp.mPayMethods.get(3);
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;
				AgentApp.mentid = 7;

				if (AgentApp.mUser.cashbanks != null
						&& AgentApp.mUser.cashbanks.size() != 0) {

					Chuxuka_historypayDialog chuxukahispay = new Chuxuka_historypayDialog(
							mActivity);
					chuxukahispay.dialogShow();

				} else {
					ChuxukapayDialog yuepayDialog = new ChuxukapayDialog(
							mActivity);
					yuepayDialog.dialogShow();

				}

			}
		});

		// 信用卡支付
		rl_mXinyongka.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;
				AgentApp.mentid = 7;

				if (AgentApp.mUser.banks != null
						&& AgentApp.mUser.banks.size() != 0) {

					Xinyong_hispayDialog chuxukahispay = new Xinyong_hispayDialog(
							mActivity);
					chuxukahispay.dialogShow();
				} else {
					XinyongkapayDialog yuepayDialog = new XinyongkapayDialog(
							mActivity);
					yuepayDialog.dialogShow();

				}

			}
		});

		// 移动充值卡支付
		rl_mYidong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// PayMethod method = AgentApp.mPayMethods.get(4);
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;
				AgentApp.mentid = 11;
				Chongzhipaydialog yidong = new Chongzhipaydialog(mActivity);
				LinearLayout ll_mHelp = yidong.getLl_mHelp();
				ll_mHelp.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Chongzhika_help_dialog chongzhika_help_dialog = new Chongzhika_help_dialog(
								mActivity);
						chongzhika_help_dialog.dialogShow();
					}
				});
				yidong.dialogShow();
			}
		});

		// 联通支付
		rl_mLiantong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;
				// PayMethod method = AgentApp.mPayMethods.get(5);
				AgentApp.mentid = 12;
				Chongzhipaydialog liantong = new Chongzhipaydialog(mActivity);

				LinearLayout ll_mHelp = liantong.getLl_mHelp();
				ll_mHelp.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Chongzhika_help_dialog chongzhika_help_dialog = new Chongzhika_help_dialog(
								mActivity);
						chongzhika_help_dialog.dialogShow();
					}
				});
				liantong.dialogShow();
			}
		});

		// 电信充值卡
		rl_mDianxin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// PayMethod method = AgentApp.mPayMethods.get(6);
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;
				AgentApp.mentid = 15;
				Chongzhipaydialog dianxin = new Chongzhipaydialog(mActivity);
				LinearLayout ll_mHelp = dianxin.getLl_mHelp();
				ll_mHelp.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Chongzhika_help_dialog chongzhika_help_dialog = new Chongzhika_help_dialog(
								mActivity);
						chongzhika_help_dialog.dialogShow();
					}
				});

				dianxin.dialogShow();
			}
		});

		// 盛大充值卡充值
		rl_mShengda.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// PayMethod method = AgentApp.mPayMethods.get(7);
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;
				AgentApp.mentid = 13;
				Otherpaydialog shengda = new Otherpaydialog(mActivity);
				LinearLayout ll_mHelp = shengda.getLl_mHelp();
				ll_mHelp.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Shengda_help_dialog shengda_help_dialog = new Shengda_help_dialog(
								mActivity);
						shengda_help_dialog.dialogShow();
					}
				});
				shengda.dialogShow();
			}
		});

		// 骏卡充值卡充值
		rl_mJunka.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// PayMethod method = AgentApp.mPayMethods.get(8);
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;
				AgentApp.mentid = 16;
				Otherpaydialog junka = new Otherpaydialog(mActivity);

				LinearLayout ll_mHelp = junka.getLl_mHelp();
				ll_mHelp.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Junka_help_dialog junka_help_dialog = new Junka_help_dialog(
								mActivity);
						junka_help_dialog.dialogShow();
					}
				});

				junka.dialogShow();
			}
		});

		rl_mYaya.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		// q币充值
		rl_mQbi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (payclickcontrol) {
					return;
				}
				payclickcontrol = true;
				// PayMethod method = AgentApp.mPayMethods.get(9);

				AgentApp.mentid = 20;
				Otherpaydialog QQ = new Otherpaydialog(mActivity);
				LinearLayout ll_mHelp = QQ.getLl_mHelp();
				ll_mHelp.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						QQka_help_dialog junka_help_dialog = new QQka_help_dialog(
								mActivity);
						junka_help_dialog.dialogShow();
					}
				});

				QQ.dialogShow();
			}
		});

		// YY币展示
		rl_mYaya.setOnClickListener(new OnClickListener() {

			private YuepayDialog mYayaDialog1;

			@Override
			public void onClick(View v) {

				mYayaDialog1 = new YuepayDialog(mActivity);

				if (AgentApp.mPayOrder.money % 100 == 0) {

					mYayaDialog1.getTv_mYue().setText(
							Long.valueOf(AgentApp.mUser.money) / 100 + "丫丫币");
				} else {
					// 除数
					BigDecimal bd = new BigDecimal(AgentApp.mUser.money);
					// 被除数
					BigDecimal bd2 = new BigDecimal(100);
					// 进行除法运算,保留2位小数,末位使用四舍五入方式,返回结果
					BigDecimal result = bd.divide(bd2, 2,
							BigDecimal.ROUND_HALF_UP);
					mYayaDialog1.getTv_mYue().setText(
							result.toString() + "丫丫币 ");
				}

				if (AgentApp.mPayOrder.money % 100 == 0) {

					mYayaDialog1.getTv_mZhifu().setText(
							+(AgentApp.mPayOrder.money) / 100 + "丫丫币 ");
				} else {
					// 除数
					BigDecimal bd = new BigDecimal(AgentApp.mPayOrder.money);
					// 被除数
					BigDecimal bd2 = new BigDecimal(100);
					// 进行除法运算,保留2位小数,末位使用四舍五入方式,返回结果
					BigDecimal result = bd.divide(bd2, 2,
							BigDecimal.ROUND_HALF_UP);
					mYayaDialog1.getTv_mZhifu().setText(
							result.toString() + "丫丫币 ");
				}
				// mYayaDialog.setBtnText("立即付款");
				mYayaDialog1.getBt_mOk().setBackgroundColor(
						Color.parseColor("#d5d5d5"));
				mYayaDialog1.dialogShow();

			}
		});
	}

	public void onSuccess(User paramUser, Order paramOrder, int paramInt) {
		if (mPaymentCallback != null) {
			mPaymentCallback.onSuccess(paramUser, paramOrder, paramInt);
		}
		mPaymentCallback = null;
		mActivity.finish();
	}

	public void onError(int paramInt) {
		if (mPaymentCallback != null) {
			mPaymentCallback.onError(paramInt);
		}
		mPaymentCallback = null;
		mActivity.finish();
	}

	public void onCancel() {
		if (mPaymentCallback != null) {
			mPaymentCallback.onCancel();
		}
		mActivity.finish();
	}

	private void AlipayKuaipayNow() {

		// 进入支付流程
		new Thread() {

			@Override
			public void run() {
				try {
					mFirstResult = com.yayawan.sdk.payment.engine.ObtainData
							.payment(mContext, AgentApp.mPayOrder,
									AgentApp.mUser, AgentApp.mPayOrder.paytype);
					payclickcontrol = false;
					mHandler.sendEmptyMessage(FIRSTRESULTKUAI);
				} catch (Exception e) {
					mHandler.sendEmptyMessage(NETERROR);
				}
			}

		}.start();
	}

	// 微信支付
	private void WxpayKuaipayNow() {

		// 进入支付流程
		new Thread() {

			@Override
			public void run() {
				try {
					mWFirstResult = com.yayawan.sdk.payment.engine.ObtainData
							.wxpayment(mContext, AgentApp.mPayOrder,
									AgentApp.mUser, AgentApp.mPayOrder.paytype);
					payclickcontrol = false;
					mHandler.sendEmptyMessage(WXPAY_FIRSTRESULT);
				} catch (Exception e) {
					mHandler.sendEmptyMessage(NETERROR);
				}
			}

		}.start();
	}

	private String mhtml;
	private RelativeLayout rl_mYinlianpay;
	private RelativeLayout rl_mWxpay;

	public void CeshiYinlian() {

		
		AgentApp.mentid = 21;
		Utilsjf.creDialogpro(mActivity, "启动银联支付...");
		new Thread() {

			@Override
			public void run() {
				try {

					// System.out.println("我发送了");
					mhtml = com.yayawan.sdk.payment.engine.ObtainData
							.payment_yinlian(mActivity, AgentApp.mPayOrder,
									AgentApp.mUser, AgentApp.mPayOrder.paytype);
					// System.out.println(payment.toString());
					// mhtml = payment.toString();

					mActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Utilsjf.stopDialog();
						}
					});
					payclickcontrol = false;

					Intent intent = new Intent(mActivity,
							BaseLogin_Activity.class);
					intent.putExtra("url", "" + mhtml);
					intent.putExtra("type", ViewConstants.YINLIANPAY_ACTIVITY);
					intent.putExtra("screen", 1);
					mActivity.startActivity(intent);
					// mHandler.sendEmptyMessage(FIRSTRESULTKUAI);
					// mHandler.sendEmptyMessage(1111);

				} catch (Exception e) {
					// mHandler.sendEmptyMessage(NETERROR);
					payclickcontrol = false;
					mActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Utilsjf.stopDialog();
						}
					});
				}
			}

		}.start();

	}

	private void AlipaypayNow() {
		// 进入支付流程
		new Thread() {
			@Override
			public void run() {
				try {
					mFirstResult = com.yayawan.sdk.payment.engine.ObtainData
							.payment(mContext, AgentApp.mPayOrder,
									AgentApp.mUser, AgentApp.mPayOrder.paytype);

					mHandler.sendEmptyMessage(FIRSTRESULT);
				} catch (Exception e) {
					mHandler.sendEmptyMessage(NETERROR);
				}
			}
		}.start();
	}

	private void startAlipay() {
		Utilsjf.safePaydialog(mActivity, "初始化安全支付...");

		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(mActivity);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();
	}

	@Override
	public void startActivityForResult(Intent data, int re) {
		// System.out.println(requestCode);
		super.startActivityForResult(data, re);
	
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (data == null) {
			return;
		}
		String respCode = data.getExtras().getString("respCode");
		//String respMsg = data.getExtras().getString("respMsg");
		
		if (respCode==null) {
			Yayalog.loger("rescode为空");
			return;
		}
		if (respCode.equals("00")) {
			Yayalog.loger("微信支付成功");
			onSuccess(AgentApp.mUser, AgentApp.mPayOrder, 1);
		}

		if (respCode.equals("02")) {
			onError(0);
		}

		if (respCode.equals("01")) {
			onError(0);
		}

		if (respCode.equals("03")) {
			onError(0);
			mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(mActivity, "正在确认支付结果，请稍候查询到账情况", Toast.LENGTH_LONG).show();
				}
			});
		}
	}
	@Override
	public void onResume() {
		// System.out.println("mele");
	}
}
