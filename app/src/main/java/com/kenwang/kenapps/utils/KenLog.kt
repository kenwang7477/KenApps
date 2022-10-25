package com.kenwang.kenapps.utils

import android.util.Log

object KenLog {

    fun v(message: String) {
        Log.v("KenLog", getClassLineNumber() + message)
    }

    fun v(tag: String, message: String) {
        Log.v(tag, getClassLineNumber() + message)
    }

    fun d(message: String) {
        Log.d("KenLog", getClassLineNumber() + message)
    }

    fun d(tag: String, message: String) {
        Log.d(tag, getClassLineNumber() + message)
    }

    fun i(message: String) {
        Log.i("KenLog", getClassLineNumber() + message)
    }

    fun i(tag: String, message: String) {
        Log.i(tag, getClassLineNumber() + message)
    }

    fun e(message: String) {
        Log.e("KenLog", getClassLineNumber() + message)
    }

    fun e(tag: String, message: String) {
        Log.e(tag, getClassLineNumber() + message)
    }

    private fun getClassLineNumber(): String {
        try {
            val stackTraceElements = Thread.currentThread().stackTrace
            if (stackTraceElements.isEmpty()) {
                return ""
            }
            var deep = 0
            for (i in stackTraceElements.indices) {
                val className = stackTraceElements[i].className
                if (className.indexOf("KenLog") >= 0) {
                    deep++
                }
                if (deep == 2) {
                    return ("[" + stackTraceElements[i + 1].fileName + " line:"
                            + stackTraceElements[i + 1].lineNumber + "] ")
                }
            }
            return ""
        } catch (e: Exception) {
            Log.i("KenLog", Log.getStackTraceString(e))
            return ""
        }

    }
}
