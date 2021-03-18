package com.lx.todaysbing.data.bingapis

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @field:SerializedName("_type") val _type: String,
    @field:SerializedName("version") val version: String,
    @field:SerializedName("traceId") val traceId: String,
    @field:SerializedName("impressionGuid") val impressionGuid: String,
    @field:SerializedName("pingUrlBase") val pingUrlBase: String,
    @field:SerializedName("pageLoadPingUrl") val pageLoadPingUrl: String,
    @field:SerializedName("answers") val answers: List<ImageAnswer>,
)