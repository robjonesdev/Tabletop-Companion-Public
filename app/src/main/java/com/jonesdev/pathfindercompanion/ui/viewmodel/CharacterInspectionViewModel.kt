package com.jonesdev.pathfindercompanion.ui.viewmodel

import androidx.lifecycle.*
import com.jonesdev.pathfindercompanion.dependencies.PathfinderCompanionApplication
import com.jonesdev.pathfindercompanion.data.model.Character
import com.jonesdev.pathfindercompanion.data.repository.api.GoogleSheetsDataRepositoryImpl
import com.jonesdev.pathfindercompanion.data.repository.database.RoomDatabaseRepositoryImpl
import com.jonesdev.pathfindercompanion.shared.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)
@HiltViewModel
class CharacterInspectionViewModel @Inject constructor(
    private val apiRepository: GoogleSheetsDataRepositoryImpl,
    private val dbRepository: RoomDatabaseRepositoryImpl,
    private val state: SavedStateHandle,
    private val application: PathfinderCompanionApplication
): AndroidViewModel(application) {

    private val characterName : LiveData<String> = state.getLiveData(Constants.DB_CHARACTER_NAME_KEY, "")

    fun refreshCharacterData(characterName: String, hardRefreshRequested: Boolean) {
        state[Constants.DB_CHARACTER_NAME_KEY] = characterName
        GlobalScope.launch{
            getCharacterAsync(hardRefreshRequested)
        }
    }

    fun getCharacterName(): String? {
        return characterName.value
    }

    private val character: MutableLiveData<Character> by lazy {
        MutableLiveData<Character>()
    }

    fun getCharacter(): LiveData<Character> {
        return character;
    }

    private suspend fun getCharacterAsync(isRefreshRequest: Boolean){
        if(characterName.value!=null){
            try {
                characterName.value?.let {
                    val dbCharacter: Character?
                    if(!isRefreshRequest){
                        // attempt to fetch cached character from DB
                        dbCharacter = dbRepository.getCompleteCharacterData(it)
                        // db character was in name only, had no stats, do a network pull anyway
                        if(dbCharacter.generalData.characterOwner.isBlank() || dbCharacter.generalData.characterOwner.isEmpty()){
                            val characterData: Character = apiRepository.getCompleteCharacterData(it)
                            // save this new character information to the DB
                            // TODO remove duplicate code
                            dbRepository.updateStoredCharacterData(listOf(characterData))
                            character.postValue(characterData)
                        }
                        // db character had valid data, post to livedata
                        else{
                            character.postValue(dbCharacter)
                        }
                    }
                    // no character existed in db, do a network pull
                    else{
                        val characterData: Character = apiRepository.getCompleteCharacterData(it)
                        // save this new character information to the DB
                        dbRepository.updateStoredCharacterData(listOf(characterData))
                        character.postValue(characterData)
                    }
                }
            }
            catch (e: HttpException) {
            } catch (e: IOException) {
            }
        }
    }
}