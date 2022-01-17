package com.example.nytimesmostviewedarticles.network

import com.example.nytimesmostviewedarticles.datatypes.ArticleDataForUI
import com.example.nytimesmostviewedarticles.datatypes.MediaDataForUI
import com.example.nytimesmostviewedarticles.datatypes.NetworkResponse
import kotlinx.coroutines.delay
import javax.inject.Singleton

@Singleton
class NyTimesArticleService: ArticleService {
    override suspend fun getArticleDataForUi(period: QueryPeriodLength): NetworkResponse {
        delay(5000L)
        return NetworkResponse.Success(
            dataForUi = listOf(
                ArticleDataForUI(
                    url = "",
                    publishedDate = "March, 14 2022",
                    section = "World",
                    subsection = "Space",
                    byline = "",
                    type = "",
                    title = "First Cases of COVID-19 Have Reached the Moon.",
                    abstract = "",
                    descriptionFacets = listOf("COVID-19; Omicron"),
                    media = MediaDataForUI(
                        url = "https://static01.nyt.com/images/2022/01/07/us/07virus-briefing-diabetes-misc/07virus-briefing-diabetes-misc-mediumThreeByTwo210.jpg",
                        caption = ""
                    )
                ),
                ArticleDataForUI(
                    url = "",
                    publishedDate = "March, 14 2022",
                    section = "World",
                    subsection = "Space",
                    byline = "",
                    type = "",
                    title = "First Cases of COVID-19 Have Reached the Moon.",
                    abstract = "",
                    descriptionFacets = listOf("COVID-19; Omicron"),
                    media = MediaDataForUI(
                        url = "https://static01.nyt.com/images/2022/01/07/us/07virus-briefing-diabetes-misc/07virus-briefing-diabetes-misc-mediumThreeByTwo210.jpg",
                        caption = ""
                    )
                ),
                ArticleDataForUI(
                    url = "",
                    publishedDate = "March, 14 2022",
                    section = "World",
                    subsection = "Space",
                    byline = "",
                    type = "",
                    title = "First Cases of COVID-19 Have Reached the Moon.",
                    abstract = "",
                    descriptionFacets = listOf("COVID-19; Omicron"),
                    media = MediaDataForUI(
                        url = "https://static01.nyt.com/images/2022/01/07/us/07virus-briefing-diabetes-misc/07virus-briefing-diabetes-misc-mediumThreeByTwo210.jpg",
                        caption = ""
                    )
                ),
                ArticleDataForUI(
                    url = "",
                    publishedDate = "March, 14 2022",
                    section = "World",
                    subsection = "Space",
                    byline = "",
                    type = "",
                    title = "First Cases of COVID-19 Have Reached the Moon.",
                    abstract = "",
                    descriptionFacets = listOf("COVID-19; Omicron"),
                    media = MediaDataForUI(
                        url = "https://static01.nyt.com/images/2022/01/07/us/07virus-briefing-diabetes-misc/07virus-briefing-diabetes-misc-mediumThreeByTwo210.jpg",
                        caption = ""
                    )
                ),
                ArticleDataForUI(
                    url = "",
                    publishedDate = "March, 14 2022",
                    section = "World",
                    subsection = "Space",
                    byline = "",
                    type = "",
                    title = "First Cases of COVID-19 Have Reached the Moon.",
                    abstract = "",
                    descriptionFacets = listOf("COVID-19; Omicron"),
                    media = MediaDataForUI(
                        url = "https://static01.nyt.com/images/2022/01/07/us/07virus-briefing-diabetes-misc/07virus-briefing-diabetes-misc-mediumThreeByTwo210.jpg",
                        caption = ""
                    )
                ),
                ArticleDataForUI(
                    url = "",
                    publishedDate = "March, 14 2022",
                    section = "World",
                    subsection = "Space",
                    byline = "",
                    type = "",
                    title = "First Cases of COVID-19 Have Reached the Moon.",
                    abstract = "",
                    descriptionFacets = listOf("COVID-19; Omicron"),
                    media = MediaDataForUI(
                        url = "https://static01.nyt.com/images/2022/01/07/us/07virus-briefing-diabetes-misc/07virus-briefing-diabetes-misc-mediumThreeByTwo210.jpg",
                        caption = ""
                    )
                )
            )
        )
    }

}

/**
 * The NY Times API offers three query period lengths.
 */
enum class NyTimesArticlePeriod(override val length: Int): QueryPeriodLength {
    DAY(1), WEEK(7), MONTH(30)
}
