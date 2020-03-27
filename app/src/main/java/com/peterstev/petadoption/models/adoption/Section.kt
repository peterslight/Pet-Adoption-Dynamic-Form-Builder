package com.peterstev.petadoption.models.adoption

import com.google.gson.annotations.SerializedName
import com.peterstev.petadoption.models.adoption.Element
import java.io.Serializable
import javax.inject.Inject

data class Section @Inject constructor(
    @SerializedName("elements")
    val elements: List<Element> = listOf(),
    @SerializedName("label")
    val label: String?
) : Serializable {
    override fun toString(): String {
        return "Section(elements=$elements, label='$label')"
    }
}