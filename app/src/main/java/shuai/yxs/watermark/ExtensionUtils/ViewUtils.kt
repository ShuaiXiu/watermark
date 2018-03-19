package shuai.yxs.watermark.ExtensionUtils

import android.graphics.Bitmap
import android.view.View

/**
 * View工具
 * Created by Administrator on 2017/8/7.
 */

/**
 * 返回一个View上面的图像
 * 必须在调用后销毁缓存(只要保证Bitmap不再使用才可以销毁)
 */
fun View.getImage(view: View): Bitmap? {
    view.isDrawingCacheEnabled = true
    view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
//          FrameLayout.layout(0, 0, FrameLayout.measuredWidth, FrameLayout.measuredHeight) 如果加上此句会使FrameLayout重新绘制，水印图片跑到左上角
    return view.drawingCache
}