package com.nytimesmostviewedarticles.viewmodels

import app.cash.turbine.test
import com.nytimesmostviewedarticles.datatypes.ArticleDataResponse
import com.nytimesmostviewedarticles.network.FakeNetworkResults
import com.nytimesmostviewedarticles.network.NetworkConnectionService
import com.nytimesmostviewedarticles.network.NetworkStatus
import com.nytimesmostviewedarticles.network.NyTimesRepository
import com.nytimesmostviewedarticles.ui.screens.MainScreenData
import com.nytimesmostviewedarticles.viewmodel.MainScreenViewModelImpl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class MainScreenViewModelTest {
    private lateinit var mainScreenViewModel: MainScreenViewModelImpl
    private lateinit var mockNyTimesRepository: NyTimesRepository
    private lateinit var mockNetworkConnectionService: NetworkConnectionService

    private val fakeNetworkStatusFlow = MutableStateFlow(NetworkStatus.CONNECTED)
    private val fakeArticleResponseFlow = MutableStateFlow<ArticleDataResponse>(ArticleDataResponse.Uninitialized)

    @Before
    fun setup() {
        mockNyTimesRepository = mockk()
        mockNetworkConnectionService = mockk()

        every { mockNetworkConnectionService.networkStatusFlow }.returns(fakeNetworkStatusFlow)
        every { mockNyTimesRepository.articleDataResponse }.returns(fakeArticleResponseFlow)

    }

    @Test
    fun `when the viewModel receives uninitialized data it requests an initialization and receives a success response`() {
        every { mockNyTimesRepository.updateArticleData() }.answers {
            fakeArticleResponseFlow.value = FakeRepoResults.articleDataSuccess
        }

        runBlocking {
            mainScreenViewModel = MainScreenViewModelImpl(mockNyTimesRepository, mockNetworkConnectionService)

            mainScreenViewModel.mainScreenContent.test {
                val firstItem = awaitItem()
                assert(firstItem.mainScreenData is MainScreenData.Uninitialized)
                val secondItem = awaitItem()
                assert(secondItem.mainScreenData is MainScreenData.Success)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when the viewModel receives an error response from the repo it emits an error value`() {
        fakeArticleResponseFlow.value = FakeRepoResults.articleDataError
        mainScreenViewModel = MainScreenViewModelImpl(mockNyTimesRepository, mockNetworkConnectionService)

        runBlocking {
            mainScreenViewModel.mainScreenContent.test {
                val firstItem = (awaitItem().mainScreenData as MainScreenData.Error)
                assert(firstItem.message == FakeRepoResults.errorMessage)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when user refreshes article viewModel asks for an update then it is updated`() {
        fakeArticleResponseFlow.value = FakeRepoResults.articleDataSuccess

        every { mockNyTimesRepository.updateArticleData() }.answers {
            fakeArticleResponseFlow.value = FakeRepoResults.articleDataSuccessTwo
        }

        mainScreenViewModel = MainScreenViewModelImpl(mockNyTimesRepository, mockNetworkConnectionService)

        runBlocking {
            mainScreenViewModel.mainScreenContent.test {

                val firstItem = awaitItem()
                val firstItemData = (firstItem.mainScreenData as MainScreenData.Success)
                assert(!firstItem.isLoading)
                assert(firstItemData.articleRowDataList[0].id == FakeNetworkResults.id)
                mainScreenViewModel.userRefreshArticles()

                val secondItem = awaitItem()
                val secondItemData = (secondItem.mainScreenData as MainScreenData.Success)
                assert(secondItem.isLoading)
                assert(secondItemData.articleRowDataList[0].id == FakeNetworkResults.id)

                val thirdItem = awaitItem()
                val thirdItemData = (thirdItem.mainScreenData as MainScreenData.Success)
                assert(!thirdItem.isLoading)
                assert(thirdItemData.articleRowDataList[0].id == FakeNetworkResults.idTwo)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when a new filter is applied it updates the mainScreenData list of articles`() {
        fakeArticleResponseFlow.value = FakeRepoResults.articleDataSuccessMultiple
        mainScreenViewModel = MainScreenViewModelImpl(mockNyTimesRepository, mockNetworkConnectionService)

        runBlocking {
            mainScreenViewModel.mainScreenContent.test {

                val firstItemData = (awaitItem().mainScreenData as MainScreenData.Success)
                assert(
                    !firstItemData.articleRowDataList.all {
                        it.section == FakeRepoResults.filterOption.apiFilterName
                    }
                )

                mainScreenViewModel.userChangedFilter(filterOption = FakeRepoResults.filterOption)

                val secondItemData = (awaitItem().mainScreenData as MainScreenData.Success)
                assert(
                    secondItemData.articleRowDataList.all {
                        it.section == FakeRepoResults.filterOption.apiFilterName
                    }
                )

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when network status is Disconnected then a Disconnected value is emitted`() {
        fakeArticleResponseFlow.value = FakeRepoResults.articleDataSuccess
        fakeNetworkStatusFlow.value = NetworkStatus.DISCONNECTED

        mainScreenViewModel = MainScreenViewModelImpl(mockNyTimesRepository, mockNetworkConnectionService)

        runBlocking {
            mainScreenViewModel.mainScreenContent.test {
                assert(awaitItem().networkStatus == NetworkStatus.DISCONNECTED)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when network status is Connected then a Connected value is emitted`() {
        fakeArticleResponseFlow.value = FakeRepoResults.articleDataSuccess

        mainScreenViewModel = MainScreenViewModelImpl(mockNyTimesRepository, mockNetworkConnectionService)

        runBlocking {
            mainScreenViewModel.mainScreenContent.test {
                assert(awaitItem().networkStatus == NetworkStatus.CONNECTED)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when network status is Uninitialized then an Uninitialized value is emitted`() {
        fakeArticleResponseFlow.value = FakeRepoResults.articleDataSuccess
        fakeNetworkStatusFlow.value = NetworkStatus.UNINITIALIZED

        mainScreenViewModel = MainScreenViewModelImpl(mockNyTimesRepository, mockNetworkConnectionService)

        runBlocking {
            mainScreenViewModel.mainScreenContent.test {
                assert(awaitItem().networkStatus == NetworkStatus.UNINITIALIZED)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when networkStatus is not Connected then when userRefreshArticles is invoked article data is not updated`() {
        fakeArticleResponseFlow.value = FakeRepoResults.articleDataSuccess
        fakeNetworkStatusFlow.value = NetworkStatus.DISCONNECTED

        every { mockNyTimesRepository.updateArticleData() }.answers {
            fakeArticleResponseFlow.value = FakeRepoResults.articleDataError
        }

        mainScreenViewModel = MainScreenViewModelImpl(mockNyTimesRepository, mockNetworkConnectionService)

        runBlocking {
            mainScreenViewModel.mainScreenContent.test {
                val itemOne = awaitItem()
                assert(itemOne.mainScreenData is MainScreenData.Success && itemOne.networkStatus == NetworkStatus.DISCONNECTED)

                mainScreenViewModel.userRefreshArticles()

                /**
                 * emit a value in order to check if mainScreenData changed
                 */
                fakeNetworkStatusFlow.value = NetworkStatus.CONNECTED

                val itemTwo = awaitItem()
                assert(itemTwo.mainScreenData is MainScreenData.Success && itemTwo.networkStatus == NetworkStatus.CONNECTED)

                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}