package com.nytimesmostviewedarticles.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nytimesmostviewedarticles.R
import com.nytimesmostviewedarticles.datatypes.ArticleData
import com.nytimesmostviewedarticles.datatypes.ArticleDataResponse
import com.nytimesmostviewedarticles.network.NetworkConnectionService
import com.nytimesmostviewedarticles.network.NetworkStatus
import com.nytimesmostviewedarticles.network.NyTimesRepository
import com.nytimesmostviewedarticles.ui.components.ArticleCardData
import com.nytimesmostviewedarticles.ui.components.FilterItem
import com.nytimesmostviewedarticles.ui.screens.MainScreenContent
import com.nytimesmostviewedarticles.ui.screens.MainScreenData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
    private val nyTimesRepository: NyTimesRepository,
    networkConnectionService: NetworkConnectionService
) : ViewModel(), MainScreenViewModel {

    private val filterItemListFlow = MutableStateFlow(FilterOptions.values().map { FilterItem(it) }.toList())

    private val isLoadingFlow = MutableStateFlow(false)

    private val networkStatusFlow: StateFlow<NetworkStatus> = networkConnectionService.networkStatusFlow

    private val articleDataResponseFlow: Flow<ArticleDataResponse> =
        nyTimesRepository.articleDataResponse.onEach {
            /**
             * Whenever an update comes in loading is complete.
             */
            isLoadingFlow.value = false
        }

    override val mainScreenContent: Flow<MainScreenContent> = combine(
        articleDataResponseFlow, isLoadingFlow, filterItemListFlow, networkStatusFlow
    ) { articleDataResponse, isLoading, filterItemList, networkStatus ->
        val mainScreenData = when (articleDataResponse) {
            is ArticleDataResponse.Error -> {
                MainScreenData.Error(message = articleDataResponse.message)
            }
            is ArticleDataResponse.Uninitialized -> {
                nyTimesRepository.updateArticleData()
                MainScreenData.Uninitialized
            }
            is ArticleDataResponse.Success -> {
                /**
                 * Filter the data if a filter is selected
                 */
                val filteredData = filterItemList
                    .firstOrNull { it.isSelected }
                    ?.let { filterItem ->
                        articleDataResponse.articleDataList
                            .filter { it.section == filterItem.filter.apiFilterName }
                    }
                    ?: articleDataResponse.articleDataList

                if (filteredData.isNullOrEmpty()) {
                    MainScreenData.Empty
                } else {
                    MainScreenData.Success(
                        articleRowDataList = filteredData.map { it.toArticleCardData() }
                    )
                }
            }
        }

        MainScreenContent(filterItemList, mainScreenData, isLoading, networkStatus)
    }


    override fun userRefreshArticles() {
        if (networkStatusFlow.value == NetworkStatus.CONNECTED) {
            isLoadingFlow.value = true
            nyTimesRepository.updateArticleData()
        }
    }


    override fun userChangedFilter(filterOption: FilterOptions) {
        val newFilterList = FilterOptions.values().map { FilterItem(it) }.toMutableList()

        val prevFilterOption = filterItemListFlow.value.firstOrNull { it.isSelected }

        if (prevFilterOption?.filter != filterOption) {
            newFilterList[filterOption.ordinal] = FilterItem(
                filter = filterOption,
                isSelected = true
            )
        }

        filterItemListFlow.value = newFilterList.toList()
    }



    private fun ArticleData.toArticleCardData() = ArticleCardData(
        id = id,
        publishedDate = publishedDate,
        section = section,
        title = title,
        descriptors = descriptors,
        media = media
    )
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

