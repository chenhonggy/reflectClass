package com.chen.reflect


import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Button

import java.io.IOException
import java.util.Enumeration
import java.util.Locale
import java.util.Properties
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class ScrollingActivity : AppCompatActivity() {
    private var mProperties: Properties? = null
    private var channel: String? = null
    private var subInf: ActivitySubInf? = null
    private var button: Button? = null
    private val PG_NAME = "com.chen.reflect."

    fun setSubInf(subInf: ActivitySubInf) {
        this.subInf = subInf
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        button = findViewById(R.id.button) as Button
        button!!.setOnClickListener {
            loadAssetProperties(this@ScrollingActivity, "kotlin")
            if (null != mProperties) {
                channel = mProperties!!.getProperty("channel")
                Log.e("tag", "channel:" + channel!!)
                //反射找到相应的类
                channel = channel!!.toUpperCase(Locale.ENGLISH)
                Log.e("tag", "channel:" + channel!!)
                val kotlinAcSub = classFrom(PG_NAME + channel + "ActivitySub")
                try {
                    setSubInf(kotlinAcSub!!.newInstance() as ActivitySubInf)
                } catch (e: InstantiationException) {
                    e.printStackTrace()
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }


                subInf!!.setToast(this@ScrollingActivity)
            }
        }


    }

    private fun classFrom(className: String): Class<*>? {
        try {
            return Class.forName(className)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 初始化propertes
     *
     * @param cxt
     * @param game
     */
    private fun loadAssetProperties(cxt: Context, game: String) {
        loadMetaInf(cxt, game)
        if (mProperties == null) {
            mProperties = Properties()
            try {
                mProperties!!.load(cxt.assets.open("file.properties"))
            } catch (e: IOException) {
                mProperties = null
                e.printStackTrace()
            }

        }
    }

    /**
     * 从META-INF目录下加载配置文件
     *
     * @param cxt
     * @param game
     */
    private fun loadMetaInf(cxt: Context, game: String) {
        val fileName = "META-INF/$game.properties"
        var entries: Enumeration<*>? = null
        var zipFile: ZipFile? = null
        try {
            zipFile = ZipFile(cxt.packageManager.getApplicationInfo(cxt.packageName, 0).sourceDir)
            entries = zipFile.entries()
            while (entries!!.hasMoreElements()) {
                val zipEntry = entries.nextElement() as ZipEntry
                if (zipEntry.name.startsWith(fileName)) {
                    mProperties = Properties()
                    mProperties!!.load(zipFile.getInputStream(zipEntry))
                    break
                }
            }
        } catch (e: Exception) {
            mProperties = null
            e.printStackTrace()
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

}