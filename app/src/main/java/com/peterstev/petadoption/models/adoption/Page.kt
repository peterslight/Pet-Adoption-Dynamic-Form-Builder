package com.peterstev.petadoption.models.adoption


import com.google.gson.annotations.SerializedName
import java.io.Serializable
import javax.inject.Inject

data class Page @Inject constructor(
    @SerializedName("label")
    val label: String?,
    @SerializedName("sections")
    val sections: List<Section> = listOf()
) : Serializable {
    override fun toString(): String {
        return "Page(label='$label', sections=$sections)"
    }
}