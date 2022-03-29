package com.jonesdev.pathfindercompanion.testrunners

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

// this class is used to create a test runner for Hilt, we are replacing the default application
// with a custom one of HiltTestApplication
class HiltTestRunner: AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name , context)
    }

}