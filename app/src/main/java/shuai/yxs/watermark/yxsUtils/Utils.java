package shuai.yxs.watermark.yxsUtils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * 工具
 * Created by Administrator on 2017/7/14.
 */

public class Utils {
    private static final String TAG = "Taggggg";
    private static StringBuilder stringBuilder;

    /**
     * 获取软键盘显示状态
     */
    public static InputMethodManager getInputKeyState(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return inputMethodManager;
    }

    /**
     * 帖子评论收起软键盘
     */
    public static void PackUpInputKey(InputMethodManager inputMethodManager, View v) {

        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 首页收起软键盘
     */
    public static void PackUpInputKey(Activity mActivity) {
        InputMethodManager inputKey = getInputKeyState(mActivity);
        if (inputKey != null) {
            inputKey.hideSoftInputFromWindow(mActivity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 获取输入框或文本框文字
     */
    public static String getText(TextView tv) {
        return tv.getText().toString().trim();
    }

    public static void E(String msg) {
        Log.e(TAG, msg);
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * 获取屏幕的宽度
     */
    public static int getWindowWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取屏幕的高度
     */
    public static int getWindowHight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * 转换16进制
     */
    public static String toHexString(int i){
        return Integer.toHexString(i);
    }

    public static void RequestPermission(){
//        Manifest.permission.ACCESS_CHECKIN_PROPERTIES;
    }

}
