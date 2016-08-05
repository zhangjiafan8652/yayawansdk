package com.yayawan.sdkdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * 作者： XuDiWei
 * <p/>
 * 日期：2015/7/27 11:41.
 * <p/>
 * 文件描述:
 */
public class MyIndicator extends HorizontalScrollView {

	private Context mContext;
	private String[] title = { "页面1", "页面2", "页面3", "页面4", "页面5", "页面6", "页面7",
			"页面8", "页面9", };
	private RadioGroup rg;
	private ImageView vIndicator;
	private ImageView vIndicator1;

	public MyIndicator(Context context) {
		super(context);
		this.mContext = context;
		initView();
	}

	public MyIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initView();
	}

	public MyIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		initView();
	}

	private void initView() {
		LinearLayout ll = new LinearLayout(mContext);
		ll.setOrientation(LinearLayout.VERTICAL);
		HorizontalScrollView.LayoutParams llParams = new HorizontalScrollView.LayoutParams(
				HorizontalScrollView.LayoutParams.MATCH_PARENT,
				HorizontalScrollView.LayoutParams.WRAP_CONTENT);
		// ll.setLayoutParams(llParams);
		ll.setLayoutParams(llParams);
		// 向LinearLayout里添加RadioGroup
		initChannel();
		ll.addView(rg);

		DisplayMetrics dm = new DisplayMetrics();
		Activity mactivity=(Activity) mContext;
		mactivity.getWindowManager().getDefaultDisplay().getMetrics(dm);

		int screenWidth = dm.widthPixels;

		int screenHeigh = dm.heightPixels;
		vIndicator1 = new ImageView(mContext);
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				1);
		vIndicator1.setLayoutParams(params2);
		vIndicator1.setBackgroundColor(Color.RED);

		// 向Linearlayout 里添加指示器(ImageView)
		initIndicator();

		ll.addView(vIndicator);
		ll.addView(vIndicator1);

		// 最后再把Linearlayout添加到当前控制（HorizontalScrollView）
		this.addView(ll);

	}

	/**
	 * 初始化RadoiGroup
	 */
	private void initChannel() {
		rg = new RadioGroup(mContext);
		rg.setOrientation(RadioGroup.HORIZONTAL);
		rg.setFadingEdgeLength(0);
		LinearLayout.LayoutParams rgParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		rg.setLayoutParams(rgParams);

		// 向RadioGroup里添加RadioButton
		// = null;
		for (int i = 0; i < title.length; i++) {
			RadioButton rb = new RadioButton(mContext);
			RadioGroup.LayoutParams rbParams = new RadioGroup.LayoutParams(
					RadioGroup.LayoutParams.WRAP_CONTENT,
					RadioGroup.LayoutParams.WRAP_CONTENT);
			rb.setLayoutParams(rbParams);
			rb.setButtonDrawable(new BitmapDrawable());
			rb.setGravity(Gravity.CENTER);
			rb.setText(title[i]);
			rb.setTextSize(20);
			rb.setTextColor(Color.GRAY);
			rb.setPadding(10, 10, 10, 10);

			rg.addView(rb);
			// System.out.println("++++++++++"+rb.getWidth());
		}
	}

	/**
	 * 初始化指示器
	 */
	private void initIndicator() {

		vIndicator = new ImageView(mContext);
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 10);
		vIndicator.setLayoutParams(params1);
		vIndicator.setBackgroundColor(Color.RED);
	}

}
