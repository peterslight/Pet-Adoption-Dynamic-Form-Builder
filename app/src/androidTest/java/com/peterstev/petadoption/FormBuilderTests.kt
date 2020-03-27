package com.peterstev.petadoption

import androidx.appcompat.widget.AppCompatEditText
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.peterstev.petadoption.forms.FormBuilder
import com.peterstev.petadoption.models.adoption.PetAdoption
import com.peterstev.petadoption.repository.PetRepo
import com.peterstev.petadoption.ui.PetApplication
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FormBuilderTests {

    private lateinit var repository: PetRepo
    private lateinit var petAdoption: PetAdoption
    private lateinit var formBuilder: FormBuilder

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<PetApplication>()
        repository = PetRepo(context)
        petAdoption = repository.getPetAdoptionScope()
        formBuilder = FormBuilder(context)
    }

    @Test
    fun testAssertNotEmpty_true() {
        assertTrue(petAdoption.pages.isNotEmpty())
    }

    @Test
    fun testAssertElementsCreated_true() {
        val data = formBuilder.getElements(petAdoption.pages[0].sections)
        assertTrue(data.isNotEmpty())
    }

    @Test
    fun testAssertEditTextValueIsNotNull_true() {
        val data = formBuilder.getElements(petAdoption.pages[2].sections)
        val view = data.getValue("text_5")
        val editText = view.findViewById<AppCompatEditText>(R.id.view_et)
        editText.setText("Dr. Hassan")
        assertTrue(editText.text.toString() == "Dr. Hassan")
    }

    @Test
    fun testAssertNumElementsCreated_true() {
        val data = formBuilder.getElements(petAdoption.pages[2].sections)
        //2 editText and one header
        assertTrue(data.size == 3)
    }

}