package shuai.yxs.watermark.yxsUtils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * 管理软键盘
 * Created by Administrator on 2017/7/29.
 */

object KeySoftUtils {
    /**
     * 获取软键盘显示状态
     */
    private fun getInputKeyState(context: Context): InputMethodManager? =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    /**
     * 帖子评论收起软键盘
     */
    fun PackUpInputKey(inputMethodManager: InputMethodManager?, v: View) {
        inputMethodManager?.hideSoftInputFromWindow(v.windowToken, 0)
    }

    /**
     * 首页收起软键盘
     */
    fun PackUpInputKey(mActivity: Activity) {
        val inputKey = getInputKeyState(mActivity)
        inputKey?.hideSoftInputFromWindow(mActivity.window.decorView.windowToken, 0)
    }

}
