package com.kenwang.kenapps.utils

import android.util.Log

object KenLog {
    private const val TAG = "KenLog"
    private var debuggable = true

    @Volatile
    private var callerIndex = -1

    fun setDebuggable(debuggable: Boolean) {
        this.debuggable = debuggable
    }

    fun v(tag: String = TAG, message: String, throwable: Throwable? = null) {
        log(Log.VERBOSE, tag, message, throwable)
    }

    fun d(tag: String = TAG, message: String, throwable: Throwable? = null) {
        log(Log.DEBUG, tag, message, throwable)
    }

    fun i(tag: String = TAG, message: String, throwable: Throwable? = null) {
        log(Log.INFO, tag, message, throwable)
    }

    fun w(tag: String = TAG, message: String, throwable: Throwable? = null) {
        log(Log.WARN, tag, message, throwable)
    }

    fun e(tag: String = TAG, message: String, throwable: Throwable? = null) {
        log(Log.ERROR, tag, message, throwable)
    }

    private fun log(priority: Int, tag: String, message: String, throwable: Throwable?) {
        if (!debuggable) return

        val text = getClassLineNumber() + message
        when (priority) {
            Log.VERBOSE -> Log.v(tag, text, throwable)
            Log.DEBUG -> Log.d(tag, text, throwable)
            Log.INFO -> Log.i(tag, text, throwable)
            Log.WARN -> Log.w(tag, text, throwable)
            Log.ERROR -> Log.e(tag, text, throwable)
        }
    }

    private fun getClassLineNumber(): String {
        val stack = Thread.currentThread().stackTrace

        if (callerIndex < 0) {
            callerIndex = findCallerIndex(stack)
        }

        val element = stack.getOrNull(callerIndex)
        return if (element != null) {
            "[${element.fileName}:${element.lineNumber}] "
        } else {
            // 極端情況（index 失效）重新找一次
            callerIndex = findCallerIndex(stack)
            val retry = stack.getOrNull(callerIndex)
            if (retry != null) {
                "[${retry.className.substringAfterLast('.')}:${retry.lineNumber}] "
            } else {
                ""
            }
        }
    }

    private fun findCallerIndex(stack: Array<StackTraceElement>): Int {
        val logClass = KenLog::class.java.name

        for (i in stack.indices) {
            val className = stack[i].className

            if (className == Thread::class.java.name) continue
            if (className.startsWith("dalvik.system")) continue
            if (className == logClass) continue

            return i
        }
        return -1
    }
}
