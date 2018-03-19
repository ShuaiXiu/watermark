package shuai.yxs.watermark.Activity

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_colorselect_dialog.*
import kotlinx.android.synthetic.main.item_colorselect_dialog.view.*
import kotlinx.android.synthetic.main.setting_activity.*
import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar
import shuai.yxs.watermark.ConstantUtils.StringUtils
import shuai.yxs.watermark.ExtensionUtils.*
import shuai.yxs.watermark.R
import shuai.yxs.watermark.yxsUtils.SpUtils
import shuai.yxs.watermark.yxsUtils.Utils

/**
 * 设置界面
 * Created by Administrator on 2017/8/16.
 */
class SettingActivity : AppCompatActivity() {
    private lateinit var mView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        setContentView(R.layout.setting_activity)
        val builder = getBuilder(this, flag = true)
        var dialog: AlertDialog
        AboutMe.setOnClickListener {
            builder.setTitle(getRString(R.string.Setting_aboutme))
            builder.setMessage(getRString(R.string.About_message))
            builder.setPositiveButton(getRString(R.string.ShunDown), { d, _ -> d.dismiss() })
            dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }
        Setting_UserMethod.setOnClickListener {
            builder.setTitle(getRString(R.string.Setting_userMethod))
            builder.setMessage(getRString(R.string.userMethod))
            builder.setPositiveButton(getRString(R.string.ShunDown), { d, _ -> d.dismiss() })
            dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }
        AboutVersion.setOnClickListener {
            builder.setTitle(getRString(R.string.Setting_aboutVersion))
            builder.setMessage(getRString(R.string.Latest_Version))
            builder.setPositiveButton(getRString(R.string.ShunDown), { d, _ -> d.dismiss() })
            dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }

        Copyright.text = "@" + getRString(R.string.Setting_copyright)
    }


}