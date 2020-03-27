package com.peterstev.petadoption.ui

import android.app.Application
import com.peterstev.petadoption.components.AppComponent
import com.peterstev.petadoption.components.DaggerAppComponent
import com.peterstev.petadoption.components.DaggerAppComponent.builder
import com.peterstev.petadoption.database.PetDatabase
import com.peterstev.petadoption.providers.AppModule

class PetApplication : Application() {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        PetDatabase.setInstance(this)
        appComponent = builder()
            .application(this)
            .build()


//        DaggerDefaultComponent.create().application(this)
//        DaggerDefaultComponent.
    }
}