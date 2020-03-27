package com.peterstev.petadoption.forms

import android.view.View

class FormRulesGenerator(
    private val visibilityRule: String,
    private val targetViews: List<String>,
    private val maplist: MutableMap<String, View>
) {
    fun toggleVisibilityStates() {
        //get the available target/view IDs
        targetViews.forEach { target ->
            //iterate through mapList to get matching views
            maplist.forEach { map ->
                //when view is found
                if (map.key == target) {
                    //get the associated view and apply appropriate rules
                    val view = map.value
                    when {
                        visibilityRule.equals(
                            PetFormOptions.CheckedAction.HIDE.name,
                            true
                        ) -> {
                            view.visibility = View.GONE
                        }
                        visibilityRule.equals(
                            PetFormOptions.CheckedAction.SHOW.name,
                            true
                        ) -> {
                            view.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }
}