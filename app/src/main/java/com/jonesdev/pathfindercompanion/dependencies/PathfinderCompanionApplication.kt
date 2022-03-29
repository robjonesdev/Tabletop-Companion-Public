package com.jonesdev.pathfindercompanion.dependencies

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/*
    Hilt requires an application class in order to use its dependency injection
    this class is the base application that will be used for this purpose.
 */

@HiltAndroidApp
class PathfinderCompanionApplication : Application() {

}