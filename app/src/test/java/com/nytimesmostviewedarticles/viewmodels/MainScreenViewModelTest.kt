package com.nytimesmostviewedarticles.viewmodels

import app.cash.turbine.test
import com.nytimesmostviewedarticles.datatypes.ArticleDataResponse
import com.nytimesmostviewedarticles.network.FakeNetworkResults
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
    private lateinit var fakeNyTimesRepository: NyTimesRepository

    @Before
    fun setup() {
        fakeNyTimesRepository = mockk()
    }

    @Test
    fun `when the viewModel receives uninitialized data it requests an initialization and receives a success response`() {
        val fakeStateFlow = MutableStateFlow<ArticleDataResponse>(ArticleDataResponse.Uninitialized)

        every { fakeNyTimesRepository.articleDataResponse }.returns(fakeStateFlow)
        every { fakeNyTimesRepository.updateArticleData() }.answers {
            fakeStateFlow.value = FakeRepoResults.articleDataSuccess
        }

        runBlocking {
            mainScreenViewModel = MainScreenViewModelImpl(fakeNyTimesRepository)

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
        val fakeStateFlow = MutableStateFlow<ArticleDataResponse>(FakeRepoResults.articleDataError)

        every { fakeNyTimesRepository.articleDataResponse }.returns(fakeStateFlow)

        runBlocking {
            mainScreenViewModel = MainScreenViewModelImpl(fakeNyTimesRepository)

            mainScreenViewModel.mainScreenContent.test {
                val firstItem = (awaitItem().mainScreenData as MainScreenData.Error)
                assert(firstItem.message == FakeRepoResults.errorMessage)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when user refreshes article viewModel asks for an update then it is updated`() {
        val fakeStateFlow =
            MutableStateFlow<ArticleDataResponse>(FakeRepoResults.articleDataSuccess)

        every { fakeNyTimesRepository.articleDataResponse }.returns(fakeStateFlow)
        every { fakeNyTimesRepository.updateArticleData() }.answers {
            fakeStateFlow.value = FakeRepoResults.articleDataSuccessTwo
        }

        mainScreenViewModel = MainScreenViewModelImpl(fakeNyTimesRepository)

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
        val fakeStateFlow =
            MutableStateFlow<ArticleDataResponse>(FakeRepoResults.articleDataSuccessMultiple)

        every { fakeNyTimesRepository.articleDataResponse }.returns(fakeStateFlow)

        runBlocking {
            mainScreenViewModel = MainScreenViewModelImpl(fakeNyTimesRepository)

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
}