package com.kenwang.kenapps.data.repository.chatgpt

import com.kenwang.kenapps.data.model.ChatMessage
import com.kenwang.kenapps.data.model.ChatMessageFrom
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatGPTMessageRepositoryTest {

    @MockK
    private lateinit var chatGPTMessageLocalDataSource: ChatGPTMessageLocalDataSource

    private lateinit var chatGPTMessageRepository: ChatGPTMessageRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        chatGPTMessageRepository = spyk(
            ChatGPTMessageRepository(chatGPTMessageLocalDataSource)
        )
    }

    @Test
    fun `Test getMessageList then return chatGPTMessageLocalDataSource getMessageList`() = runTest {

        chatGPTMessageRepository.getMessageList()

        coVerify { chatGPTMessageLocalDataSource.getMessageList() }
    }

    @Test
    fun `Test addMessage then chatGPTMessageLocalDataSource addMessage invoke`() = runTest {
        val chatMessage = ChatMessage(ChatMessageFrom.ChatGPT, "message", 1)

        chatGPTMessageRepository.addMessage(chatMessage)

        coVerify { chatGPTMessageLocalDataSource.addMessage(chatMessage) }
    }
}
