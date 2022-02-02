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
import kotlinx.coroutines.flow.onEach
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

    private val filter = MutableStateFlow<ArticleFilter>(ArticleFilter.None)

    private val isLoading = MutableStateFlow(false)

    private val articleDataResponse: Flow<ArticleDataResponse> =
        nyTimesRepository.articleDataResponse.onEach {
            /**
             * Whenever an update comes in loading is complete.
             */
            isLoading.value = false
        }


    override val mainScreenContent: Flow<MainScreenContent> = combine(
        articleDataResponse, isLoading, filter
    ) { articleResponse, loading, _ ->
        val mainScreenData = when (articleResponse) {
            is ArticleDataResponse.Error -> {
                MainScreenData.Error(articleResponse.message)
            }
            is ArticleDataResponse.Uninitialized -> {
                nyTimesRepository.updateArticleData()
                MainScreenData.Uninitialized
            }
            is ArticleDataResponse.Success -> {
                val filteredData =
                    applyFilter(articleResponse.articleDataList.map { it.toArticleCardData() })

                if (filteredData.isEmpty()) {
                    MainScreenData.Empty
                } else {
                    MainScreenData.Success(filteredData)
                }
            }
        }

        MainScreenContent(filterItemList, mainScreenData, loading)
    }


    override fun userRefreshArticles() {
        isLoading.value = true
        nyTimesRepository.updateArticleData()
    }


    override fun userChangedFilter(filterOption: FilterOptions) {
        val curArticleFilter = filter.value

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
                if (curArticleFilter.filterOption != filterOption) {
                    updateFilterListItem(curArticleFilter.filterOption.ordinal)
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


    private fun updateFilterListItem(filterIndex: Int) {
        val filterItem = filterItemList[filterIndex]

        filterItemList[filterIndex] = FilterItem(
            filter = filterItem.filter,
            isSelected = !filterItem.isSelected
        )
    }


    private fun applyFilter(articleCardDataList: List<ArticleCardData>) =
        when (val articleFilter = filter.value) {
            is ArticleFilter.None -> articleCardDataList
            is ArticleFilter.Active -> articleCardDataList
                .filter { it.section == articleFilter.filterOption.apiFilterName }
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
        object None : ArticleFilter()

        /**
         * [mainScreenContent] article data is filtered by [filterOption]
         */
        class Active(val filterOption: FilterOptions) : ArticleFilter()
    }
}


enum class FilterOptions(
    @StringRes val label: Int,
    /**
     * Actual name of section as it appears in API results. Used for actual filtering.
     */
    val apiFilterName: String
) {
    World(R.string.section_name_world, "World"),
    Us(R.string.section_name_us, "U.S."),
    Politics(R.string.section_name_politics, "Politics"),
    Ny(R.string.section_name_ny, "New York"),
    Business(R.string.section_name_business, "Business"),
    Opinion(R.string.section_name_opinion, "Opinion"),
    Tech(R.string.section_name_tech, "Technology"),
    Science(R.string.section_name_science, "Science"),
    Wellness(R.string.section_name_wellness, "Well"),
    Health(R.string.section_name_health, "Health"),
    Sports(R.string.section_name_sports, "Sports"),
    Arts(R.string.section_name_arts, "Arts"),
    Books(R.string.section_name_books, "Books"),
    Style(R.string.section_name_style, "Style"),
    Food(R.string.section_name_food, "Food"),
    Travel(R.string.section_name_travel, "Travel")
}

