package com.peterstev.petadoption.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.peterstev.petadoption.models.AdoptedPet
import com.peterstev.petadoption.utils.DATABASE_NAME
import javax.inject.Inject

@Database(entities = [AdoptedPet::class], version = 1)
abstract class PetDatabase : RoomDatabase() {

    abstract fun petAdoptionDao(): PetAdoptionDao



    companion object {
        private var instance: PetDatabase? = null

        fun getInstance() : PetDatabase{
            return instance!!
        }

        @Inject
        @Synchronized
        fun setInstance(context: Application): PetDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    PetDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }


}