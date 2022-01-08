package com.taskphase.git.utils

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(
        name = "REPOS_LIST"
    )

    val getRepoList: Flow<String>
        get() = dataStore.data.map { preferences ->
            preferences[REPO_KEY] ?: ""
        }

    suspend fun saveRepoList(repoList: String) {
        dataStore.edit { preferences ->
            preferences[REPO_KEY] = repoList
        }
    }

    companion object {
        val REPO_KEY = preferencesKey<String>("key_repos")
    }
}