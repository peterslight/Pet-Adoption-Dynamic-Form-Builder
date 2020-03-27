package com.peterstev.petadoption.providers

import android.app.Application
import android.content.Context
import android.view.View
import com.peterstev.petadoption.forms.FormValidation
import com.peterstev.petadoption.models.adoption.PetAdoption
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application

}