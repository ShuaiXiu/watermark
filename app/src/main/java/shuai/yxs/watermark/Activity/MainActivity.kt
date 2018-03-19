package shuai.yxs.watermark.Activity

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import com.sbingo.guide.GuideView
import com.yuyh.library.imgsel.ImgSelActivity
import kotlinx.android.synthetic.main.activity_main.*
import shuai.yxs.watermark.Adapter.RecyclerViewAdapter
import shuai.yxs.watermark.ConstantUtils.IntegerUtils
import shuai.yxs.watermark.ConstantUtils.StringUtils
import shuai.yxs.watermark.ExtensionUtils.*
import shuai.yxs.watermark.LintenerImpl.MyOnTouchListener
import shuai.yxs.watermark.R
import shuai.yxs.watermark.View.BitmapImageView
import shuai.yxs.watermark.View.CommandDialog.SendCommandDialog
import shuai.yxs.watermark.View.setParams
import shuai.yxs.watermark.yxsUtils.SpUtils
import shuai.yxs.watermark.yxsUtils.Utils
import java.io.File

/**
 * 主界面
 */
class MainActivity : BaseActivity() {
    private val ADDPHOTO = 1 //返回代码
    private var i = 0 // 用来标记添加的水印
    private var flag = false
    private var isShow = false //判断修改图片框是否显示
    private lateinit var mAdapter: RecyclerViewAdapter
    private var mMap = HashMap<Int, BitmapImageView>()

    val mHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                IntegerUtils.ONE -> mAdapter.setList(msg.obj as Array<File>)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initGuiding()
        SpUtils.putInt(StringUtils.HEIGHT, Utils.getWindowHight(this))
    }

    /**
     * 初始化抽屉
     */
    private fun init() {
        sDrawerLayout = mDrawerLayout
        sNavigation = mNavigation
    }

    /**
     * 初始化监听事件
     */
    override fun onStart() {
        super.onStart()
        initBtn()
    }

    /**
     * 初始化引导
     */
    private fun initGuiding() {
        //测试使用:
        // var builder = GuideView.Builder(this, "1.0" , true)
        val builder = GuideView.Builder(this, "1.0")
        builder.setTextSize(20f)
        builder.addHintView(Main_menu, getRString(R.string.guiding_slidingmenu), GuideView.Direction.BOTTOM, GuideView.MyShape.ELLIPSE).
                addHintView(Main_addPhoto, getRString(R.string.guiding_addphoto), GuideView.Direction.BOTTOM, GuideView.MyShape.ELLIPSE)
//                .addHintView(Main_addPhoto, getRString(R.string.guiding_frame), GuideView.Direction.RIGHT, GuideView.MyShape.ELLIPSE, -50, -100)
                .addHintView(Tv_Vertical_WaterStyle, getRString(R.string.guiding_vertical), GuideView.Direction.TOP, GuideView.MyShape.ELLIPSE)
                .addHintView(Tv_Horizontal_WaterStyle, getRString(R.string.guiding_horizontal), GuideView.Direction.TOP, GuideView.MyShape.ELLIPSE)
                .addHintView(Tv_Text_WaterStyle, getRString(R.string.guiding_text), GuideView.Direction.TOP, GuideView.MyShape.ELLIPSE)
                .addHintView(Tv_Image_WaterStyle, getRString(R.string.guiding_img), GuideView.Direction.TOP, GuideView.MyShape.ELLIPSE)
                .addHintView(Main_SaveImage, getRString(R.string.guiding_save), GuideView.Direction.BOTTOM, GuideView.MyShape.ELLIPSE)
                .show()
    }

    /**
     * 初始化按钮监听
     */
    private fun initBtn() {

        //添加要添加水印的图片
        Main_addPhoto.setOnClickListener {
            RotateAni(Main_addPhoto, "rotation", { addPhoto() })
        }
        //打开侧边栏
        Main_menu.setOnClickListener {
            mDrawerLayout.openDrawer(GravityCompat.START)
            LinAnimation(0, 5000)
            isShow = false
        }
        Main_Defaylt.setOnClickListener { addPhoto() }
        //保存图片
        Main_SaveImage.setOnClickListener {
            SavePhoto()
        }
        //添加垂直排列的水印
        Tv_Vertical_WaterStyle.setOnClickListener {
            addwatermark(IntegerUtils.ZERO)
        }
        //添加水平排列的水印
        Tv_Horizontal_WaterStyle.setOnClickListener {
            addwatermark(IntegerUtils.ONE)
        }
        //添加只有文字的水印
        Tv_Text_WaterStyle.setOnClickListener {
            addwatermark(IntegerUtils.TWO)
        }
        //添加只有图片的水印
        Tv_Image_WaterStyle.setOnClickListener {
            addwatermark(IntegerUtils.THREE)
        }
        //关闭图片选择框
        Main_Native_Exit.setOnClickListener {
            isShow = false
            LinAnimation(0, 5000)
        }
        //添加自定义水印
        Main_Native_addWater.setOnClickListener {
            startAct(DiyWaterActivity::class.java)
            SpUtils.putBoolean(StringUtils.isMainaddWater, false)
        }
        Main_Recycler.layoutManager = LinearLayoutManager(this)
    }

    private fun addPhoto() {
        if (mMap.size > 0) {
            val builder = getBuilder(this, R.string.updataPhoto, R.string.updataMsg, R.mipmap.error)
            builder.setNegativeButton(getRString(R.string.Crueltomodify)) { dialog, _ ->
                OpenSelectImage(this, ADDPHOTO)
                clearMap()
                dialog?.dismiss()
            }
            builder.setPositiveButton(getRString(R.string.updataCancle)) { dialog, _ -> dialog?.dismiss() }
            val dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
            return
        }
        Gone(Main_Defaylt)
        //打开图片选择器
        OpenSelectImage(this, ADDPHOTO)
        SpUtils.putBoolean(StringUtils.IMGNOHAVEPHOTO, false)
    }

    /**
     * 保存图片
     */
    fun SavePhoto() {
        if (SpUtils.getBoolean(StringUtils.IMGNOHAVEPHOTO)) {
            ShuaiToast(getRString(R.string.SaveNull))
            return
        }
        val bit = Main_FrameLayout.getImage(Main_FrameLayout)
        val cacheBit = Bitmap.createBitmap(bit)
        clearMap()
        Main_ImageView.setImageBitmap(cacheBit)
        val file = SaveBitmap(cacheBit, SpUtils.getString(StringUtils.FILEADDRESS))
        mMap.clear()
        Main_FrameLayout.destroyDrawingCache() //销毁的时候必须确保缓存没有在使用
        flag = false
        RefreshPhotos(file) //刷新系统想相册

        Visible(Main_Defaylt)
        Main_ImageView.setImageDrawable(null) //使用setImageBitmap 会导致下次无法设置图片
        SpUtils.putBoolean(StringUtils.IMGNOHAVEPHOTO, true)
        startAct(SaveSuccessActivity::class.java)
    }

    /**
     * 添加水印图片的具体操作
     */
    private fun addwatermark(i: Int) {
        if (!flag) {
            ShuaiToast(getRString(R.string.ShouldAddImg))
            return
        }
        val bitImage = BitmapImageView(this, i)
        Main_FrameLayout.addView(bitImage)
        setParams(bitImage)
        bitImage.setViewOnTouchListener(MyOnTouchListener(bitImage, Main_FrameLayout, bitImage)) //设置滚动事件
        bitImage.setViewOnClickListener(object : BitmapImageView.OnCallBack { //设置点击事件
            /**
             * 修改图片
             */
            override fun changeImage(bitmapImageView: BitmapImageView) {
                if (isShow) return
                Main_Lin_Gone.visibility = View.VISIBLE
                LinAnimation(5000, 0)
                isShow = true

                mAdapter = RecyclerViewAdapter(this@MainActivity, {
                    val file = File(SpUtils.getString(StringUtils.SMALLWATERMARK))
                    var Size = 0
                    Size = if (file.listFiles().isEmpty()) {  //如果在本界面清空了水印图片会导致下面创建数组的长度为-1 引发NegativeArraySizeException异常
                        1
                    } else file.listFiles().size
                    //其实可以使用try包裹，但是不知道怎么定义空的Array<File>() 咱使用笨方法
                    var list = Array(Size - 1, { file }) //因为不能创建空的list  所以以这种方式创建一个不为空但是限制长度的数组

                    var i = 0
                    Thread({
                        file.listFiles().forEach {
                            if (JudgeFileisImg(it)) {
                                list[i] = it
                                i += 1
                            }
                        }
                        mHandler.obtainMessage(IntegerUtils.ONE, list).sendToTarget()
                    }).start()

                    return@RecyclerViewAdapter list

                }, bitmapImageView)

                Main_Recycler.adapter = mAdapter
            }
        },
                /**
                 * 修改文字大小
                 */
                object : SendCommandDialog.OnChangeTextSize {
                    override fun Top() { // 增大
                        // 单位 大小
                        bitImage.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, bitImage.getTextView().textSize + 2)
                    }

                    override fun Bottom() { // 减小
                        bitImage.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_PX, bitImage.getTextView().textSize - 2)
                    }
                })
        mMap.put(i, bitImage)
    }

    /**
     * 自定义水印后 返回到主页面时刷新RecyclerView的数据
     */
    override fun onResume() {
        super.onResume()
        if (SpUtils.getBoolean(StringUtils.ISADDWATER, false)) {
            if (SpUtils.getBoolean(StringUtils.isMainaddWater, false)) return
            mAdapter.onRefresh()
            SpUtils.putBoolean(StringUtils.ISADDWATER, false)
        }
    }

    override fun UpdateGridView() = onResume()


    /**
     * 使用ImgSelActivity加载图片不能获取data.data的Uri
     * 获取的是图片的物理地址
     * 需要设用特定的方法 data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT)
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADDPHOTO && resultCode == Activity.RESULT_OK && data != null) {
            val list = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT)
            list.forEach {
                Main_ImageView.loadImg(it, Main_ImageView)
                flag = true
            }
        } else if (requestCode == IntegerUtils.ZERO && resultCode == Activity.RESULT_OK && data != null) {
            val list = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT)
            list.forEach {
                SpUtils.putString(StringUtils.WELCOMEIMG, it)

//                if(JudgWidthHeight(it)) return

                val builder = getBuilder(this, R.string.DialogTitle, R.string.ScyleType)
                builder.setNegativeButton(getRString(R.string.OK), { d, _ ->
                    SpUtils.putBoolean(StringUtils.IsScyleType, true)
                    ShuaiToast(getRString(R.string.NextTakeeffect))
                    d.dismiss()
                })
                builder.setPositiveButton(getRString(R.string.Cancle), { d, _ ->
                    SpUtils.putBoolean(StringUtils.IsScyleType, false)
                    ShuaiToast(getRString(R.string.NextTakeeffect))
                    d.dismiss()
                })
                val dialog = builder.create()
                dialog.setCanceledOnTouchOutside(false)
                dialog.show()
            }
        } else if (requestCode == ADDPHOTO && resultCode == Activity.RESULT_CANCELED) {
            Visible(Main_Defaylt)
        }
    }

    /**
     * 清理Bitmap缓存
     */
    private fun clearMap() {
        if (mMap.size == 0) return
        for (item in mMap.keys) {
            Main_FrameLayout.removeView(mMap[item])
            val cacheView = mMap[item] as BitmapImageView
            cacheView.DestroyImg()
        }
        i = 0
    }

    /**
     * 修改水印 弹出框动画
     */
    private fun LinAnimation(top: Int, bottom: Int) {
        val objectAnimator = ObjectAnimator.ofFloat(Main_Lin_Gone, "translationY", top.toFloat(), bottom.toFloat())
        objectAnimator.duration = 500
        objectAnimator.start()
    }

    /**
     * 退出程序时清理缓存
     */
    override fun onDestroy() {
        super.onDestroy()
        clearMap()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START)
                return true
            } else {
                if (mMap.size > 0) {
                    setBuildButton(getBuilder(this, R.string.isExit, R.string.isHavePhoto, R.mipmap.cray, false), R.string.Exit, R.string.Cancle, {
                        finish()
                    }).create().show()
                } else finish()
            }
        }
        return true
    }
}

