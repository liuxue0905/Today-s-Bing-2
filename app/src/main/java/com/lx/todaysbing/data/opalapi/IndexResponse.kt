package com.lx.todaysbing.data.opalapi

import com.google.gson.annotations.SerializedName

data class IndexResponse(
    @field:SerializedName("categories") val categories: List<String>,
    @field:SerializedName("colors") val colors: List<String>,
    @field:SerializedName("countries") val countries: List<String>,
    @field:SerializedName("holidays") val holidays: List<String>,
    @field:SerializedName("regions") val regions: List<String>,
    @field:SerializedName("tags") val tags: List<String>,
)
