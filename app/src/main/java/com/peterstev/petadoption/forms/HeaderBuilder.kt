@file:Suppress("ReplacePutWithAssignment")
@file:SuppressLint("SetTextI18n")

package com.peterstev.petadoption.forms

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.peterstev.petadoption.R
import com.peterstev.petadoption.models.adoption.Section
import java.util.*

class HeaderBuilder(private val section: Section, private val context: Context) {

    fun build(): MutableMap<String, View> {
        val mapList: MutableMap<String, View> = mutableMapOf()
        val sectionLayout =
            LayoutInflater.from(context).inflate(R.layout.view_text, null, false)
        val sectionHeader = sectionLayout.findViewById<AppCompatTextView>(R.id.view_text)
        sectionHeader.text = section.label
        mapList.put(getSectionHeaderID(section.label!!), sectionLayout)
        return mapList
    }

    private fun getSectionHeaderID(text: String): String {
        return text.replace(" ", "_").toLowerCase(Locale.getDefault())
    }

}