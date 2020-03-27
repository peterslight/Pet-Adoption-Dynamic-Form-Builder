@file:Suppress("ReplacePutWithAssignment")
@file:SuppressLint("SetTextI18n")

package com.peterstev.petadoption.forms

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.peterstev.petadoption.R
import com.peterstev.petadoption.models.adoption.Element
import com.peterstev.petadoption.utils.DATE_TIME
import com.peterstev.petadoption.utils.NUMERIC
import com.peterstev.petadoption.utils.TEXT
import java.text.SimpleDateFormat
import java.util.*

class TextBuilder(private val element: Element, private val context: Context) {

    fun build(): MutableMap<String, View> {
        val mapList: MutableMap<String, View> = mutableMapOf()
        val calendar: Calendar = Calendar.getInstance()

        val elementType = element.type

        val elementLabel = element.label
        val textLayout =
            LayoutInflater.from(context).inflate(R.layout.view_input, null, false)
        val editText = textLayout.findViewById<AppCompatEditText>(R.id.view_et)
        if (element.isMandatory!!) editText.hint =
            "$elementLabel *" else editText.hint = elementLabel

        //here we switch the input types based on the type of text field requested
        when {
            elementType.equals(TEXT) -> {
                when {
                    elementLabel!!.contains(
                        PetFormOptions.TextInputType.EMAIL.name,
                        true
                    ) -> {
                        editText.inputType =
                            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    }
                    elementLabel.contains(
                        PetFormOptions.TextInputType.PASSWORD.name,
                        true
                    ) -> {
                        editText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    }
                    elementLabel.contains(
                        PetFormOptions.TextInputType.NUMBER.name,
                        true
                    ) -> {
                        editText.inputType =
                            InputType.TYPE_CLASS_PHONE or InputType.TYPE_CLASS_NUMBER
                    }
                    elementLabel.contains(
                        PetFormOptions.TextInputType.NAME.name,
                        true
                    ) -> {
                        editText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                    }
                    elementLabel.contains(
                        PetFormOptions.TextInputType.DATE.name,
                        true
                    ) -> {
                        editText.inputType =
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_CLASS_DATETIME
                    }
                }
            }
            elementType.equals(NUMERIC) -> {
                editText.inputType =
                    InputType.TYPE_CLASS_PHONE or InputType.TYPE_CLASS_NUMBER
                editText.addTextChangedListener(PhoneNumberFormattingTextWatcher())
            }
            elementType.equals(DATE_TIME) -> {
                //we dont want the date edit text to gain focus because we wanna trigger a date picker dialog
                //hence we set an onclick listener
                editText.isFocusableInTouchMode = false
                editText.isFocusable = false

                editText.setOnClickListener {
                    DatePickerDialog(
                        context,
                        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                            calendar.set(Calendar.YEAR, year)
                            calendar.set(Calendar.MONTH, month)
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            val dateFormat = "MM-dd-YYYY"
                            val simpleDateFormat =
                                SimpleDateFormat(dateFormat, Locale.getDefault())
                            editText.setText(simpleDateFormat.format(calendar.time))
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }
        }
        mapList.put(element.uniqueId!!, textLayout)
        return mapList
    }
}