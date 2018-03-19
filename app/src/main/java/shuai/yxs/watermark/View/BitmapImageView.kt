package shuai.yxs.watermark.View

import android.content.Context
import android.graphics.Bitmap
import android.view.InflateException
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.horizontal_water_item.view.*
import kotlinx.android.synthetic.main.imageview_water_item.view.*
import kotlinx.android.synthetic.main.text_water_item.view.*
import kotlinx.android.synthetic.main.vertical_water_item.view.*
import shuai.yxs.watermark.ConstantUtils.IntegerUtils
import shuai.yxs.watermark.ExtensionUtils.*
import shuai.yxs.watermark.LintenerImpl.MyOnTouchListener
import shuai.yxs.watermark.R
import shuai.yxs.watermark.View.CommandDialog.SendCommandDialog
import shuai.yxs.watermark.application.App

/**
 * 组合控件
 * Created by Administrator on 2017/8/4.
 */
class BitmapImageView(context: Context, flag: Int) : LinearLayout(context) {
    private val mFlag = flag
    private var mText = context.getRString()
    private lateinit var mBitmap: Bitmap
    private val mView: View
    private val mContext = context

    /**
     * 根据flag加载不同布局
     */
    init {
        mView = when (mFlag) {
            IntegerUtils.ZERO -> View.inflate(App.context, R.layout.vertical_water_item, this)//垂直
            IntegerUtils.ONE -> View.inflate(App.context, R.layout.horizontal_water_item, this) //水平
            IntegerUtils.TWO -> View.inflate(App.context, R.layout.text_water_item, this)//只有文字
            IntegerUtils.THREE -> View.inflate(App.context, R.layout.imageview_water_item, this)//只有图片
            else -> {
                throw NullPointerException(context.getRString(R.string.ErrorNullLayout))
            }
        }
    }

    /**
     * 设置文字
     */
    fun setText(msg: String) {
        isTrue()
        mText = msg
        when (mFlag) {
            IntegerUtils.ZERO -> Vertical_water_msg.text = msg
            IntegerUtils.ONE -> Horizontal_water_msg.text = msg
            IntegerUtils.TWO -> Text_water_msg.text = msg
        }
    }

    /**
     * 获取所点的文本框的文字
     */
    fun getText(): String = when (mFlag) {
        IntegerUtils.ZERO -> Vertical_water_msg.text.toString()//垂直
        IntegerUtils.ONE -> Horizontal_water_msg.text.toString() //水平
        IntegerUtils.TWO -> Text_water_msg.text.toString()//只有文字
        else -> {
            context.getRString()
        }
    }

    /**
     * 返回传递进来的flag
     */
    fun getFlag(): Int = mFlag

//    fun setScaleType(type: ImageView.ScaleType) {
//        when (mFlag) {
//            IntegerUtils.ZERO -> Vertical_water_img.scaleType = type
//            IntegerUtils.ONE -> Horizontal_water_img.scaleType = type
//            IntegerUtils.THREE -> ImageView_water_img.scaleType = type
//        }
//    }

    /**
     * 设置图片
     */
    fun setBitmap(bitmap: Bitmap) {
        isTrue()
        mBitmap = bitmap
        when (mFlag) {
            IntegerUtils.ZERO -> Vertical_water_img.setImageBitmap(bitmap)
            IntegerUtils.ONE -> Horizontal_water_img.setImageBitmap(bitmap)
            IntegerUtils.THREE -> ImageView_water_img.setImageBitmap(bitmap)
        }
    }

    /**
     * 清空这个控件
     */
    fun DestroyImg() {
        when (mFlag) {
            IntegerUtils.ZERO -> {
                Vertical_water_img.setImageBitmap(null)
                Vertical_water_msg.text = ""
            }
            IntegerUtils.ONE -> {
                Horizontal_water_img.setImageBitmap(null)
                Horizontal_water_msg.text = ""
            }
            IntegerUtils.TWO -> Text_water_msg.text = ""
            IntegerUtils.THREE -> ImageView_water_img.setImageBitmap(null)
        }

    }

    /**
     * 单击事件
     */
    fun setViewOnClickListener(onCallBack: OnCallBack, onChangeTextSize: SendCommandDialog.OnChangeTextSize) {
        ShowDialog(onCallBack, onChangeTextSize)
    }

    /**
     * 长按事件 因与OnTouch冲突  暂未使用
     */
    fun setViewOnClickListener() {
        when (mFlag) {//垂直
            IntegerUtils.ZERO -> {
                Vertical_water_Lin.setOnLongClickListener {
                    mContext.ShuaiToast(mContext.getRString(R.string.ClearSuccess))
                    return@setOnLongClickListener true
                }
            }
            IntegerUtils.ONE -> {//水平
                Horizontal_water_Lin.setOnLongClickListener {
                    mContext.ShuaiToast(mContext.getRString(R.string.ClearSuccess))
                    return@setOnLongClickListener true
                }
            }
            IntegerUtils.TWO -> {//只有文字
                Text_water_msg.setOnLongClickListener {
                    mContext.ShuaiToast(mContext.getRString(R.string.ClearSuccess))
                    return@setOnLongClickListener true
                }
            }
            IntegerUtils.THREE -> {//只有图片
                ImageView_water_img.setOnLongClickListener {
                    mContext.ShuaiToast(mContext.getRString(R.string.ClearSuccess))
                    return@setOnLongClickListener true
                }
            }
            else -> {
                mView
            }
        }
    }


    /**
     * 滑动事件
     */
    fun setViewOnTouchListener(ontoListener: MyOnTouchListener) {
        when (mFlag) {//垂直
            IntegerUtils.ZERO -> {
                Vertical_water_Lin.setOnTouchListener(ontoListener)
            }
            IntegerUtils.ONE -> {//水平
                Horizontal_water_Lin.setOnTouchListener(ontoListener)
            }
            IntegerUtils.TWO -> {//只有文字
                Text_water_msg.setOnTouchListener(ontoListener)
            }
            IntegerUtils.THREE -> {//只有图片
                ImageView_water_img.setOnTouchListener(ontoListener)
            }
            else -> {
                mView
            }
        }
    }


    /**
     * 判断是否正确加载布局
     */
    private fun isTrue() {
        if (mView == null) {
            return throw InflateException(context.getRString(R.string.ErrorNullLayout))
        }
    }

    /**
     * 回调
     */
    interface OnCallBack {
        fun changeImage(bitmapImageView: BitmapImageView)//修改图片
    }

    /**
     * 弹出对话框
     */
    private fun ShowDialog(oncallBack: OnCallBack, onChangeTextSize: SendCommandDialog.OnChangeTextSize) {

        val dialog = SendCommandDialog(mContext)

        val builder = getBuilder(mContext, R.string.DialogTitle, R.string.DialogMessage_text , R.mipmap.select)
        builder.setNegativeButton(mContext.getString(R.string.DialogText)) { dia, _ ->

            when (mFlag) {//垂直
                IntegerUtils.ZERO -> {
                    dialog.show()
                    dialog.setText(getText())
                    dialog.setSendListener {
                        setText(it)
                    }
                    dialog.setChangeSizeListener(onChangeTextSize)
                }
                IntegerUtils.ONE -> {//水平
                    dialog.show()
                    dialog.setText(getText())
                    dialog.setSendListener {
                        setText(it)
                    }
                    dialog.setChangeSizeListener(onChangeTextSize)
                }
                else -> {
                    mView
                }
            }
            dia.dismiss()
        }
        /**
         * 修改字体颜色
         */
        builder.setNeutralButton(mContext.getRString(R.string.UpdataColor), { _, _ ->
            ShowSelectColor(mContext, this)
        })

        builder.setPositiveButton(mContext.getString(R.string.DialogImage)) { dia, which ->
            when (mFlag) {//垂直
                IntegerUtils.ZERO -> {
                    oncallBack.changeImage(this)
                }
                IntegerUtils.ONE -> {//水平
                    oncallBack.changeImage(this)
                }
                else -> {
                    mView
                }
            }

            dia.dismiss()
        }

        when (mFlag) {//垂直
            IntegerUtils.ZERO -> {
                Vertical_water_Lin.setOnClickListener {
                    val alert = builder.create()
                    alert.show()
                }
            }
            IntegerUtils.ONE -> {//水平
                Horizontal_water_Lin.setOnClickListener {
                    val alert = builder.create()
                    alert.show()
                }
            }
            IntegerUtils.TWO -> {//只有文字
                //可以修改颜色和文字 还有文字大小
                Text_water_msg.setOnClickListener {
                    val childbuilder = getBuilder(mContext, R.string.DialogTitle, R.string.DialogMessage_color)
                    childbuilder.setNegativeButton(mContext.getString(R.string.DialogText), { _, _ ->
                        dialog.show()
                        dialog.setText(getText())
                        dialog.setSendListener {
                            setText(it)
                        }
                        dialog.setChangeSizeListener(onChangeTextSize)
                    })
                    childbuilder.setPositiveButton(context.getRString(R.string.DialogColor), { _, _ ->
                        ShowSelectColor(mContext, this)
                    })
                    val dialog = childbuilder.create()
                    dialog.setCanceledOnTouchOutside(false)
                    dialog.show()
                }
            }
            IntegerUtils.THREE -> {
                ImageView_water_img.setOnClickListener {
                    oncallBack.changeImage(this)
                }
            }
            else -> {
                mView
            }
        }
    }

    /**
     * 获取正在使用的TextVIew
     */
    fun getTextView(): TextView = when (mFlag) {
        IntegerUtils.ZERO -> Vertical_water_msg//垂直
        IntegerUtils.ONE -> Horizontal_water_msg //水平
        IntegerUtils.TWO -> Text_water_msg//只有文字
        else -> {
            throw NullPointerException(context.getRString(R.string.ErrorNullLayout))
        }
    }
}

/**
 * 需要先添加到布局中才可以设置宽高
 */
fun setParams(mView: View) {
    val params = mView.layoutParams
    params.height = LinearLayout.LayoutParams.WRAP_CONTENT
    params.width = LinearLayout.LayoutParams.WRAP_CONTENT
    mView.layoutParams = params
}