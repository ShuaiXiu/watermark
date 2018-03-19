package shuai.yxs.watermark.ExtensionUtils

import android.widget.ImageView
import com.bumptech.glide.Glide
import shuai.yxs.watermark.application.App

/**
 * Image工具
 * Created by Administrator on 2017/8/4.
 */

/**
 * 使用Glide加载图片
 */
fun ImageView.loadImg(t: Any, img: ImageView){
    Glide.with(App.context).load(t).into(img)
}