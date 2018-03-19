package shuai.yxs.watermark.Activity

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import kotlinx.android.synthetic.main.splash_activity.*
import shuai.yxs.watermark.ConstantUtils.StringUtils
import shuai.yxs.watermark.ExtensionUtils.*
import shuai.yxs.watermark.R
import shuai.yxs.watermark.yxsUtils.SpUtils
import java.io.File
import java.util.*

/**
 * 欢迎界面
 * Created by Administrator on 2017/8/7.
 */
class SplashActivity : AppCompatActivity() {

    private val RequsetWrite = 100
    private val RequsetRead = 101

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        // 判断用户是否设置欢迎图
        if (SpUtils.getString(StringUtils.WELCOMEIMG) != "") {
            //判断是否需要拉伸图片
            if (SpUtils.getBoolean(StringUtils.IsScyleType, false)) Splash_ImageView.scaleType = ImageView.ScaleType.CENTER_CROP
            Splash_ImageView.loadImg(SpUtils.getString(StringUtils.WELCOMEIMG), Splash_ImageView)
        } else {
            //加载默认欢迎图
            Splash_ImageView.scaleType = ImageView.ScaleType.CENTER_CROP
            Splash_ImageView.loadImg(R.mipmap.wel, Splash_ImageView)
        }

        if (Build.VERSION.SDK_INT >= 23) {
            RequstPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, RequsetWrite, { init() })
        } else if (Build.VERSION.SDK_INT < 23) {
            init()
        }

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun init() {

        if (SpUtils.getBoolean(StringUtils.isFirst, true)) {
            val file = File(Environment.getExternalStorageDirectory().toString() + "/watermark/Smallwatermark")
            val list = file.listFiles()
            if (list != null) {
                list.forEach {
                    it.delete()
                }
            }
            SpUtils.putBoolean(StringUtils.isFirst, false)
        }


        //判断是否有内存卡 内存是否不足
        val sdfile = Environment.getExternalStorageDirectory()
        val sf = StatFs(sdfile.path)
        val alone = sf.blockSizeLong
        val all = sf.blockCountLong
        if (Environment.getExternalStorageState() != android.os.Environment.MEDIA_MOUNTED || (alone * all) / 1024 / 1024 < 3) {
            ShuaiToast(getRString(R.string.noSdCare))
            toActivity()
            return
        }

        //创建根目录
        val file = File(Environment.getExternalStorageDirectory().toString() + "/watermark")
        if (!file.exists())
            file.mkdir()
        //保存图片的目录
        val ImgFile = File(file, "/Image")
        if (!ImgFile.exists()) ImgFile.mkdir()
        //保存水印图片的目录
        val SmallWaterMark = File(file, "/Smallwatermark")
        if (!SmallWaterMark.exists()) SmallWaterMark.mkdir()

        //将文件目录保存至共享参数
        SpUtils.putString(StringUtils.SMALLWATERMARK, SmallWaterMark.path)
        SpUtils.putString(StringUtils.FILEADDRESS, ImgFile.path)

        //默认设置Image未加载图片
        SpUtils.putBoolean(StringUtils.IMGNOHAVEPHOTO, true)
        SpUtils.putBoolean(StringUtils.ISADDWATER, false)
        //避免在主页面用侧边栏自定义水印返回时OnResume刷新Recycler Recycler的Adapter为初始化！
        SpUtils.putBoolean(StringUtils.isMainaddWater, false)

        //判断用户是否删除了默认的水印图片  是否只剩 .nomedia  第一次安装会调用  判断水印文件夹是否为空
        if (SmallWaterMark.listFiles()?.size == 1 || SpUtils.getBoolean(StringUtils.isLoadPhoto, true) || SmallWaterMark.list().isEmpty()) {
            val noShow = File(SmallWaterMark, "/.nomedia") //创建 .nomedia文件 使此文件的文件不会被系统扫描至图库
            if (!noShow.exists()) noShow.createNewFile()
            val list: Array<String> = getRStringArray(R.array.photoItem)
            list
                    .map {
                        BitmapFactory.decodeResource(resources, resources.getIdentifier(it, "mipmap", packageName)) //获取mipmap下的图片
                    }
                    .forEach { SaveBitmap(it, SpUtils.getString(StringUtils.SMALLWATERMARK), false) }
            SpUtils.putBoolean(StringUtils.isLoadPhoto, false)
        }
        toActivity()
    }

    /**
     * 跳转至主界面
     */
    private fun toActivity() {
        val time = Timer()
        time.schedule(object : TimerTask() {
            override fun run() {
                startAct(MainActivity::class.java)
                finish()
            }

        }, 1 * 2000)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RequsetWrite -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    E("同意了1")
                    init()
                    RequstPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE, RequsetRead, {})
                } else finish()
            }
            RequsetRead -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    E("同意了2")
                    init()
                } else finish()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Splash_ImageView.setImageBitmap(null)
    }
}