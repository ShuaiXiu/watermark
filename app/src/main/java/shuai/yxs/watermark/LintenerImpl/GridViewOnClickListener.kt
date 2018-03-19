package shuai.yxs.watermark.LintenerImpl

import android.graphics.Bitmap
import android.view.View
import android.widget.AdapterView
import shuai.yxs.watermark.ExtensionUtils.getBitmap
import shuai.yxs.watermark.View.BitmapImageView
import java.io.File

/**
 * GridView的点击事件
 * Created by Administrator on 2017/8/11.
 */
class GridViewOnClickListener(list: List<File>, bitmapImageView: BitmapImageView) : AdapterView.OnItemClickListener {
    private val mList = list
    //    private val mItem = onItemClick
    private var mBitImg = bitmapImageView

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        mBitImg.setBitmap(mList[position].getBitmap(mList[position]))
    }
}