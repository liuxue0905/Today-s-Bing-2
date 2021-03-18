package com.lx.todaysbing.data.bingapis

import com.google.gson.annotations.SerializedName

data class ImageAnswer(
    @field:SerializedName("_type") val _type: String,
    @field:SerializedName("readLink") val readLink: String,
    @field:SerializedName("webSearchUrl") val webSearchUrl: String,
    @field:SerializedName("totalEstimatedMatches") val totalEstimatedMatches: Int,
    @field:SerializedName("images") val images: List<Image>,
)

data class Image(
    @field:SerializedName("name") val name: String,
    @field:SerializedName("webSearchUrl") val webSearchUrl: String,
    @field:SerializedName("webSearchUrlPingSuffix") val webSearchUrlPingSuffix: String,
    @field:SerializedName("thumbnailUrl") val thumbnailUrl: String,
    @field:SerializedName("datePublished") val datePublished: String,
    @field:SerializedName("contentUrl") val contentUrl: String,
    @field:SerializedName("hostPageUrl") val hostPageUrl: String,
    @field:SerializedName("hostPageUrlPingSuffix") val hostPageUrlPingSuffix: String,
    @field:SerializedName("contentSize") val contentSize: Int,
    @field:SerializedName("encodingFormat") val encodingFormat: String,
    @field:SerializedName("hostPageDisplayUrl") val hostPageDisplayUrl: String,
    @field:SerializedName("width") val width: Int,
    @field:SerializedName("height") val height: Int,
    @field:SerializedName("thumbnail") val thumbnail: Thumbnail,
    @field:SerializedName("imageInsightsToken") val imageInsightsToken: String,
    @field:SerializedName("imageId") val imageId: String,
    @field:SerializedName("insightsSourcesSummary") val insightsSourcesSummary: InsightsSourcesSummary,
)

data class Thumbnail(
    @field:SerializedName("width") val width: Int,
    @field:SerializedName("height") val height: Int,
) {
    companion object {
        @JvmStatic
        fun dimensionRatio(thumbnail: Thumbnail): String {
            return "${thumbnail.width}:${thumbnail.height}"
        }
    }
}

data class InsightsSourcesSummary(
    @field:SerializedName("shoppingSourcesCount") val shoppingSourcesCount: Int,
    @field:SerializedName("recipeSourcesCount") val recipeSourcesCount: Int,
)