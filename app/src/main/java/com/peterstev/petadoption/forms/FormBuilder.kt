@file:Suppress("ReplacePutWithAssignment")
@file:SuppressLint("SetTextI18n")

package com.peterstev.petadoption.forms

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.peterstev.petadoption.models.adoption.Section
import com.peterstev.petadoption.utils.*
import javax.inject.Inject

class FormBuilder @Inject constructor(val context: Context) {

    private val maplist: MutableMap<String, View> = mutableMapOf()

    fun getElements(sections: List<Section>): MutableMap<String, View> {



        generateElement(sections)
        return maplist
    }

    private fun generateElement(sections: List<Section>) {
        sections.forEach { section ->
            //generate a section header
            val sectionHeader = HeaderBuilder(section, context).build()
            maplist.putAll(sectionHeader)

            val elements = section.elements
            elements.forEach { element ->
                val elementType = element.type
                when {
                    //when element is a photo
                    elementType.equals(PHOTO) -> {
                        maplist.putAll(ImageBuilder(element, context).build())
                    }

                    //when element is a text field
                    elementType.equals(TEXT)
                            || elementType.equals(NUMERIC)
                            || elementType.equals(DATE_TIME) -> {
                        maplist.putAll(TextBuilder(element, context).build())
                    }

                    //is element a checkable option
                    elementType!! == YES_NO -> {
                        maplist.putAll(ChipBuilder(element, context, maplist).build())
                    }
                }
            }
        }
    }
}