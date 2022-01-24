package com.nytimesmostviewedarticles.network

import app.cash.turbine.test
import com.nytimesmostviewedarticles.datatypes.ArticleDataResponse
import com.nytimesmostviewedarticles.datatypes.SpecificArticleResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@DelicateCoroutinesApi
@ExperimentalCoroutinesApi
class NyTimesRepositoryImplTest {

    private lateinit var nyTimesRepositoryImpl: NyTimesRepositoryImpl

    private lateinit var mockNyTimesArticleService: NyTimesApiService
    private val testCoroutineScope = TestScope()
    private val mainThreadSurrogate = newSingleThreadContext("Global Thread")

    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        mockNyTimesArticleService = mockk()

        nyTimesRepositoryImpl = NyTimesRepositoryImpl(
            mockNyTimesArticleService,
            testCoroutineScope,
            Dispatchers.Main
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `when service throws error articleDataResponse emits error value`() {
        coEvery { mockNyTimesArticleService.getArticlesFromLastWeek(any()) }.throws(FakeNetworkResults.exception)

        runBlocking {
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

        runBlocking {
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

        nyTimesRepositoryImpl.updateArticleData()

        runBlocking {
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

        nyTimesRepositoryImpl.updateArticleData()

        runBlocking {
            nyTimesRepositoryImpl.getSpecificArticleData(FakeNetworkResults.id).test {
                val value = awaitItem()
                assert(value is SpecificArticleResponse.Success)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when service returns success results and getSpecificArticle is called with incorrect id flow emits NoMatch value`() {
        coEvery { mockNyTimesArticleService.getArticlesFromLastWeek(any()) }.returns(
            FakeNetworkResults.success
        )

        nyTimesRepositoryImpl.updateArticleData()

        runBlocking {
            nyTimesRepositoryImpl.getSpecificArticleData("INCORRECT").test {
                assert(awaitItem() is SpecificArticleResponse.NoMatch)
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    @Test
    fun `when updateArticle data is invoked a new request is sent to the service`() {
        coEvery { mockNyTimesArticleService.getArticlesFromLastWeek(any()) }.returns(
            FakeNetworkResults.success
        )

        nyTimesRepositoryImpl.updateArticleData()

        runBlocking {
            nyTimesRepositoryImpl.articleDataResponse.test {
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