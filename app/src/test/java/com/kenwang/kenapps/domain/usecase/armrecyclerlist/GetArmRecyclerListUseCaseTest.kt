package com.kenwang.kenapps.domain.usecase.armrecyclerlist

import com.google.common.truth.Truth
import com.kenwang.kenapps.data.model.ArmRecycler
import com.kenwang.kenapps.data.repository.armrecycler.ArmRecyclerRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetArmRecyclerListUseCaseTest {

    @MockK
    private lateinit var armRecyclerRepository: ArmRecyclerRepository

    private lateinit var getArmRecyclerListUseCase: GetArmRecyclerListUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        getArmRecyclerListUseCase = spyk(
            GetArmRecyclerListUseCase(armRecyclerRepository)
        )
    }

    @Test
    fun `Test invoke with armRecyclerRepository getArmRecyclerList() is empty then emit Result Empty`() = runTest{
        coEvery { armRecyclerRepository.getArmRecyclerList() } returns emptyList()

        val result = getArmRecyclerListUseCase.invoke().first()

        Truth
            .assertThat(result)
            .isEqualTo(GetArmRecyclerListUseCase.Result.Empty)
    }

    @Test
    fun `Test invoke with armRecyclerRepository getArmRecyclerList() is not empty then emit Result Success`() = runTest{
        val armRecyclerList = listOf(ArmRecycler())
        coEvery { armRecyclerRepository.getArmRecyclerList() } returns armRecyclerList

        val result = getArmRecyclerListUseCase.invoke().first()

        Truth
            .assertThat(result)
            .isEqualTo(GetArmRecyclerListUseCase.Result.Success(armRecyclerList))
    }
}