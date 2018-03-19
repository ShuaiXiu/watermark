package shuai.yxs.watermark.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_recycler_grid.view.*
import shuai.yxs.watermark.LintenerImpl.GridViewOnClickListener
import shuai.yxs.watermark.R
import shuai.yxs.watermark.View.BitmapImageView
import shuai.yxs.watermark.application.App
import java.io.File

/**
 * RecyclerAdapter的适配器
 * Created by Administrator on 2017/8/10.
 */
class RecyclerViewAdapter(context: Context, array: () -> Array<File>, bitmapImageView: BitmapImageView) : RecyclerView.Adapter<MyViewHolder>() {
    private var mFun = array
    private var mArray = array.invoke()
    private val mContext = context
    private var mBitImg = bitmapImageView
    private var i = 0
    override fun getItemCount(): Int {
        return (mArray.size / 4) + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(App.context).inflate(R.layout.item_recycler_grid, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        i = position * 4
        val list = mArray.slice(i..i + 3).filterNotNull()  //数组切割 并筛选掉元素为null的值
        holder?.itemView?.Item_Recycler_GridView?.adapter = GridViewAdapter(list, mContext)
        holder?.itemView?.Item_Recycler_GridView?.onItemClickListener = GridViewOnClickListener(list, mBitImg)
    }

    fun onRefresh() {
        mArray = mFun.invoke()
        this.notifyDataSetChanged()
    }

    fun setList(array: Array<File>) {
        mArray = array
        notifyDataSetChanged()
    }
}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)