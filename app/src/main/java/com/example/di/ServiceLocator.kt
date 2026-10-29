package com.example.di

import android.content.Context
import com.example.data.local.NaihatiDatabase
import com.example.data.local.SettingsDataStore
import com.example.data.remote.NetworkModule
import com.example.data.repository.NaihatiRepository

object ServiceLocator {
    @Volatile
    private var repository: NaihatiRepository? = null

    @Volatile
    private var settingsDataStore: SettingsDataStore? = null

    fun getRepository(context: Context): NaihatiRepository {
        return repository ?: synchronized(this) {
            val db = NaihatiDatabase.getInstance(context)
            val repo = NaihatiRepository(
                dao = db.dao(),
                api = runCatching { NetworkModule.api }.getOrNull(),
                context = context.applicationContext
            )
            repository = repo
            repo
        }
    }

    fun getSettingsDataStore(context: Context): SettingsDataStore {
        return settingsDataStore ?: synchronized(this) {
            val store = SettingsDataStore(context.applicationContext)
            settingsDataStore = store
            store
        }
    }
}
