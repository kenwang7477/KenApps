package com.kenwang.kenapps.data.repository.chatgpt

import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatGPTRepositoryTest {

    @MockK
    private lateinit var chatGPTServerDataSource: ChatGPTServerDataSource

    private lateinit var chatGPTRepository: ChatGPTRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        chatGPTRepository = spyk(
            ChatGPTRepository(chatGPTServerDataSource)
        )
    }

    @Test
    fun `Test getResult then chatGPTServerDataSource getResult invoke`() = runTest {
        val keyword = "keyword"

        chatGPTRepository.getResult(keyword)

        coVerify { chatGPTServerDataSource.getResult(keyword) }
    }
}
