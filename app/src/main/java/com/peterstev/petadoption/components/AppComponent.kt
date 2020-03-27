package com.peterstev.petadoption.components

import android.app.Application
import com.peterstev.petadoption.providers.AppModule
import com.peterstev.petadoption.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun injectActivity(mainActivity: MainActivity)

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application: Application) : Builder
        fun build() : AppComponent
    }
}