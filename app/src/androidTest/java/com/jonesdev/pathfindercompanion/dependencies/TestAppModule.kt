package com.jonesdev.pathfindercompanion.dependencies

import android.content.Context
import androidx.room.Room
import com.jonesdev.pathfindercompanion.data.datasource.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("TEST_CHARACTER_DATABASE")
    fun provideInMemoryDb(@ApplicationContext context: Context): AppDatabase =
        Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

}