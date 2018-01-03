package com.chen.reflect

import android.content.Context
import android.widget.Toast

/**
 * Created by Administrator on 2018/1/2.
 */

class KOTLINActivitySub : ActivitySubInf {

    override fun setToast(context: Context) {
        Toast.makeText(context, "反射", Toast.LENGTH_LONG).show()
    }
}
