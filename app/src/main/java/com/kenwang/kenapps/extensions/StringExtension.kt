package com.kenwang.kenapps.extensions

fun String.cleanLineBreak(): String {
    return this.replace("\n", "")
}