package com.macrowell.pipimy.utility;

import tw.com.pipimy.app.android.R;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.macrowell.pipimy.me.edit.MeEditFragment.PopMenuCallBack;

/**
 * 底部彈出選單
 */
public class BottomPopupWindow extends PopupWindow implements PopMenuCallBack {

	private View mMenuView;

	public BottomPopupWindow(Context context, View view) {
		super(context);
		mMenuView = view;

		this.setContentView(mMenuView);// 設置SelectPopupWindow的View
		this.setWidth(LayoutParams.MATCH_PARENT);// 設置彈出視窗的寬
		this.setHeight(LayoutParams.WRAP_CONTENT);// 設置彈出視窗的高
		this.setFocusable(true);// 設置SelectPopupWindow彈出視窗可點擊
		this.setAnimationStyle(R.style.AnimBottom);// 設置彈出視窗動畫效果
		ColorDrawable dw = new ColorDrawable(0xb0000000);// 實例化一個ColorDrawable顏色為半透明

		this.setBackgroundDrawable(null);// 設置彈出視窗的背景

		// mMenuView添加OnTouchListener監聽判斷獲取觸屏位置如果在選擇框外面則銷毀彈出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}

	@Override
	public void closeWindow() {
		dismiss();
	}

}
