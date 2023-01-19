package com.kenwang.kenapps.jnilib

class KenNativeLib {

    external fun getABIType(name: String): String

    companion object {

        init {
            System.loadLibrary("ken-native-lib")
        }
    }
}