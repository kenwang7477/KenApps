#include <cstring>
#include <jni.h>
#include <iostream>

/*
 *   See the corresponding Java source file located at:
 *   app/src/main/java/com/kenwang/kenapps/jnilib/KenNativeLib.kt
 */
extern "C" JNIEXPORT jstring JNICALL
Java_com_kenwang_kenapps_jnilib_KenNativeLib_getABIType(JNIEnv* env, jobject /* this */, jstring name) {
    #if defined(__arm__)
      #if defined(__ARM_ARCH_7A__)
        #if defined(__ARM_NEON__)
          #if defined(__ARM_PCS_VFP)
            #define ABI "armeabi-v7a/NEON (hard-float)"
          #else
            #define ABI "armeabi-v7a/NEON"
          #endif
        #else
          #if defined(__ARM_PCS_VFP)
            #define ABI "armeabi-v7a (hard-float)"
          #else
            #define ABI "armeabi-v7a"
          #endif
        #endif
      #else
       #define ABI "armeabi"
      #endif
    #elif defined(__i386__)
       #define ABI "x86"
    #elif defined(__x86_64__)
       #define ABI "x86_64"
    #elif defined(__mips64)  /* mips64el-* toolchain defines __mips__ too */
       #define ABI "mips64"
    #elif defined(__mips__)
       #define ABI "mips"
    #elif defined(__aarch64__)
       #define ABI "arm64-v8a"
    #else
       #define ABI "unknown"
    #endif

    const char * nameChs = env->GetStringUTFChars(name, 0);
    std::string fullText = "Hello ";
    fullText.append(nameChs);
    fullText.append(" from JNI ! Compiled with ABI " ABI ".");
    
    return env->NewStringUTF(fullText.c_str());
}
