package com.peterstev.petadoption.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.peterstev.petadoption.utils.TABLE_NAME

@Entity(tableName = TABLE_NAME, indices = [Index(value = ["label"], unique = true)])
data class AdoptedPet(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var label: String = "",
    var value: String = ""
) {
    override fun toString(): String {
        return "AdoptedPet(id=$id, label='$label', value='$value')"
    }
}