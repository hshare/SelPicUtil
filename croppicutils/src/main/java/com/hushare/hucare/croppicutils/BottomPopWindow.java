package com.hushare.hucare.croppicutils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * 功能/模块 ：从底部弹出的三选框
 *
 * @author huzeliang
 * @version 1.0 2017/3/21
 * @see ***
 * @since ***
 */
public class BottomPopWindow {
    /**
     * 上下文
     */
    private Context context;
    /**
     * 控件的布局
     */
    private View myView;
    /**
     * 点击这块布局，控件收起
     */
    private RelativeLayout rlDismiss;
    /**
     * 第一个按钮
     */
    private Button btFirstbar;
    /**
     * 第二个按钮
     */
    private Button btSecondbar;
    /**
     * 取消按钮
     */
    private Button btCancelbar;
    /**
     * popupWindow
     */
    private PopupWindow popupWindow;
    /**
     * 点击事件回调接口
     */
    private PopClick popClick;

    public BottomPopWindow(Context context, PopClick popClick) {
        this.popClick = popClick;
        this.context = context;
    }

    /**
     * 点击事件的回调接口
     */
    public interface PopClick {
        /**
         * 点击第一个按钮的回调
         */
        void firstClick();

        /**
         * 点击第二个按钮的回调
         */
        void secondClick();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        btFirstbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                popClick.firstClick();
            }
        });
        btSecondbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                popClick.secondClick();
            }
        });
        btCancelbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        rlDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * dip转px
     *
     * @param context 上下文
     * @param dpValue dip值
     * @return px值
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    /**
     * 初始化控件
     *
     * @param firstText  第一个按钮显示的文字
     * @param secondText 第二个按钮显示的文字
     */
    public void initTitleWindow(String firstText, String secondText) {
        myView = LayoutInflater.from(context).inflate(R.layout.pop_bottom_view_dfjkf, null);
        btFirstbar = (Button) myView.findViewById(R.id.bt_firstbar);
        btSecondbar = (Button) myView.findViewById(R.id.bt_secondbar);
        btCancelbar = (Button) myView.findViewById(R.id.bt_cancelbar);
        rlDismiss = (RelativeLayout) myView.findViewById(R.id.rl_dismiss);
        btFirstbar.setText(firstText);
        btSecondbar.setText(secondText);
        initListener();
        popupWindow = new PopupWindow(myView, ViewGroup.LayoutParams.MATCH_PARENT,
                ((Activity) context).getWindowManager().getDefaultDisplay().getHeight()
                        - dip2px(context, 0)
                        - getStatusHeight(context), true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.bottom_pop_detail);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
    }

    /**
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStatusHeight(Context context) {
        boolean statusHeight = false;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        int statusHeight1 = localRect.top;
        if (0 == statusHeight1) {
            try {
                Class localClass = Class.forName("com.android.internal.R$dimen");
                Object e = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(e).toString());
                statusHeight1 = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }

        return statusHeight1;
    }

    /**
     * 显示当前控件
     *
     * @param view view
     */
    public void showTitlePop(View view) {
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
}
