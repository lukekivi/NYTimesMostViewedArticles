package com.nytimesmostviewedarticles.viewmodels

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.nytimesmostviewedarticles.datatypes.SpecificArticleResponse
import com.nytimesmostviewedarticles.network.NetworkConnectionService
import com.nytimesmostviewedarticles.network.NetworkStatus
import com.nytimesmostviewedarticles.network.NyTimesRepository
import com.nytimesmostviewedarticles.ui.screens.DetailScreenData
import com.nytimesmostviewedarticles.viewmodel.DetailScreenViewModelImpl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailScreenViewModelTest {
    private lateinit var detailScreenViewModelImpl: DetailScreenViewModelImpl
    private lateinit var fakeNyTimesRepository: NyTimesRepository
    private lateinit var fakeSavedStateHandle: SavedStateHandle
    private lateinit var fakeNetworkConnectionService: NetworkConnectionService

    private val articleId = "mock article id"

    private val fakeNetworkStatusFlow = MutableStateFlow(NetworkStatus.CONNECTED)
    private val fakeSpecificArticleResponseFlow = MutableStateFlow<SpecificArticleResponse>(FakeRepoResults.specificArticleSuccess)

    @Before
    fun setup() {
        fakeNyTimesRepository = mockk()
        fakeSavedStateHandle = mockk()
        fakeNetworkConnectionService = mockk()

        every { fakeNetworkConnectionService.networkStatusFlow }.returns(fakeNetworkStatusFlow)
        every { fakeNyTimesRepository.getSpecificArticleData(articleId) }.returns(fakeSpecificArticleResponseFlow)
        every { fakeSavedStateHandle.get<String>(any()) }.returns(articleId)
    }

    @Test
    fun `when the repo returns a match the viewModel emits a success state`() {
        detailScreenViewModelImpl = DetailScreenViewModelImpl(
            nyTimesRepository = fakeNyTimesRepository,
            savedStateHandle = fakeSavedStateHandle,
            networkConnectionService = fakeNetworkConnectionService
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
        fakeSpecificArticleResponseFlow.value = SpecificArticleResponse.NoMatch

        detailScreenViewModelImpl = DetailScreenViewModelImpl(
            nyTimesRepository = fakeNyTimesRepository,
            savedStateHandle = fakeSavedStateHandle,
            networkConnectionService = fakeNetworkConnectionService
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
        fakeSpecificArticleResponseFlow.value = FakeRepoResults.specificArticleError

        detailScreenViewModelImpl = DetailScreenViewModelImpl(
            nyTimesRepository = fakeNyTimesRepository,
            savedStateHandle = fakeSavedStateHandle,
            networkConnectionService = fakeNetworkConnectionService
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
        fakeSpecificArticleResponseFlow.value = SpecificArticleResponse.Uninitialized

        every { fakeNyTimesRepository.updateArticleData() }.coAnswers {
            fakeSpecificArticleResponseFlow.value = FakeRepoResults.specificArticleSuccess
        }

        detailScreenViewModelImpl = DetailScreenViewModelImpl(
            nyTimesRepository = fakeNyTimesRepository,
            savedStateHandle = fakeSavedStateHandle,
            networkConnectionService = fakeNetworkConnectionService
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
                savedStateHandle = fakeSavedStateHandle,
                networkConnectionService = fakeNetworkConnectionService
            )

            val item = detailScreenViewModelImpl.detailScreenContentFlow.take(1).first()

            assert(item.detailScreenData is DetailScreenData.Error)
        }
    }

    @Test
    fun `when network status is Disconnected then a Disconnected value is emitted`() {
        fakeSpecificArticleResponseFlow.value = FakeRepoResults.specificArticleSuccess
        fakeNetworkStatusFlow.value = NetworkStatus.DISCONNECTED

        detailScreenViewModelImpl = DetailScreenViewModelImpl(
            nyTimesRepository = fakeNyTimesRepository,
            savedStateHandle = fakeSavedStateHandle,
            networkConnectionService = fakeNetworkConnectionService
        )

        runBlocking {
            detailScreenViewModelImpl.detailScreenContentFlow.test {
                assert(awaitItem().networkStatus == NetworkStatus.DISCONNECTED)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when network status is Connected then a Connected value is emitted`() {
        fakeSpecificArticleResponseFlow.value = FakeRepoResults.specificArticleSuccess

        detailScreenViewModelImpl = DetailScreenViewModelImpl(
            nyTimesRepository = fakeNyTimesRepository,
            savedStateHandle = fakeSavedStateHandle,
            networkConnectionService = fakeNetworkConnectionService
        )

        runBlocking {
            detailScreenViewModelImpl.detailScreenContentFlow.test {
                assert(awaitItem().networkStatus == NetworkStatus.CONNECTED)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when network status is Uninitialized then an Uninitialized value is emitted`() {
        fakeSpecificArticleResponseFlow.value = FakeRepoResults.specificArticleSuccess
        fakeNetworkStatusFlow.value = NetworkStatus.UNINITIALIZED

        detailScreenViewModelImpl = DetailScreenViewModelImpl(
            nyTimesRepository = fakeNyTimesRepository,
            savedStateHandle = fakeSavedStateHandle,
            networkConnectionService = fakeNetworkConnectionService
        )

        runBlocking {
            detailScreenViewModelImpl.detailScreenContentFlow.test {
                assert(awaitItem().networkStatus == NetworkStatus.UNINITIALIZED)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when networkStatus is not Connected then when refreshAppData is invoked article data is not updated`() {
        fakeSpecificArticleResponseFlow.value = FakeRepoResults.specificArticleSuccess
        fakeNetworkStatusFlow.value = NetworkStatus.DISCONNECTED

        every { fakeNyTimesRepository.updateArticleData() }.answers {
            fakeSpecificArticleResponseFlow.value = FakeRepoResults.specificArticleError
        }

        detailScreenViewModelImpl = DetailScreenViewModelImpl(
            nyTimesRepository = fakeNyTimesRepository,
            savedStateHandle = fakeSavedStateHandle,
            networkConnectionService = fakeNetworkConnectionService
        )

        runBlocking {
            detailScreenViewModelImpl.detailScreenContentFlow.test {
                val itemOne = awaitItem()
                assert(itemOne.detailScreenData is DetailScreenData.Success && itemOne.networkStatus == NetworkStatus.DISCONNECTED)

                detailScreenViewModelImpl.refreshAppData()

                /**
                 * emit a value in order to check if mainScreenData changed
                 */
                fakeNetworkStatusFlow.value = NetworkStatus.CONNECTED

                val itemTwo = awaitItem()
                assert(itemTwo.detailScreenData is DetailScreenData.Success && itemTwo.networkStatus == NetworkStatus.CONNECTED)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}