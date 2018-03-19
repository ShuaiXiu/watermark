package shuai.yxs.watermark.ExtensionUtils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.item_colorselect_dialog.view.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import shuai.yxs.watermark.ConstantUtils.StringUtils
import shuai.yxs.watermark.R
import shuai.yxs.watermark.View.BitmapImageView
import shuai.yxs.watermark.application.App
import shuai.yxs.watermark.yxsUtils.SpUtils
import shuai.yxs.watermark.yxsUtils.Utils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 任何地方都能调用的工具类
 * Created by Administrator on 2017/8/8.
 */

private var mTrans = 0
private var mRed = 0
private var mGreen = 0
private var mBlue = 0
private var mColor = ""

/**
 * 打错误日志
 */
fun Any.E(msg: String) {
    Log.e(TAG, msg)
}

/**
 * 时间格式化
 */
fun Any.TimeFormat(long: Long): String {
    val simple = SimpleDateFormat("yy-mm-dd HH:MM:ss")
    val date = Date(long)
    App.context.E("时间:" + simple.format(date))
    return simple.format(date)
}

/**
 * 保存图片过后刷新系统图库
 */
fun Any.RefreshPhotos(file: File) {
    App.context.E("地址:" + SpUtils.getString(StringUtils.FILEPATH))
//    MediaStore.Images.Media.insertImage(App.context.contentResolver, file.absolutePath, file.name, null) 把文件插入图库
    SpUtils.putString(StringUtils.SUCCESSIMG, file.path)
    App.context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.path)))
}

/**
 * 返回一个弹出框
 * 上下文 名字 内容 icon 是否直接返回
 */
fun Any.getBuilder(context: Context, titleid: Int = R.string.app_name, msgid: Int = R.string.app_name, iconid: Int = R.mipmap.ic_launcher, flag: Boolean = false): AlertDialog.Builder {
    val builder = AlertDialog.Builder(context)

    if (flag) {
        return builder
    }
    builder.setTitle(App.context.getString(titleid))
    builder.setMessage(App.context.getString(msgid))
    builder.setIcon(iconid)
    return builder
}

/**
 * 设置弹出框的按钮
 */
fun Any.setBuildButton(builder: AlertDialog.Builder, name: Int, msg: Int, ok: () -> Unit): AlertDialog.Builder {
    builder.setNegativeButton(name, { d, _ ->
        ok.invoke()
    })
    builder.setPositiveButton(msg, { d, _ ->
        d.dismiss()
    })
    return builder
}

/**
 * 绝对路径转换成Uri
 */
fun Any.getFileUri(string: String): Uri? {
    val file = File(string)
    val address = file.absolutePath
    val cursor = App.context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media._ID), MediaStore.Images.Media.DATA + "=? ",
            arrayOf(address), null)
    if (cursor != null && cursor.moveToFirst()) {
        val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
        val uri = Uri.parse("content://media/external/images/media")
        cursor.close()
        return Uri.withAppendedPath(uri, "" + id)
    } else {
        return if (file.exists()) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, address)
            cursor.close()
            App.context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } else {
            cursor.close()
            null
        }
    }
}

/**
 * 动态申请权限
 */
fun Any.RequstPermission(activity: Activity, per: String, code: Int, a: () -> Unit) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, per)) {
        //2已经禁止提示了
        App.context.ShuaiToast(App.context.getRString(R.string.NoRemind))
    } else {
        if (ContextCompat.checkSelfPermission(activity, per) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(per), code)
        } else {
            a.invoke()
        }
    }
}

/**
 * Uri转换成Url
 */
fun Any.UriToUrl(uri: Uri): String {
    val list = arrayOf(MediaStore.MediaColumns.DATA)
    val cursor = App.context.contentResolver.query(uri, list, null, null, null)
    cursor.moveToFirst()
    val columnIndex = cursor.getColumnIndex(list[0])
    val address = cursor.getString(columnIndex)
    cursor.close()
    return address
}

/**
 * 判断图片的宽高是否大于屏幕
 * 暂未使用，因为图片普遍比屏幕大
 */
fun Any.JudgWidthHeight(string: String): Boolean {
    val width = Utils.getWindowWidth(App.context)
    val height = Utils.getWindowHight(App.context)
    val file = File(string)
    val bit = file.getBitmap(file)
    E("" + bit.width + "----" + bit.height + "---$width---$height")
    if (bit.width >= width && bit.height >= height) return true
    return false
}

/**
 *获取设置里定义的颜色
 */
fun Any.getSpColor(): Int {
    val mTrans = SpUtils.getInt(StringUtils.A, 255)
    val mRed = SpUtils.getInt(StringUtils.R, 0)
    val mGreen = SpUtils.getInt(StringUtils.G, 0)
    val mBlue = SpUtils.getInt(StringUtils.B, 0)
    return Color.argb(mTrans, mRed, mGreen, mBlue)
}

/**
 * 弹出颜色修改框
 */
fun Any.ShowSelectColor(context: Context, bitmapImageView: BitmapImageView) {
    val builder = context.getBuilder(context, flag = true)
    builder.setIcon(R.mipmap.updatacolor)
    builder.setTitle(context.getRString(R.string.UpdataColor))
    val mView = View.inflate(context, R.layout.item_colorselect_dialog, null)
    builder.setView(mView)

    RotateAni(mView.Item_Tips, "rotationY", {}, 1500)

    mTrans = SpUtils.getInt(StringUtils.A, 255)
    mRed = SpUtils.getInt(StringUtils.R, 0)
    mGreen = SpUtils.getInt(StringUtils.G, 0)
    mBlue = SpUtils.getInt(StringUtils.B, 0)

    mView.Item_Color_Test.setTextColor(Color.argb(SpUtils.getInt(StringUtils.A, 255), SpUtils.getInt(StringUtils.R, 0),
            SpUtils.getInt(StringUtils.G, 0), SpUtils.getInt(StringUtils.B, 0)))
    mView.Item_Color_LastSetting.text = "上次修改:(" + SpUtils.getInt(StringUtils.A, 255) + " " +
            SpUtils.getInt(StringUtils.R, 0) + " " + SpUtils.getInt(StringUtils.G, 0) + " " +
            SpUtils.getInt(StringUtils.B, 0) + ")"
    initItemListener(mView, context)
    builder.setNegativeButton(context.getRString(R.string.OK)) { dialog, _ ->
        SpUtils.putInt(StringUtils.A, mTrans)
        SpUtils.putInt(StringUtils.R, mRed)
        SpUtils.putInt(StringUtils.G, mGreen)
        SpUtils.putInt(StringUtils.B, mBlue)
        bitmapImageView.getTextView().setTextColor(Color.argb(mTrans, mRed, mGreen, mBlue))
        dialog.dismiss()
    }
    builder.setPositiveButton(context.getRString(R.string.Cancle), { d, _ ->
        d.dismiss()
    })
    val dialog = builder.create()
    dialog.setCanceledOnTouchOutside(false)

    dialog.show()

}

/**
 * 弹出框的点击事件注册
 */
fun initItemListener(mView: View, context: Context) {
    mView.Item_ColorSelect_Trans.max = 255
    mView.Item_ColorSelect_Trans.progress = SpUtils.getInt(StringUtils.A, 255)
    mView.Item_ColorSelect_Trans.setOnProgressChangeListener(object : DiscreteSeekBar.OnProgressChangeListener {
        override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
//                mTrans = Utils.toHexString(value)
            mTrans = value
            mColor = "#" + mTrans + mRed + mGreen + mBlue
            UpdataTextColor(mView)
        }

        override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {
        }
    })
    mView.Item_ColorSelect_Red.max = 255
    mView.Item_ColorSelect_Red.progress = SpUtils.getInt(StringUtils.R, 0)
    mView.Item_ColorSelect_Red.setOnProgressChangeListener(object : DiscreteSeekBar.OnProgressChangeListener {
        override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
//                mRed = Utils.toHexString(value)
            mRed = value
            mColor = "#" + mTrans + mRed + mGreen + mBlue
            UpdataTextColor(mView)
        }

        override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {
        }
    })
    mView.Item_ColorSelect_Green.max = 255
    mView.Item_ColorSelect_Green.progress = SpUtils.getInt(StringUtils.G, 0)
    mView.Item_ColorSelect_Green.setOnProgressChangeListener(object : DiscreteSeekBar.OnProgressChangeListener {
        override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
//                mGreen = Utils.toHexString(value)
            mGreen = value
            mColor = "#" + mTrans + mRed + mGreen + mBlue
            UpdataTextColor(mView)
        }

        override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {
        }
    })
    mView.Item_ColorSelect_Blue.max = 255
    mView.Item_ColorSelect_Blue.progress = SpUtils.getInt(StringUtils.B, 0)
    mView.Item_ColorSelect_Blue.setOnProgressChangeListener(object : DiscreteSeekBar.OnProgressChangeListener {
        override fun onProgressChanged(seekBar: DiscreteSeekBar?, value: Int, fromUser: Boolean) {
//                mBlue = Utils.toHexString(value)
            mBlue = value
            mColor = "#" + mTrans + mRed + mGreen + mBlue
            UpdataTextColor(mView)
        }

        override fun onStartTrackingTouch(seekBar: DiscreteSeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: DiscreteSeekBar?) {
        }
    })

    mView.Item_Color_red.setOnClickListener { updataParameter(255, 0, 0, mView) }
    mView.Item_Color_orange.setOnClickListener { updataParameter(255, 128, 0, mView) }
    mView.Item_Color_yellow.setOnClickListener { updataParameter(255, 255, 0, mView) }
    mView.Item_Color_green.setOnClickListener { updataParameter(0, 255, 0, mView) }
    mView.Item_Color_cyan.setOnClickListener { updataParameter(0, 255, 255, mView) }
    mView.Item_Color_blue.setOnClickListener { updataParameter(0, 0, 255, mView) }
    mView.Item_Color_purple.setOnClickListener { updataParameter(125, 0, 255, mView) }
    mView.Item_Tips.setOnClickListener {
        context.getBuilder(context, R.string.color_Tips, R.string.color_msg, R.mipmap.error).setPositiveButton(R.string.ShunDown, { d, _ -> d.dismiss() }).show()
    }
}

/**
 * 设置测试字体颜色
 */
fun UpdataTextColor(mView: View) {
    mView.Item_Color_Test.setTextColor(Color.argb(mTrans, mRed, mGreen, mBlue))
}

/**
 * 更新参数
 */
fun updataParameter(red: Int, green: Int, blue: Int, mView: View) {
    mRed = red; mGreen = green; mBlue = blue; UpdataTextColor(mView);updataProgress(mView)
}

/**
 * 更新进度条
 */
fun updataProgress(mView: View) {
    mView.Item_ColorSelect_Trans.progress = mTrans
    mView.Item_ColorSelect_Red.progress = mRed
    mView.Item_ColorSelect_Green.progress = mGreen
    mView.Item_ColorSelect_Blue.progress = mBlue
}


/**
 * 旋转动画  "rotation"
 */
fun Any.RotateAni(view: View, string: String, myfun: () -> Unit, time: Long = 800) {
    val objectAnimation = ObjectAnimator.ofFloat(view, string, 0.0F, 360.0F)
    objectAnimation.duration = time
    objectAnimation.start()
    objectAnimation.addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {}

        override fun onAnimationEnd(animation: Animator?) {
            myfun.invoke()
        }

        override fun onAnimationCancel(animation: Animator?) {}

        override fun onAnimationStart(animation: Animator?) {}

    })
}