package com.yayawan.sdkdemo2;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.yayawan.sdk.utils.ToastUtil;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Environment;

public class Savebitmap {

	public static Bitmap savePassword(String password,String username, Context mcontext) {

		Bitmap copy = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(copy);

		String testString = password;
		Paint mPaint = new Paint();
		mPaint.setStrokeWidth(3);
		mPaint.setTextSize(40);
		mPaint.setColor(Color.RED);
		canvas.drawColor(Color.YELLOW);

		mPaint.setTextAlign(Align.LEFT);
		Rect bounds = new Rect();
		mPaint.getTextBounds(testString, 0, testString.length(), bounds);
		canvas.drawText(testString, 500 / 2 - bounds.width() / 2,
				500 / 2 + bounds.height() / 2, mPaint);

		
		try {
			File myCaptureFile = new File(Environment.getExternalStorageDirectory()
					+ "/"+username + ".jpg");
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(myCaptureFile));
			copy.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ToastUtil.showSuccess(mcontext, "密码保存在sd卡中");

		return copy;
	}
}
