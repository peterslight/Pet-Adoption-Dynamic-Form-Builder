@file:SuppressLint("CheckResult")

package com.peterstev.petadoption.view_models

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.peterstev.petadoption.forms.FormBuilder
import com.peterstev.petadoption.forms.FormValidation
import com.peterstev.petadoption.models.AdoptedPet
import com.peterstev.petadoption.models.ErrorValidationForm
import com.peterstev.petadoption.models.adoption.Page
import com.peterstev.petadoption.models.adoption.PetAdoption
import com.peterstev.petadoption.repository.PetRepo
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(mApplication: Application) :
    AndroidViewModel(mApplication) {

    @Inject
    lateinit var petRepo: PetRepo
    @Inject
    lateinit var formValidator: FormValidation
    @Inject
    lateinit var formBuilder: FormBuilder

    //stack of generated views
    private val viewTree: MutableMap<String, View> = mutableMapOf()

    //get data from json file and parse to PetAdoptionClass
    fun getPetAdoptionScope(): PetAdoption {
        return petRepo.getPetAdoptionScope()
    }

    //get generated forms by page
    fun getPetAdoptionFormPage(page: Page, context: Context): MutableMap<String, View> {
        formBuilder = FormBuilder(context)
        val data = formBuilder.getElements(page.sections)
        viewTree.putAll(data)
        return data
    }

    //validate all fields and return the extracted values
    fun getValidViews(): MutableList<AdoptedPet> {
        return formValidator.getValidFields()
    }

    //get history of adopted pets data
    fun fetchData(): Flowable<MutableList<AdoptedPet>> {
        return petRepo.fetchData()
    }

    //insert validated form data into the database
    fun insertData(adoptedPet: AdoptedPet) {
        petRepo.insert(adoptedPet)
    }


    //return the headers of database entries
    fun getPetHistoryHeaders(): MutableList<String> {
        val list: MutableList<String> = mutableListOf()
        fetchData()
            .subscribeOn(Schedulers.io())
            .subscribe {
                it.forEach {
                    list.add(it.label)
                }
            }
        return list
    }

    //validate form and return the error fields
    fun validateForm(): MutableList<ErrorValidationForm> {
        return formValidator.validateFields(getPetAdoptionScope(), viewTree)!!
    }
}