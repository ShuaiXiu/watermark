package shuai.yxs.watermark.LintenerImpl

import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import shuai.yxs.watermark.View.BitmapImageView

/**
 * 组合控件的滑动监听
 * Created by Administrator on 2017/8/4.
 */
open class MyOnTouchListener(v: View, frameLayout: FrameLayout, bitmapImageView: BitmapImageView) : View.OnTouchListener {
    private var mBitImg = bitmapImageView
    private val mView = v
    private var startX: Int = 0
    private var startY: Int = 0

    /**
     * 标记位，如果位移距离过小，不拦截事件，不然会多次点击无效
     */
    private var X: Int = 0
    private var Y: Int = 0

    private var flag = false
    private var mFrameLayout = frameLayout
    private val width: Int by lazy {
        //        mActivity.windowManager.defaultDisplay.width
        frameLayout.width

    }
    val height: Int by lazy {
        //        mActivity.windowManager.defaultDisplay.height
        frameLayout.height
    }

    private var startTime = 0L
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startTime = System.currentTimeMillis()
                startX = event.rawX.toInt()
                startY = event.rawY.toInt()
                X = event.rawX.toInt()
                Y = event.rawY.toInt()
                flag = true
                mFrameLayout.parent.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE -> {
                flag = false
                val endX = event.rawX.toInt()
                val endY = event.rawY.toInt()
                val dx = endX - startX
                val dy = endY - startY
                val l = mView.left + dx
                val r = mView.right + dx
                val t = mView.top + dy
                val b = mView.bottom + dy
                if (r < width && b < height && l > 0 && t > 0) {
                    val params = mView.layoutParams as FrameLayout.LayoutParams
                    params.leftMargin = l
                    params.topMargin = t
                    mView.layoutParams = params
                }

                startX = event.rawX.toInt()
                startY = event.rawY.toInt()

            }
            MotionEvent.ACTION_UP -> { //标记是否消耗事件
                val endTime = System.currentTimeMillis()
                if (!flag) { //如果移动距离大于5 不触发单击事件
                    if (Math.abs(startX - X) > 5 || Math.abs(startY - Y) > 5) {
                        return true
                    }
                }
                //如果滑动时间超过两秒 并且移动距离小于5清空所选图片
                if (Math.abs(startX - X) < 5 || Math.abs(startY - Y) < 5) {
                    if (endTime - startTime > 1000L) {
                        mBitImg.DestroyImg()
                        return true
                    }
                }
                mFrameLayout.parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return false
    }
}