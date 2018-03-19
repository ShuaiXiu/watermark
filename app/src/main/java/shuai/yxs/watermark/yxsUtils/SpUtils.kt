package shuai.yxs.watermark.yxsUtils

import org.jetbrains.anko.defaultSharedPreferences
import shuai.yxs.watermark.application.App

/**
 * Sp工具类
 * Created by Administrator on 2017/8/4.
 */
object SpUtils {
    private val tag = "-1"

    private val Sp by lazy {
        App.context.defaultSharedPreferences
    }
    private val Edit by lazy {
        Sp.edit()
    }

    fun putBoolean(name: String, msg: Boolean): Boolean = Edit.putBoolean(name, msg).commit()

    fun getBoolean(name: String, default: Boolean = false): Boolean = Sp.getBoolean(name, default)

    fun putString(name: String, msg: String): Boolean = Edit.putString(name, msg).commit()

    fun getString(name: String, default: String = ""): String = Sp.getString(name, default)

    fun putInt(name: String, msg: Int): Boolean = Edit.putInt(name, msg).commit()

    fun getInt(name: String, default: Int = 200): Int = Sp.getInt(name, default)

    fun putLong(name: String, msg: Long): Boolean = Edit.putLong(name, msg).commit()

    fun getLong(name: String): Long = Sp.getLong(name, tag.toLong())
}