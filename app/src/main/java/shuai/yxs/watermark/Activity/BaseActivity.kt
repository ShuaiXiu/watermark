package shuai.yxs.watermark.Activity

import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.updatawidhei_item.view.*
import shuai.yxs.watermark.ConstantUtils.IntegerUtils
import shuai.yxs.watermark.ConstantUtils.StringUtils
import shuai.yxs.watermark.ExtensionUtils.*
import shuai.yxs.watermark.R
import shuai.yxs.watermark.yxsUtils.SpUtils
import shuai.yxs.watermark.yxsUtils.Utils
import java.io.File

/**
 * Activity的基类
 * Created by Administrator on 2017/8/15.
 */
abstract class BaseActivity : AppCompatActivity() {
    open var sDrawerLayout: DrawerLayout? = null
    open var sNavigation: NavigationView? = null
    override fun onStart() {
        super.onStart()
        initToggle()
    }

    /**
     * 绑定侧边框
     */
    private fun initToggle() {
        val toggle = ActionBarDrawerToggle(this, sDrawerLayout, 1, 1)
        sDrawerLayout?.addDrawerListener(toggle)
        toggle.syncState()
        val params = sNavigation?.layoutParams
        params?.width = Utils.getWindowWidth(this) - Utils.getWindowWidth(this) / 4
        sNavigation?.layoutParams = params

        sNavigation?.setNavigationItemSelectedListener { item ->
            OnItemListener(item)
        }
    }

    /**
     * 抽屉点击事件
     */
    private fun OnItemListener(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mWidthHeight -> {
                E("高度")
                val builder = getBuilder(this, flag = true)
                builder.setTitle(getRString(R.string.UpdataWidghHeight))
                val view = View.inflate(this, R.layout.updatawidhei_item, null)
                view.UpdataWidHei_Width.setText(SpUtils.getInt(StringUtils.DIYWIDTH).toString())
                view.UpdataWidHei_Height.setText(SpUtils.getInt(StringUtils.DIYHEIGHT).toString())
                builder.setView(view)
                builder.setNegativeButton(getRString(R.string.Updata)) { dialog, _ ->
                    SpUtils.putInt(StringUtils.DIYWIDTH, getMsgText(view.UpdataWidHei_Width).toInt())
                    SpUtils.putInt(StringUtils.DIYHEIGHT, getMsgText(view.UpdataWidHei_Height).toInt())
                    ShuaiToast(getRString(R.string.NextUpdate))
                    dialog.dismiss()
                }
                builder.setNeutralButton(getRString(R.string.DefaultWH), { d, _ ->
                    SpUtils.putBoolean(StringUtils.UserDefaultWH, true)
                    d.dismiss()
                })
                builder.setPositiveButton(getRString(R.string.Cancle), { d, _ -> d.dismiss() })
                builder.show()
                sDrawerLayout?.closeDrawers()
            }
            R.id.addDiywater -> {
                SpUtils.putBoolean(StringUtils.isMainaddWater, true)
                startAct(DiyWaterActivity::class.java)
            }
            R.id.mClearWater -> {
                E("清理水印")
                val file = File(SpUtils.getString(StringUtils.SMALLWATERMARK))
                val list = file.listFiles()
                if (list == null) {
                    ShuaiToast(getRString(R.string.NoCache))
                    sDrawerLayout?.closeDrawers()
                    return true
                }
                list.forEach {
                    it.delete()
                }
                ShuaiToast(getRString(R.string.ClearSuccess))
                SpUtils.putBoolean(StringUtils.ISADDWATER, true)
                SpUtils.putBoolean(StringUtils.isMainaddWater, true)
                sDrawerLayout?.closeDrawers()
            }
            R.id.mClearSaveImg -> {
                E("清理图片")
                val address = SpUtils.getString(StringUtils.FILEADDRESS)
                val file = File(address)
                val list = file.listFiles()
                if (list.isEmpty()) {
                    ShuaiToast(getRString(R.string.NoCache))
                    sDrawerLayout?.closeDrawers()
                    return true
                }
                list.forEach {
                    it.delete()
                    RefreshPhotos(it)
                }
                ShuaiToast(getRString(R.string.ClearSuccess))
                UpdateGridView()
                sDrawerLayout?.closeDrawers()
            }
            R.id.mUpdataWel -> {
                E("欢迎图")
                OpenSelectImage(this, IntegerUtils.ZERO)
                sDrawerLayout?.closeDrawers()
            }
            R.id.mSetting -> {
                E("设置")
                startAct(SettingActivity::class.java)
                sDrawerLayout?.closeDrawers()
            }
        }
        item.isCheckable = false
        return true
    }

    open fun UpdateGridView() {}
}