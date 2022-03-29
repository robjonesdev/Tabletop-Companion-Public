package com.jonesdev.pathfindercompanion.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jonesdev.pathfindercompanion.databinding.ActivityCharacterPaneBinding

import com.jonesdev.pathfindercompanion.ui.viewmodel.CharacterPanelViewModel
import com.jonesdev.pathfindercompanion.data.model.Character
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class CharacterPaneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterPaneBinding
    val viewModel by viewModels<CharacterPanelViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterPaneBinding.inflate(layoutInflater)
        binding.loadingCharactersProgressBar.visibility = View.VISIBLE
        setContentView(binding.root)

        viewModel.getCharacters().observe(this) { characters ->
            loadCharactersIntoView(characters)
        }
    }

    private fun loadCharactersIntoView(characterList: List<Character>){
        binding.loadingCharactersProgressBar.visibility = View.GONE
        val adapter = CharacterPaneViewpagerAdapter(characterList)
        binding.characterPaneViewpager.adapter = adapter
        binding.characterPaneViewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        TabLayoutMediator(binding.characterPanelTabs, binding.characterPaneViewpager) { tab, position ->
            tab.text = characterList[position].characterName
        }.attach()
    }
}