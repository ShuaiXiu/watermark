package shuai.yxs.watermark.Activity

import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ShareCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.demo.*
import shuai.yxs.watermark.ExtensionUtils.getFileUri
import shuai.yxs.watermark.R
import java.io.File

/**
 * 测试页面
 * Created by Administrator on 2017/8/8.
 */
class demo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo)
        val i = ShareCompat.IntentBuilder.from(this)
        val file = File(Environment.getExternalStorageDirectory().toString() + "/watermark/Smallwatermark/1502444277867.jpg")
    }
}