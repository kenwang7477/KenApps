package com.kenwang.kenapps

import io.mockk.every
import io.mockk.mockk
import javax.inject.Provider

inline fun <reified T> Any.mockProvider(): Provider<T> {
    val mockProvider = mockk<Provider<T>>()
    every { mockProvider.get() } returns this as T
    return mockProvider
}
