package com.peterstev.petadoption.models.adoption


import com.google.gson.annotations.SerializedName
import java.io.Serializable
import javax.inject.Inject

data class PetAdoption @Inject constructor(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("pages")
    val pages: List<Page> = listOf()
) : Serializable {
    override fun toString(): String {
        return "PetAdoption(id='$id', name='$name', pages=$pages)"
    }
}