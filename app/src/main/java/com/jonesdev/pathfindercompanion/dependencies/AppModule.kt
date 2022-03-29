package com.jonesdev.pathfindercompanion.dependencies

import android.content.Context
import androidx.room.Room
import com.jonesdev.pathfindercompanion.data.datasource.api.GoogleSheetsAPI
import com.jonesdev.pathfindercompanion.data.datasource.database.AppDatabase
import com.jonesdev.pathfindercompanion.data.repository.api.GoogleSheetsDataRepositoryImpl
import com.jonesdev.pathfindercompanion.data.repository.CharacterDataRepository
import com.jonesdev.pathfindercompanion.data.repository.database.RoomDatabaseRepositoryImpl
import com.jonesdev.pathfindercompanion.shared.Constants.SHEETS_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): PathfinderCompanionApplication {
        return app as PathfinderCompanionApplication
    }

    @Singleton
    @Provides
    fun provideGoogleSheetsApi(): GoogleSheetsAPI {
        return Retrofit.Builder()
            .baseUrl(SHEETS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleSheetsAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideCharacterDataRepository(api: GoogleSheetsAPI): CharacterDataRepository {
        return GoogleSheetsDataRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext app: Context): AppDatabase {
        return Room.databaseBuilder(app,
            AppDatabase::class.java, "database-name").build()
    }

    @Singleton
    @Provides
    fun provideGoogleSheetsCharacterDataRepository(api: GoogleSheetsAPI): GoogleSheetsDataRepositoryImpl
    {
        return GoogleSheetsDataRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideRoomCharacterDataRepository(appDatabase: AppDatabase): RoomDatabaseRepositoryImpl {
        return RoomDatabaseRepositoryImpl(appDatabase)
    }
}