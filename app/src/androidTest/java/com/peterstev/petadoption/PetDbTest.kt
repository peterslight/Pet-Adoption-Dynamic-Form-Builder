package com.peterstev.petadoption

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.peterstev.petadoption.database.PetDatabase
import com.peterstev.petadoption.models.AdoptedPet
import com.peterstev.petadoption.models.adoption.PetAdoption
import com.peterstev.petadoption.repository.PetRepo
import com.peterstev.petadoption.ui.PetApplication
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PetDbTest {

    lateinit var database: PetDatabase
    private lateinit var repository: PetRepo
    private lateinit var petAdoption: PetAdoption
    private lateinit var adoptedPet: AdoptedPet

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<PetApplication>()
        database = Room.inMemoryDatabaseBuilder(context, PetDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = PetRepo(context)
        petAdoption = repository.getPetAdoptionScope()
        adoptedPet = AdoptedPet()
        adoptedPet.label = "Test Pet"
        adoptedPet.value = "dog"
    }

    @Test
    fun testAssertDataInserted_true() {
        repository.insert(adoptedPet)
        val data = repository.getSingleData(adoptedPet.label)
        assertTrue(data.label == adoptedPet.label)
    }

    @Test
    fun testAssertDataRetrieved_true() {
        repository.fetchData()
            .observeOn(Schedulers.io())
            .subscribe {
                assertTrue(it.size > 0)
            }
    }

    @Test
    fun testEmptyDataSetEntered_false() {
        val pet = AdoptedPet()
        pet.label = ""
        pet.value = ""
        repository.insert(pet)
        val data = repository.getSingleData(adoptedPet.label)
        assertFalse(data.label == pet.label)
    }



}