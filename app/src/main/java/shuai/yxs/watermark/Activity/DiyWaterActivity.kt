package shuai.yxs.watermark.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import com.yuyh.library.imgsel.ImgSelActivity
import shuai.yxs.watermark.ConstantUtils.IntegerUtils
import shuai.yxs.watermark.ConstantUtils.StringUtils
import shuai.yxs.watermark.ExtensionUtils.*
import shuai.yxs.watermark.R
import shuai.yxs.watermark.yxsUtils.SpUtils
import java.io.File

/**
 * 自定义水印
 * Created by Administrator on 2017/8/11.
 */
class DiyWaterActivity : AppCompatActivity() {
    private val mIntent by lazy {
        Intent("com.android.camera.action.CROP")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.diywater_activity)
        OpenSelectImage(this, IntegerUtils.ZERO)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //取消操作
        if (resultCode == Activity.RESULT_CANCELED) {
            this.finish()
            return
        }
        //第一次返回的选择的图片
        if (requestCode == IntegerUtils.ZERO && resultCode == Activity.RESULT_OK && data != null) {
            val list = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT)
            list.forEach {
                mIntent.setDataAndType(getFileUri(it), "image/*")
                mIntent.putExtra("crop", "true")
                if (!SpUtils.getBoolean(StringUtils.UserDefaultWH, true)) {
                }
                mIntent.putExtra("outputX", SpUtils.getInt(StringUtils.DIYWIDTH))
                mIntent.putExtra("outputY", SpUtils.getInt(StringUtils.DIYHEIGHT))
                mIntent.putExtra("return-data", true)
                startActivityForResult(mIntent, IntegerUtils.ONE) //然后将选择的图片跳转到系统的裁剪页面
            }
        } else if (requestCode == IntegerUtils.ONE && data != null) { //而后第二次返回的是裁剪好的图片
            val extras = data.extras
            E("地址：" + data.data + "   " + data.extras)
            if (extras != null) {
                val photo = extras.getParcelable<Bitmap>("data")
                val file = File(UriToUrl(data.data))
                file.delete()
                SaveBitmap(photo, SpUtils.getString(StringUtils.SMALLWATERMARK), false)
                SpUtils.putBoolean(StringUtils.ISADDWATER, true)
            }else {
                ShuaiToast("")
            }
            this.finish()
        }
    }
}