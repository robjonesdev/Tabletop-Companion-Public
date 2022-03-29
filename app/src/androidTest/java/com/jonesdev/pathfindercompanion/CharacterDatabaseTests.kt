package com.jonesdev.pathfindercompanion

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jonesdev.pathfindercompanion.data.datasource.database.AppDatabase
import com.jonesdev.pathfindercompanion.data.repository.api.GoogleSheetsDataRepositoryImpl
import com.jonesdev.pathfindercompanion.ui.viewmodel.CharacterInspectionViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@HiltAndroidTest
class CharacterDatabaseTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("TEST_CHARACTER_DATABASE")
    lateinit var characterDb: AppDatabase

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testDbExists(){
        assert(::characterDb.isInitialized)
    }

}