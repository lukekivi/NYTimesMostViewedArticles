package com.nytimesmostviewedarticles.viewmodels

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.nytimesmostviewedarticles.datatypes.SpecificArticleResponse
import com.nytimesmostviewedarticles.network.NyTimesRepository
import com.nytimesmostviewedarticles.ui.screens.DetailScreenData
import com.nytimesmostviewedarticles.viewmodel.DetailScreenViewModelImpl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailScreenViewModelTest {
    private lateinit var detailScreenViewModelImpl: DetailScreenViewModelImpl
    private lateinit var fakeNyTimesRepository: NyTimesRepository
    private lateinit var fakeSavedStateHandle: SavedStateHandle

    private val articleId = "mock article id"

    @Before
    fun setup() {
        fakeNyTimesRepository = mockk()
        fakeSavedStateHandle = mockk()
    }

    @Test
    fun `when the repo returns a match the viewModel emits a success state`() {
        val fakeStateFlow = MutableStateFlow(FakeRepoResults.specificArticleSuccess)

        every { fakeSavedStateHandle.get<String>(any()) }.returns(articleId)
        every { fakeNyTimesRepository.getSpecificArticleData(articleId) }.returns(fakeStateFlow)

        detailScreenViewModelImpl = DetailScreenViewModelImpl(
            nyTimesRepository = fakeNyTimesRepository,
            savedStateHandle = fakeSavedStateHandle
        )

        runBlocking {
            detailScreenViewModelImpl.detailScreenContentFlow.test {
                assert(awaitItem().detailScreenData is DetailScreenData.Success)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when the repo returns doesn't find a match the viewModel emits a noMatch state`() {
        val fakeStateFlow = MutableStateFlow(SpecificArticleResponse.NoMatch)

        every { fakeSavedStateHandle.get<String>(any()) }.returns(articleId)
        every { fakeNyTimesRepository.getSpecificArticleData(articleId) }.returns(fakeStateFlow)

        detailScreenViewModelImpl = DetailScreenViewModelImpl(
            nyTimesRepository = fakeNyTimesRepository,
            savedStateHandle = fakeSavedStateHandle
        )

        runBlocking {
            detailScreenViewModelImpl.detailScreenContentFlow.test {
                assert(awaitItem().detailScreenData is DetailScreenData.NoMatch)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when the repo returns an error the viewModel emits an error state`() {
        val fakeStateFlow = MutableStateFlow(FakeRepoResults.specificArticleError)

        every { fakeSavedStateHandle.get<String>(any()) }.returns(articleId)
        every { fakeNyTimesRepository.getSpecificArticleData(articleId) }.returns(fakeStateFlow)

        detailScreenViewModelImpl = DetailScreenViewModelImpl(
            nyTimesRepository = fakeNyTimesRepository,
            savedStateHandle = fakeSavedStateHandle
        )

        runBlocking {
            detailScreenViewModelImpl.detailScreenContentFlow.test {
                val item = (awaitItem().detailScreenData as DetailScreenData.Error)
                assert(item.message == FakeRepoResults.errorMessage)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when the repo returns uninitialized the viewModel calls refreshAppData and is emits Success state`() {
        val fakeStateFlow =
            MutableStateFlow<SpecificArticleResponse>(SpecificArticleResponse.Uninitialized)

        every { fakeSavedStateHandle.get<String>(any()) }.returns(articleId)
        every { fakeNyTimesRepository.getSpecificArticleData(articleId) }.returns(fakeStateFlow)
        every { fakeNyTimesRepository.updateArticleData() }.coAnswers {
            fakeStateFlow.value = FakeRepoResults.specificArticleSuccess
        }

        detailScreenViewModelImpl = DetailScreenViewModelImpl(
            nyTimesRepository = fakeNyTimesRepository,
            savedStateHandle = fakeSavedStateHandle
        )

        runBlocking {
            detailScreenViewModelImpl.detailScreenContentFlow.test {
                assert(awaitItem().detailScreenData is DetailScreenData.Uninitialized)

                assert(awaitItem().detailScreenData is DetailScreenData.Success)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when the savedStateHandle fails to find a string the viewModel emits an error state`() {

        every { fakeSavedStateHandle.get<String>(any()) }.returns(null)


        runBlocking {
            detailScreenViewModelImpl = DetailScreenViewModelImpl(
                nyTimesRepository = fakeNyTimesRepository,
                savedStateHandle = fakeSavedStateHandle
            )

            val item = detailScreenViewModelImpl.detailScreenContentFlow.take(1).first()

            assert(item.detailScreenData is DetailScreenData.Error)
        }
    }
}