package shuai.yxs.watermark.Activity

import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.support.v4.view.GravityCompat
import android.view.KeyEvent
import kotlinx.android.synthetic.main.savasuccess_activity.*
import shuai.yxs.watermark.ConstantUtils.StringUtils
import shuai.yxs.watermark.ExtensionUtils.*
import shuai.yxs.watermark.R
import shuai.yxs.watermark.yxsUtils.SpUtils

/**
 *保存图片成功 展示界面
 * Created by Administrator on 2017/8/11.
 */
class SaveSuccessActivity : BaseActivity() {

    private var isExit = false
    private val address by lazy {
        SpUtils.getString(StringUtils.SUCCESSIMG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.savasuccess_activity)
        init()

        SaveSuccess_address.text = "保存成功: 地址$address\n快去分享吧"

        SaveSuccess_Img.loadImg(address, SaveSuccess_Img)

    }

    private fun init() {
        sDrawerLayout = SaveSuccess_Drawlayout
        sNavigation = SaveSuccess_Navigation
    }

    override fun onStart() {
        super.onStart()
        initBtn()
    }

    private fun initBtn() {
        val intent = ShareCompat.IntentBuilder.from(this)
        //分享点击事件
        SaveSuccess_Share.setOnClickListener {
            intent.setType("image/*")
//            intent.setText(getRString(R.string.ShareTitle))
            intent.setStream(getFileUri(address))
            val shareIntent = intent.intent
            if (shareIntent.resolveActivity(packageManager) != null) {
                startActivity(shareIntent)
            }
        }
        //查看大图
        SaveSuccess_Img.setOnClickListener {
            startAct(PhotoDrawActivity::class.java)
        }

        SaveSuccess_OpenMenu.setOnClickListener {
            SaveSuccess_Drawlayout.openDrawer(GravityCompat.START)
            isExit = true
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (SaveSuccess_Drawlayout.isDrawerOpen(GravityCompat.START)) {
                SaveSuccess_Drawlayout.closeDrawer(GravityCompat.START)
                return true
            } else finish()
        }
        return true
    }

}