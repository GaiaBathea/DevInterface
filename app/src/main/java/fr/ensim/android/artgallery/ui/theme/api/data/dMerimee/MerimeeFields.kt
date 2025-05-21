package fr.ensim.android.artgallery.ui.theme.api.data.dMerimee

data class MerimeeFields(
    val tico: String, // Title
    val wadrs: String, // Address
    val edif: String?, // Building type
    val dpro: String?, // Construction date
    val autr: List<String>?, // Architects
    val photo: List<String>? // Photos
)