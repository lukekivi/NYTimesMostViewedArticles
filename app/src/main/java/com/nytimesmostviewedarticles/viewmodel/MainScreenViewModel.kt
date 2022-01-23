package com.nytimesmostviewedarticles.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.nytimesmostviewedarticles.R
import com.nytimesmostviewedarticles.datatypes.ArticleData
import com.nytimesmostviewedarticles.datatypes.ArticleDataResponse
import com.nytimesmostviewedarticles.network.NyTimesRepository
import com.nytimesmostviewedarticles.ui.components.ArticleCardData
import com.nytimesmostviewedarticles.ui.components.FilterItem
import com.nytimesmostviewedarticles.ui.screens.MainScreenContent
import com.nytimesmostviewedarticles.ui.screens.MainScreenData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

interface MainScreenViewModel {
    val mainScreenContent: Flow<MainScreenContent>

    /**
     * Updates [mainScreenContent] with up to date article data.
     */
    fun userRefreshArticles()

    /**
     * Update filter which is applied to [mainScreenContent] article data.
     */
    fun userChangedFilter(filterOption: FilterOptions)
}

@HiltViewModel
class MainScreenViewModelImpl @Inject constructor(
    private val nyTimesRepository: NyTimesRepository
) : ViewModel(), MainScreenViewModel {

    private val filterItemList = FilterOptions.values().map { FilterItem(it) }.toMutableList()

    private var filter = MutableStateFlow<ArticleFilter>(ArticleFilter.None)

    override val mainScreenContent: Flow<MainScreenContent> = nyTimesRepository.articleDataResponse
        .combine(filter) { articleResponse, _ ->
            val mainScreenData = when (articleResponse) {
                is ArticleDataResponse.Loading -> MainScreenData.Loading
                is ArticleDataResponse.Error -> MainScreenData.Error(articleResponse.message)
                is ArticleDataResponse.Uninitialized -> {
                    userRefreshArticles()
                    MainScreenData.Loading
                }
                is ArticleDataResponse.Success -> {
                    val filteredData = applyFilter(articleResponse.articleDataList.map { it.toArticleCardData() })

                    if (filteredData.isEmpty()) {
                        MainScreenData.Empty
                    } else {
                        MainScreenData.Success(filteredData)
                    }
                }
            }
            MainScreenContent(filterItemList, mainScreenData)
        }

    override fun userChangedFilter(filterOption: FilterOptions) {
        filter.value.let { curArticleFilter ->

            /**
             * Toggle the new filter in the list
             */
            updateFilterListItem(filterOption.ordinal)

            when (curArticleFilter) {
                is ArticleFilter.None -> filter.value = ArticleFilter.Active(filterOption)
                is ArticleFilter.Active -> {

                    /**
                     * If current active filter isn't the same as [filterOption] deselect it.
                     */
                    curArticleFilter.filterOption.let {
                        if (it != filterOption) updateFilterListItem(it.ordinal)
                    }

                    // update filter state
                    if (curArticleFilter.filterOption == filterOption) {
                        filter.value = ArticleFilter.None
                    } else {
                        filter.value = ArticleFilter.Active(filterOption)
                    }
                }
            }
        }
    }

    private fun updateFilterListItem(filterIndex: Int) {
        val filterItem = filterItemList[filterIndex]

        filterItemList[filterIndex] = FilterItem(
            filter = filterItem.filter,
            isSelected = !filterItem.isSelected
        )
    }

    private fun applyFilter(articleCardDataList: List<ArticleCardData>) =
        filter.value.let { articleFilter ->
            when (articleFilter) {
                is ArticleFilter.None -> articleCardDataList
                is ArticleFilter.Active -> articleCardDataList
                    .filter { it.section == articleFilter.filterOption.apiFilterName }
            }
        }

    override fun userRefreshArticles() {
        nyTimesRepository.updateArticleData()
    }

    private fun ArticleData.toArticleCardData() = ArticleCardData(
        id = id,
        publishedDate = publishedDate,
        section = section,
        title = title,
        descriptors = descriptors,
        media = media
    )

    private sealed class ArticleFilter {
        object None: ArticleFilter()

        /**
         * [mainScreenContent] article data is filtered by [filterOption]
         */
        class Active(val filterOption: FilterOptions): ArticleFilter()
    }
}


enum class FilterOptions(
    @StringRes val label: Int,
    /**
     * Actual name of section as it appears in API results. Used for actual filtering.
     */
    val apiFilterName: String
    ) {
    WORLD(R.string.section_name_world, "World"),
    US(R.string.section_name_us, "U.S."),
    POLITICS(R.string.section_name_politics, "Politics"),
    NY(R.string.section_name_ny, "New York"),
    BUSINESS(R.string.section_name_business, "Business"),
    OPINION(R.string.section_name_opinion, "Opinion"),
    TECH(R.string.section_name_tech, "Technology"),
    SCIENCE(R.string.section_name_science, "Science"),
    WELLNESS(R.string.section_name_wellness, "Well"),
    HEALTH(R.string.section_name_health, "Health"),
    SPORTS(R.string.section_name_sports, "Sports"),
    ARTS(R.string.section_name_arts, "Arts"),
    BOOKS(R.string.section_name_books, "Books"),
    STYLE(R.string.section_name_style, "Style"),
    FOOD(R.string.section_name_food, "Food"),
    TRAVEL(R.string.section_name_travel, "Travel")
}

