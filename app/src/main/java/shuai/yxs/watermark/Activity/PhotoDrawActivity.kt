package shuai.yxs.watermark.Activity

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.imagepipeline.image.ImageInfo
import kotlinx.android.synthetic.main.photodraw_activity.*
import shuai.yxs.watermark.ConstantUtils.StringUtils
import shuai.yxs.watermark.ExtensionUtils.getFileUri
import shuai.yxs.watermark.R
import shuai.yxs.watermark.application.App
import shuai.yxs.watermark.yxsUtils.SpUtils

/**
 * 图片详情
 * Created by Administrator on 2017/8/12.
 */
class PhotoDrawActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photodraw_activity)
        val address = SpUtils.getString(StringUtils.SUCCESSIMG) //获取保存的图片的地址

        //TODO 此页面可能不会退出 因为在查看大图过后点击返回 到保存成功页面 再次点击返回 会回到此页面(bug)
        //初始化查看大图控件
        val builder = GenericDraweeHierarchyBuilder(App.context.resources)
        val hierarchy = builder
                .setPlaceholderImage(R.mipmap.zhanwei)
                .setFailureImage(R.mipmap.loser)
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .build()
        PhotoDraweeView.hierarchy = hierarchy

        val controller = Fresco.newDraweeControllerBuilder()
        controller.setUri(getFileUri(address))
        controller.controllerListener = object : BaseControllerListener<ImageInfo>() {
            override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?, animatable: Animatable?) {
                super.onFinalImageSet(id, imageInfo, animatable)
                if (imageInfo == null || PhotoDraweeView == null) {
                    return
                }
                PhotoDraweeView.update(imageInfo.width, imageInfo.height)
            }
        }
        controller.oldController = PhotoDraweeView.controller
        PhotoDraweeView.controller = controller.build()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}