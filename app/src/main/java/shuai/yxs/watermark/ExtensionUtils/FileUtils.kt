package shuai.yxs.watermark.ExtensionUtils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File

/**
 * 文件工具
 * Created by Administrator on 2017/8/11.
 */
/**
 * 将文件转换成Bitmap
 */
fun File.getBitmap(file: File): Bitmap{
    return BitmapFactory.decodeFile(file.path)
}