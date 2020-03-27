@file:Suppress("ReplacePutWithAssignment")
@file:SuppressLint("SetTextI18n")

package com.peterstev.petadoption.forms

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.peterstev.petadoption.R
import com.peterstev.petadoption.models.adoption.Element

class ChipBuilder(
    private val element: Element,
    private val context: Context,
    private val originalList: MutableMap<String, View>
) {

    fun build(): MutableMap<String, View> {
        val mapList: MutableMap<String, View> = mutableMapOf()
        val chiplayout =
            LayoutInflater.from(context)
                .inflate(R.layout.view_selection_group, null, false)
        val chipGroup = chiplayout.findViewById<ChipGroup>(R.id.view_chip_group)
        val chipText =
            chiplayout.findViewById<AppCompatTextView>(R.id.view_chip_text)
        if (element.isMandatory!!)
            chipText.text = "${element.label} *" else chipText.text = element.label

        if (element.rules.isNotEmpty()) {
            val rules = element.rules
            chipGroup.setOnCheckedChangeListener { _, checkedId ->
                rules.forEach { rule ->
                    val ruleOption = rule.value
                    val chip = chipGroup.findViewById<Chip>(checkedId)
                    if (chip.text.toString().equals(ruleOption, true)) {
                        //yard criteria is fulfilled, perform appropriate action
                        //get the available targets and retrieve their respective views
                        //and perform specified action on them
                        FormRulesGenerator(
                            rule.action!!,
                            rule.targets,
                            originalList
                        ).toggleVisibilityStates()
                    } else {
                        FormRulesGenerator(
                            rule.otherwise!!,
                            rule.targets,
                            originalList
                        ).toggleVisibilityStates()
                    }
                }
            }
        }
        mapList.put(element.uniqueId!!, chiplayout)
        return mapList
    }
}