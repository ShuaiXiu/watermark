package shuai.yxs.watermark.application

import android.app.Application
import android.content.Context
import android.support.v4.content.ContextCompat
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * 应用界面
 * Created by Administrator on 2017/8/4.
 */
class App : Application() {
    companion object {
        lateinit var context: Context
        fun getInstence(): App {
            return App()
        }
    }

    override fun onCreate() {
        super.onCreate()
        Thread {
            Fresco.initialize(this)
        }.start()
        init()
    }

    private fun init() {
        context = applicationContext
    }


    override fun onLowMemory() {
        super.onLowMemory()
    }
}