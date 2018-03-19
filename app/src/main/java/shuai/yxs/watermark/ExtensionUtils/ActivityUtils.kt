package shuai.yxs.watermark.ExtensionUtils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.yuyh.library.imgsel.ImageLoader
import com.yuyh.library.imgsel.ImgSelActivity
import com.yuyh.library.imgsel.ImgSelConfig
import shuai.yxs.watermark.ConstantUtils.IntegerUtils
import shuai.yxs.watermark.ConstantUtils.StringUtils
import shuai.yxs.watermark.R
import shuai.yxs.watermark.application.App
import shuai.yxs.watermark.yxsUtils.SpUtils
import java.io.File
import java.io.FileOutputStream

/**
 *Activity的工具
 * Created by Administrator on 2017/8/4.
 */
val TAG = "TAG"

var toast: Toast? = null
/**
 * 弹吐司
 */
fun Context.ShuaiToast(msg: String) {
    if (toast == null) {
        toast = Toast.makeText(App.context, msg, Toast.LENGTH_SHORT)
    } else toast?.setText(msg)
    toast?.show()
}

/**
 * 获取String字符串
 */
fun Context.getRStringArray(id: Int = R.string.app_name): Array<String> {
    return App.context.resources.getStringArray(id)
}

/**
 * 获取字符串数组
 */
fun Context.getRString(id: Int = R.string.app_name): String {
    return App.context.getString(id)
}

/**
 * Activity跳转
 */
fun Context.startAct(clazz: Class<*>) {
    startActivity(Intent(App.context, clazz))
}

/**
 * 保存图片到本地
 */
fun Context.SaveBitmap(bitmap: Bitmap, string: String, flag: Boolean = true): File {
    val time = System.currentTimeMillis()
    val address = string + "/" + time.toString()
    val file = File(address + ".jpg")
    SpUtils.putString(StringUtils.FILEPATH, file.path)
    file.createNewFile()
    file.setLastModified(time)
    val fadeoutStream = FileOutputStream(file)
    if (!flag) {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fadeoutStream)
    } else {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fadeoutStream)
    }
    fadeoutStream.flush()
    fadeoutStream.close()
    return file
}

/**
 * 隐藏View
 */
fun Context.Gone(view: View) {
    view.visibility = View.GONE
}

/**
 * 显示View
 */
fun Context.Visible(view: View) {
    view.visibility = View.VISIBLE
}

/**
 * 获取文本框输入框的字符串
 */
fun Context.getMsgText(textView: TextView): String {
    return textView.text.toString()
}

/**
 * 图片选择器
 * 上下参数不能使用App.context
 * 报错可能是因为需要把参数返回到ActivityResult，而App.context没有
 */
fun Context.OpenSelectImage(activity: Activity, id: Int) {
    //第二个参数是把从系统相册获取的图片加载到控件里
    val config = ImgSelConfig.Builder(activity, ImageLoader { context, path, imageView -> Glide.with(context).load(path).into(imageView) })
            // 是否多选, 默认true
            .multiSelect(false)
//            .needCrop(Crop) 是否使用裁剪
            // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
            .rememberSelected(false)
            // “确定”按钮背景色
            .btnBgColor(Color.GRAY)
            // “确定”按钮文字颜色
            .btnTextColor(Color.BLUE)
            // 使用沉浸式状态栏
            .statusBarColor(Color.parseColor("#3F51B5"))
            // 返回图标ResId
            .backResId(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material)
            // 标题
            .title("图片")
            // 标题文字颜色
            .titleColor(Color.WHITE)
            // TitleBar背景色
            .titleBgColor(Color.parseColor("#3F51B5"))
            // 裁剪大小。needCrop为true的时候配置
//                .cropSize(1, 1, 200, 200)
            .needCrop(false)
            // 第一个是否显示相机，默认true
            .needCamera(true)
            // 最大选择图片数量，默认9
            .maxNum(IntegerUtils.ONE)
            .build()
    ImgSelActivity.startActivity(activity, config, id)
}

/**
 * 获取文件后缀名
 */
fun Any.getFileSuffix(file: File): String = file.name.substring(file.name.lastIndexOf(".") + 1, file.name.length)

/**
 * 判断文件是否是图片 获取文件名后缀名 避免将.nomedia加入数组
 */
fun Any.JudgeFileisImg(file: File): Boolean {
    if (getFileSuffix(file) == "jpg"
            || getFileSuffix(file) == "jpeg"
            || getFileSuffix(file) == "png"
            || getFileSuffix(file) == "JPG"
            || getFileSuffix(file) == "JPEG"
            || getFileSuffix(file) == "PNG") {
        return true
    }
    return false
}