package net.xblacky.animexstream.ui.main.home.epoxy

import android.view.View
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.Carousel.setDefaultGlobalSnapHelperFactory
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.TypedEpoxyController
import net.xblacky.animexstream.R
import net.xblacky.animexstream.utils.constants.Const
import net.xblacky.animexstream.utils.epoxy.AnimeCommonModel_
import net.xblacky.animexstream.utils.model.AnimeMetaModel
import net.xblacky.animexstream.utils.model.HomeScreenModel

class HomeController(private val adapterCallbacks: EpoxyAdapterCallbacks) :
    TypedEpoxyController<ArrayList<HomeScreenModel>>() {

    override fun buildModels(data: ArrayList<HomeScreenModel>) {
        data.forEach { homeScreenModel ->
            AnimeMiniHeaderModel_()
                .id(homeScreenModel.typeValue)
                .typeName(homeScreenModel.type)
                .addIf(!homeScreenModel.animeList.isNullOrEmpty(), this)

            when (homeScreenModel.typeValue) {
                Const.TYPE_MOVIE, Const.TYPE_NEW_SEASON -> {
                    val movieModelList = arrayListOf<AnimeCommonModel_>()
                    homeScreenModel.animeList?.forEach {
                        val animeMetaModel = it

                        movieModelList.add(
                            AnimeCommonModel_()
                                .id(animeMetaModel.ID)
                                .clickListener { model, _, _, _ ->
                                    adapterCallbacks.animeTitleClick(model = model.animeMetaModel())
                                }
                                .animeMetaModel(animeMetaModel)
                        )
                    }
                    setDefaultGlobalSnapHelperFactory(null)

                    CarouselModel_()
                        .id(homeScreenModel.hashCode())
                        .models(movieModelList)
                        .padding(Carousel.Padding.dp(20, 0, 20, 0, 20))
                        .addTo(this)

                }
                Const.TYPE_POPULAR_ANIME -> {
                    homeScreenModel.animeList?.forEach {
                        val animeMetaModel = it
                        AnimePopularModel_()
                            .id(animeMetaModel.ID)
                            .clickListener { model, _, _, _ ->
                                adapterCallbacks.animeTitleClick(model = model.animeMetaModel())
                            }
                            .animeMetaModel(animeMetaModel)
                            .addTo(this)
                    }

                }
                else -> {
                    val recentModelList = arrayListOf<AnimeSubDubModel2_>()
                    homeScreenModel.animeList?.forEach {
                        val animeMetaModel = it
                        recentModelList.add(
                            AnimeSubDubModel2_()
                                .id(animeMetaModel.ID)
                                .clickListener { model, _, clickedView, _ ->
                                    recentSubDubClick(model.animeMetaModel(), clickedView)
                                }
                                .animeMetaModel(animeMetaModel)
                        )
                    }
                    CarouselModel_()
                        .id(homeScreenModel.hashCode())
                        .models(recentModelList)
                        .padding(Carousel.Padding.dp(20, 0, 20, 0, 20))
                        .addTo(this)
                }
            }
        }

    }

    private fun recentSubDubClick(model: AnimeMetaModel, clickedView: View) {
        when (clickedView.id) {
            R.id.backgroundImage -> {
                adapterCallbacks.recentSubDubEpisodeClick(model = model)
            }
            R.id.animeTitle -> {
                adapterCallbacks.animeTitleClick(model = model)
            }
        }
    }

    interface EpoxyAdapterCallbacks {
        fun recentSubDubEpisodeClick(model: AnimeMetaModel)
        fun animeTitleClick(model: AnimeMetaModel)
    }

}