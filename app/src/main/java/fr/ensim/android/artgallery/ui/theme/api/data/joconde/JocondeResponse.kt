package fr.ensim.android.artgallery.ui.theme.api.data.joconde

import com.google.gson.annotations.SerializedName

data class JocondeResponse(
    @SerializedName("response") val response: JocondeResponseData
)

data class JocondeResponseData(
    @SerializedName("docs") val docs: List<JocondeArtwork>,
    @SerializedName("numFound") val numFound: Int,
    @SerializedName("start") val start: Int
)

data class JocondeArtwork(
    @SerializedName("REF") val reference: String?,
    @SerializedName("TICO") val title: String?,
    @SerializedName("AUTR") val author: String?,
    @SerializedName("DOMN") val domain: String?,
    @SerializedName("DENO") val denomination: String?,
    @SerializedName("PERI") val period: String?,
    @SerializedName("MILL") val millennium: String?,
    @SerializedName("EPOQ") val epoch: String?,
    @SerializedName("HIST") val history: String?,
    @SerializedName("BIOG") val biography: String?,
    @SerializedName("IMG") val images: List<String>?,
    @SerializedName("LOCA") val location: String?,
    @SerializedName("TECH") val technique: String?,
    @SerializedName("DIMS") val dimensions: String?,
    @SerializedName("COPY") val copyright: String?
)