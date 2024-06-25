package com.dicoding.asclepius

import android.app.Application
import com.dicoding.asclepius.data.di.AppModule
import com.dicoding.asclepius.data.di.AppModuleImpl
import com.dicoding.asclepius.data.entity.ResultEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MyApp : Application() {
    companion object {
        lateinit var realm: Realm
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(ResultEntity::class)
            )
        )
        appModule = AppModuleImpl(this)
    }
}