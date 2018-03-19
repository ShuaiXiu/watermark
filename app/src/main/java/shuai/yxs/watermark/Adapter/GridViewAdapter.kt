package shuai.yxs.watermark.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.item_gridview.view.*
import shuai.yxs.watermark.ExtensionUtils.loadImg
import shuai.yxs.watermark.R
import java.io.File

/**
 * Grid的适配器
 * Created by Administrator on 2017/8/10.
 */
class GridViewAdapter(array: List<File>, context: Context) : BaseAdapter() {
    private val mArray = array
    private val mContext = context
    override fun getCount(): Int = mArray.size


    override fun getItem(position: Int): Any = mArray[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_gridview, parent, false)
        view.Item_Img_one.loadImg(mArray[position], view.Item_Img_one)
        return view
    }
}
