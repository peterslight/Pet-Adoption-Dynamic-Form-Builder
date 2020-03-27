package com.peterstev.petadoption.models.adoption


import com.google.gson.annotations.SerializedName
import java.io.Serializable
import javax.inject.Inject

data class Rule @Inject constructor(
    @SerializedName("action")
    val action: String?,
    @SerializedName("condition")
    val condition: String?,
    @SerializedName("otherwise")
    val otherwise: String?,
    @SerializedName("targets")
    val targets: List<String> = listOf(),
    @SerializedName("value")
    val value: String?
) : Serializable {
    override fun toString(): String {
        return "Rule(action='$action', condition='$condition', otherwise='$otherwise', targets=$targets, value='$value')"
    }
}