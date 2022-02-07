package com.nytimesmostviewedarticles.network

import app.cash.turbine.test
import com.nytimesmostviewedarticles.datatypes.ArticleDataResponse
import com.nytimesmostviewedarticles.datatypes.SpecificArticleResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class NyTimesRepositoryImplTest {

    private lateinit var nyTimesRepositoryImpl: NyTimesRepositoryImpl

    private lateinit var mockNyTimesArticleService: NyTimesApiService

    private val testDispatcher = StandardTestDispatcher()

    private val testCoroutineScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockNyTimesArticleService = mockk()

        nyTimesRepositoryImpl = NyTimesRepositoryImpl(
            mockNyTimesArticleService,
            testCoroutineScope
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when service throws error articleDataResponse emits error value`() {
        coEvery { mockNyTimesArticleService.getArticlesFromLastWeek(any()) }.throws(FakeNetworkResults.exception)

        runTest {
            nyTimesRepositoryImpl.articleDataResponse.test {
                assert(awaitItem() is ArticleDataResponse.Uninitialized)
                nyTimesRepositoryImpl.updateArticleData()
                assert(awaitItem() is ArticleDataResponse.Error)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when service returns success results articleDataResponse emits success value`() {
        coEvery { mockNyTimesArticleService.getArticlesFromLastWeek(any()) }.returns(
            FakeNetworkResults.success
        )

        runTest {
            nyTimesRepositoryImpl.articleDataResponse.test {
                assert(awaitItem() is ArticleDataResponse.Uninitialized)
                nyTimesRepositoryImpl.updateArticleData()
                assert(awaitItem() is ArticleDataResponse.Success)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when service throws error getSpecificArticle flow emits error value`() {
        coEvery { mockNyTimesArticleService.getArticlesFromLastWeek(any()) }.throws(FakeNetworkResults.exception)

        /**
         * Be sure the articleDataRequest is updated prior to testing the specific
         * article data. Otherwise there may be some race conditions.
         */
        runTest {
            nyTimesRepositoryImpl.articleDataResponse.test {
                assert(awaitItem() is ArticleDataResponse.Uninitialized)
                nyTimesRepositoryImpl.updateArticleData()
                assert(awaitItem() is ArticleDataResponse.Error)
                cancelAndIgnoreRemainingEvents()
            }

            nyTimesRepositoryImpl.getSpecificArticleData(FakeNetworkResults.id).test {
                assert(awaitItem() is SpecificArticleResponse.Error)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when service returns success results and getSpecificArticle is called with correct id flow emits success value`() {
        coEvery { mockNyTimesArticleService.getArticlesFromLastWeek(any()) }.returns(
            FakeNetworkResults.success
        )

        /**
         * Be sure the articleDataRequest is updated prior to testing the specific
         * article data. Otherwise there may be some race conditions.
         */
        runTest {
            nyTimesRepositoryImpl.articleDataResponse.test {
                assert(awaitItem() is ArticleDataResponse.Uninitialized)
                nyTimesRepositoryImpl.updateArticleData()
                assert(awaitItem() is ArticleDataResponse.Success)
                cancelAndIgnoreRemainingEvents()
            }

            nyTimesRepositoryImpl.getSpecificArticleData(FakeNetworkResults.id).test {
                assert(awaitItem() is SpecificArticleResponse.Success)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when service returns success results and getSpecificArticle is called with incorrect id flow emits NoMatch value`() {
        coEvery { mockNyTimesArticleService.getArticlesFromLastWeek(any()) }.returns(
            FakeNetworkResults.success
        )

        runTest {
            nyTimesRepositoryImpl.getSpecificArticleData("INCORRECT").test {
                assert(awaitItem() is SpecificArticleResponse.Uninitialized)

                nyTimesRepositoryImpl.updateArticleData()

                assert(awaitItem() is SpecificArticleResponse.NoMatch)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when articleDataResponse is uninitialized and getSpecificArticleData is called it emits Uninitialized value`() {
        runTest {
            nyTimesRepositoryImpl.getSpecificArticleData(FakeNetworkResults.id).test {
                assert(awaitItem() is SpecificArticleResponse.Uninitialized)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when articleDataResponse is uninitialized, then is updated, getSpecificArticleData emits Uninitialized and then Success value`() {

        runTest {
            nyTimesRepositoryImpl.getSpecificArticleData(FakeNetworkResults.id).test {
                assert(awaitItem() is SpecificArticleResponse.Uninitialized)

                coEvery { mockNyTimesArticleService.getArticlesFromLastWeek(any()) }.returns(
                    FakeNetworkResults.success
                )
                nyTimesRepositoryImpl.updateArticleData()

                assert(awaitItem() is SpecificArticleResponse.Success)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when updateArticle data is invoked a new request is sent to the service`() {
        coEvery { mockNyTimesArticleService.getArticlesFromLastWeek(any()) }.returns(
            FakeNetworkResults.success
        )

        runTest {
            nyTimesRepositoryImpl.articleDataResponse.test {
                assert(awaitItem() is ArticleDataResponse.Uninitialized)

                nyTimesRepositoryImpl.updateArticleData()

                assert((awaitItem() as ArticleDataResponse.Success).articleDataList[0].id == FakeNetworkResults.id)

                coEvery { mockNyTimesArticleService.getArticlesFromLastWeek(any()) }.returns(
                    FakeNetworkResults.successTwo
                )
                nyTimesRepositoryImpl.updateArticleData()

                assert((awaitItem() as ArticleDataResponse.Success).articleDataList[0].id == FakeNetworkResults.idTwo)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}