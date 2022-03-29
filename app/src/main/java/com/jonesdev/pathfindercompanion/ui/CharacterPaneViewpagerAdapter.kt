package com.jonesdev.pathfindercompanion.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jonesdev.pathfindercompanion.R
import com.google.android.material.imageview.ShapeableImageView
import com.jonesdev.pathfindercompanion.data.model.Character

/*
    This viewpager adapter is used to fill a recycler view of character panels
    in the main activity of the application.
 */
class CharacterPaneViewpagerAdapter(private val characterData: List<Character>) : RecyclerView.Adapter<CharacterPaneViewpagerAdapter.ViewPagerViewHolder>(){

    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var characterImage: ImageView = itemView.findViewById(R.id.characterPanelImage)
        var characterTitle: TextView = itemView.findViewById(R.id.characterText)
        var characterShine: ShapeableImageView = itemView.findViewById(R.id.characterPanelShine)
    }

    lateinit var parentContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.character_pane_viewpager, parent, false)
        parentContext = parent.context;
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val currentImage = characterData[position].image
        Glide.with(parentContext).load(currentImage).into(holder.characterImage)
        holder.characterTitle.text = characterData[position].characterName
        holder.characterShine.setOnClickListener {
            val intent = Intent(parentContext, CharacterInspectionComposeActivity::class.java)
            intent.putExtra(R.string.character_pane_data.toString(), characterData[position])
            parentContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return characterData.size
    }
}