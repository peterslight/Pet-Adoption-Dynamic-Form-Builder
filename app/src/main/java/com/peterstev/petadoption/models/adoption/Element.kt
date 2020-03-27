package com.peterstev.petadoption.models.adoption

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import javax.inject.Inject

data class Element @Inject constructor(
    @SerializedName("file")
    val `file`: String?,
    @SerializedName("formattedNumeric")
    val formattedNumeric: String?,
    @SerializedName("isMandatory")
    val isMandatory: Boolean?,
    @SerializedName("keyboard")
    val keyboard: String?,
    @SerializedName("label")
    val label: String?,
    @SerializedName("mode")
    val mode: String?,
    @SerializedName("rules")
    val rules: List<Rule> = listOf(),
    @SerializedName("type")
    val type: String?,
    @SerializedName("unique_id")
    val uniqueId: String?
) : Serializable {
    override fun toString(): String {
        return "Element(`file`='$`file`', formattedNumeric='$formattedNumeric', isMandatory=$isMandatory, keyboard='$keyboard', label='$label', mode='$mode', rules=$rules, type='$type', uniqueId='$uniqueId')"
    }
}