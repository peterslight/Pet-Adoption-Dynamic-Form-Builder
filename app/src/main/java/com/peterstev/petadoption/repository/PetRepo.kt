package com.peterstev.petadoption.repository

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import com.peterstev.petadoption.R
import com.peterstev.petadoption.database.PetAdoptionDao
import com.peterstev.petadoption.database.PetDatabase
import com.peterstev.petadoption.models.AdoptedPet
import com.peterstev.petadoption.models.adoption.PetAdoption
import com.peterstev.petadoption.utils.LOGGER
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.nio.charset.Charset
import javax.inject.Inject


class PetRepo @Inject constructor(val context: Context) {

    private var petAdoptionDao: PetAdoptionDao

    init {
        val database = PetDatabase.getInstance()
        petAdoptionDao = database.petAdoptionDao()
    }


    //insert into db
    fun insert(adoptedPet: AdoptedPet) {
        if (adoptedPet.label.isEmpty() || adoptedPet.value.isEmpty()) {
            LOGGER("Not saved")
            return
        } else
            petAdoptionDao.insertAdoptedPet(adoptedPet)
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun getSingleData(label: String): AdoptedPet {
        return petAdoptionDao.getSingleEntryData(label)
    }

    //fetch data from local datasource
    private fun getLocalData(): Flowable<MutableList<AdoptedPet>> {
        return petAdoptionDao.getAllAdoptions()
    }

    //public endpoint for viewModel to call
    fun fetchData(): Flowable<MutableList<AdoptedPet>> {
        return getLocalData()
    }

    //parse adoption form file into PetAdoption object
    fun getPetAdoptionScope(): PetAdoption {
        val gson = Gson()
        val json = getJsonAsString()
        return gson.fromJson(json, PetAdoption::class.java)
    }

    //retrieve local Json adoption form file
    private fun getJsonAsString(): String {
        val stream = context.resources.openRawResource(R.raw.pet_adoption)
        val byteStream = ByteArray(stream.available())
        stream.read(byteStream)
        stream.close()
        return String(byteStream, Charset.defaultCharset())
    }
}