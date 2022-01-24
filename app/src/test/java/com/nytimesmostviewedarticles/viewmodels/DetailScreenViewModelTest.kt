package com.nytimesmostviewedarticles.viewmodels

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.nytimesmostviewedarticles.datatypes.SpecificArticleResponse
import com.nytimesmostviewedarticles.network.NyTimesRepository
import com.nytimesmostviewedarticles.ui.screens.DetailScreenData
import com.nytimesmostviewedarticles.viewmodel.DetailScreenViewModelImpl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DetailScreenViewModelTest {
    private lateinit var detailScreenViewModelImpl: DetailScreenViewModelImpl
    private lateinit var fakeNyTimesRepository: NyTimesRepository
    private lateinit var fakeSavedStateHandle: SavedStateHandle

    private val articleId = ""

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
            detailScreenViewModelImpl.getArticleDetail.test {
                assert(awaitItem() is DetailScreenData.Success)
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
            detailScreenViewModelImpl.getArticleDetail.test {
                assert(awaitItem() is DetailScreenData.NoMatch)
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
            detailScreenViewModelImpl.getArticleDetail.test {
                val item = (awaitItem() as DetailScreenData.Error)
                assert(item.message == FakeRepoResults.errorMessage)

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

            val item = detailScreenViewModelImpl.getArticleDetail.take(1).first()

            assert(item is DetailScreenData.Error)
        }
    }
}