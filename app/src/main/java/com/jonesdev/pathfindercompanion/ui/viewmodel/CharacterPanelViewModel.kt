package com.jonesdev.pathfindercompanion.ui.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jonesdev.pathfindercompanion.dependencies.PathfinderCompanionApplication
import com.jonesdev.pathfindercompanion.data.model.Character
import com.jonesdev.pathfindercompanion.data.repository.api.GoogleSheetsDataRepositoryImpl
import com.jonesdev.pathfindercompanion.data.repository.database.RoomDatabaseRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)
@HiltViewModel
class CharacterPanelViewModel @Inject constructor(
    private val apiRepository: GoogleSheetsDataRepositoryImpl,
    private val dbRepository: RoomDatabaseRepositoryImpl,
    private val application: PathfinderCompanionApplication
): AndroidViewModel(application) {

    private val characters: MutableLiveData<List<Character>> by lazy {
        MutableLiveData<List<Character>>().also {
            GlobalScope.launch{
                getCharactersAsync()
            }
        }
    }

    fun getCharacters(): LiveData<List<Character>> {
        return characters;
    }

    private suspend fun getCharactersAsync(){
        try {
            // attempt to fetch cached characters from DB
            val dbCharList = dbRepository.getAllCompleteCharacterData()
            // if we had no db data, do a network fetch instead
            if(dbCharList.isEmpty()){
                val characterNames: List<String> = apiRepository.getCharacterNames()
                val characterImageUrls: List <String> = apiRepository.getCharacterImageUrls(characterNames)
                val characterDataList: MutableList<Character> = mutableListOf()
                for(i in characterNames.indices){
                    characterDataList.add(Character(characterNames[i]))
                    characterDataList[i].image = characterImageUrls[i]
                }
                // persist newly found blank characters to the database
                dbRepository.updateStoredCharacterData(characterDataList.toList())
                characters.postValue(characterDataList.toList())
            }
            // we had cached data, just use that and refresh only if asked
            else{
                characters.postValue(dbCharList)
            }
        } catch (e: HttpException) {
        } catch (e: IOException) {
        }
    }
}