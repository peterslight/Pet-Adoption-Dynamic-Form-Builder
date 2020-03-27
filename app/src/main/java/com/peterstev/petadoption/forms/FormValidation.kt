package com.peterstev.petadoption.forms

import android.telephony.PhoneNumberUtils
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.peterstev.petadoption.R
import com.peterstev.petadoption.models.AdoptedPet
import com.peterstev.petadoption.models.ErrorValidationForm
import com.peterstev.petadoption.models.adoption.PetAdoption
import com.peterstev.petadoption.utils.*
import javax.inject.Inject

class FormValidation @Inject constructor() {

    private val list: MutableList<AdoptedPet> = mutableListOf()

    fun getValidFields(): MutableList<AdoptedPet> {
        return list
    }

    fun validateFields(adoptionForm: PetAdoption, map: MutableMap<String, View>): MutableList<ErrorValidationForm>? {
        val errorErrorList: MutableList<ErrorValidationForm> = mutableListOf()
        var errorForm: ErrorValidationForm
        var validForm: AdoptedPet
        list.clear()

        //iterate through the pages of the form
        adoptionForm.pages.forEachIndexed { index, page ->
            page.sections.forEach { section ->
                //iterate through the various elements on the screen.
                val elements = section.elements
                elements.forEach elementLoop@{ element ->

                    val elementType = element.type
                    val elementLabel = element.label
                    val key = element.uniqueId

                    //we are only interested in elements that require user input
                    //hence we look at only the checked elements and edit text
                    when {
                        elementType.equals(TEXT)
                                || elementType.equals(NUMERIC)
                                || elementType.equals(DATE_TIME) -> {

                            //create an error log to detect all possible problems in the form and report back
                            errorForm = ErrorValidationForm(key, elementLabel, index)

                            //receive the view and begin checks
                            val layout = map.getValue(key!!)
                            val view = layout.findViewById<AppCompatEditText>(R.id.view_et)
                            val text = view.text.toString()

                            //if the text is empty and the element is mandatory
                            // note the error and enter in to our error logger
                            if (text.isEmpty()) {
                                if (element.isMandatory!!) {
                                    view.error = "${element.label} is mandatory"
                                    errorErrorList.add(errorForm)
                                    return@elementLoop
                                } else {
                                    view.error = null
                                }
                            } else {
                                //if text isnt empty, perform SIMPLE and GENERIC input type validations
                                //while noting the fields that fail our validations
                                //if they pass, we do nothing and assume success
                                when {
                                    elementLabel!!.contains(
                                        PetFormOptions.TextInputType.EMAIL.name,
                                        true
                                    ) -> {
                                        //simple email validation
                                        if (!validateEmail(text)) {
                                            view.error = "Enter valid Email address"
                                            errorErrorList.add(errorForm)
                                            return@elementLoop
                                        }

                                    }
                                    elementLabel.contains(
                                        PetFormOptions.TextInputType.NUMBER.name,
                                        true
                                    ) -> {
                                        val number = PhoneNumberUtils.stripSeparators(text)
                                        if (number.length < 6) {
                                            view.error = "Enter valid Phone number"
                                            errorErrorList.add(errorForm)
                                            return@elementLoop
                                        }

                                    }
                                    elementLabel.contains(
                                        PetFormOptions.TextInputType.NAME.name,
                                        true
                                    ) -> {
                                        if (!text.contains(" ")) {
                                            view.error = "Enter FullName (First & Last name)"
                                            errorErrorList.add(errorForm)
                                            return@elementLoop
                                        }

                                    }
                                    elementLabel.contains(
                                        PetFormOptions.TextInputType.DATE.name,
                                        true
                                    ) -> {
                                        if (!text.contains("-")) {
                                            view.error = "Kindly select your date of birth"
                                            errorErrorList.add(errorForm)
                                            return@elementLoop
                                        }

                                    }
                                }
                            }

                            validForm = buildValidForm(
                                elementLabel!!,
                                if (text.isEmpty() && !element.isMandatory!!) "N/A" else text
                            )
                            list.add(validForm)
                        }

                        elementType.equals(YES_NO) -> {
                            //get reference to our view
                            val layout = map.getValue(key!!)
                            val chipGroup = layout.findViewById<ChipGroup>(R.id.view_chip_group)
                            val chipID = chipGroup.checkedChipId

                            //ensure that we have a checked chip
                            if (chipID == -1) {
                                //no chip was checked, note the error abort validation for this element
                                errorForm = ErrorValidationForm(
                                    key,
                                    "$elementLabel: you must select an option to proceed",
                                    index
                                )
                                errorErrorList.add(errorForm)
                                return@elementLoop
                            } else {
                                //else a chip was clicked, which is okay beu we need to check if this checked chip
                                //should have fulfilled any rule before we proceed
                                val checkedChip = layout.findViewById<Chip>(chipID)
                                val rules = element.rules
                                //we iterate through our rules to ensure all rules were observed
                                rules.forEach { rule ->
                                    if (rule.value.equals(checkedChip.text.toString())) {
                                        //this chip should trigger a rule.
                                        //hence check the enclosed views to see if any of the rules are mandatory
                                        rule.targets.forEach { target ->
                                            //we find a view by the target tag
                                            val innerLayout = map.getValue(target)

                                            //next we go through our elements list to check
                                            //if that associated element is mandatory
                                            elements.forEach { innerElement ->
                                                if (innerElement.uniqueId.equals(target)) {
                                                    //we found one !!!
                                                    //we tag and bag :-)
                                                    if (innerElement.isMandatory!!) {
                                                        val innerView =
                                                            innerLayout.findViewById<AppCompatEditText>(
                                                                R.id.view_et
                                                            )
                                                        innerView.error =
                                                            "${innerElement.label} is mandatory"
                                                        errorForm = ErrorValidationForm(
                                                            target,
                                                            "${innerElement.label}: you must fill this field to proceed",
                                                            index
                                                        )
                                                        errorErrorList.add(errorForm)
                                                        return@elementLoop
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                validForm = buildValidForm(
                                    elementLabel!!,
                                    checkedChip.text.toString()
                                )
                                list.add(validForm)
                            }
                        }
                    }
                }
            }
        }
        return errorErrorList
    }

    private fun buildValidForm(label: String, value: String): AdoptedPet {
        return AdoptedPet(null, label, capitalize(value))
    }

}