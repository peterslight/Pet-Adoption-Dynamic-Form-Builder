package com.peterstev.petadoption.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peterstev.petadoption.models.AdoptedPet
import com.peterstev.petadoption.utils.TABLE_NAME
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface PetAdoptionDao {

    @Query("SELECT * FROM $TABLE_NAME ORDER BY id ASC")
    fun getAllAdoptions(): Flowable<MutableList<AdoptedPet>>

    @Query("SELECT * FROM $TABLE_NAME WHERE label LIKE :label")
    fun getSingleEntryData(label: String): AdoptedPet

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAdoptedPet(adoptedPet: AdoptedPet): Completable
}