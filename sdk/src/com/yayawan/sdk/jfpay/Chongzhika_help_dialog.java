package com.yayawan.sdk.jfpay;

import com.yayawan.sdk.jflogin.StringConstants;
import com.yayawan.sdk.jflogin.ViewConstants;
import com.yayawan.sdk.jfutils.Basedialogview;
import com.yayawan.sdk.jfxml.GetAssetsutils;
import com.yayawan.sdk.jfxml.MachineFactory;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Chongzhika_help_dialog extends Basedialogview {

	public Chongzhika_help_dialog(Activity activity) {
		super(activity);
	}

	@Override
	public void createDialog(Activity mActivity) {
		dialog = new Dialog(mContext);

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		baselin = new LinearLayout(mContext);
		baselin.setOrientation(LinearLayout.VERTICAL);
		MachineFactory machineFactory = new MachineFactory(mActivity);
		machineFactory.MachineView(baselin, ViewConstants.getHoldDialogWith(mContext), ViewConstants.getHoldDialogHeight(mContext), "LinearLayout");
		baselin.setBackgroundColor(Color.WHITE);
		baselin.setGravity(Gravity.CENTER_VERTICAL);

		// 中间内容
		LinearLayout ll_content = new LinearLayout(mContext);
		machineFactory.MachineView(ll_content, ViewConstants.getHoldDialogWith(mContext), 450, "LinearLayout");
		ll_content.setBackgroundColor(Color.WHITE);
		ll_content.setGravity(Gravity.CENTER_HORIZONTAL);
		ll_content.setOrientation(LinearLayout.VERTICAL);

		TextView chongzhihelp1 = new TextView(mActivity);
		machineFactory.MachineTextView(chongzhihelp1, MATCH_PARENT,
				WRAP_CONTENT, 0, StringConstants.CHONGZHIKA_HELP1, 28,
				mLinearLayout, 20, 20, 20, 0);
		
		TextView chongzhihelp2 = new TextView(mActivity);
		machineFactory.MachineTextView(chongzhihelp2, MATCH_PARENT,
				WRAP_CONTENT, 0, StringConstants.CHONGZHIKA_HELP2, 28,
				mLinearLayout, 20, 20, 20, 0);
		TextView chongzhihelp3 = new TextView(mActivity);
		machineFactory.MachineTextView(chongzhihelp3, MATCH_PARENT,
				WRAP_CONTENT, 0, StringConstants.CHONGZHIKA_HELP3, 28,
				mLinearLayout, 20, 20, 20, 0);
		
		
		ll_content.addView(chongzhihelp1);
		ll_content.addView(chongzhihelp2);
		ll_content.addView(chongzhihelp3);

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
	}

}
