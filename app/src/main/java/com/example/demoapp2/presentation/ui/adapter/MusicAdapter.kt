package com.example.demoapp2.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demoapp2.R
import com.example.demoapp2.databinding.MusicItemBinding
import com.example.demoapp2.domain.data.MusicListResponse

class MusicAdapter(private val item: ArrayList<MusicListResponse>) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    private var isPlaying = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MusicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val musicItem = item[position]
        holder.bind(musicItem, onClickListener, position)
    }

    override fun getItemCount(): Int = item.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(private val binding: MusicItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(musicItem: MusicListResponse, onClickListener: OnClickListener?, position: Int) {
            binding.apply {
                musicNameTextView.text = musicItem.trackName
                artisNameTextView.text = musicItem.artistName
                Glide.with(itemView.context)
                    .load(musicItem.artworkUrl100)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(musicImageView)

                itemView.setOnClickListener {
                    onClickListener?.onClick(
                        position = position,
                        item = musicItem
                    )
                }

                if (musicItem.isPlaying) {
                    playerImageView.isVisible = true
                    playerImageView.setImageResource(R.drawable.ic_icon_pause)
                    mainLayout.setBackgroundResource(R.color.almost_black)
                } else {
                    playerImageView.isVisible = false
                    playerImageView.setImageResource(R.drawable.ic_icon_play)
                    mainLayout.setBackgroundResource(R.color.black)
                }
            }
        }
    }

    fun updateView(isPlaying: Boolean) {
        this.isPlaying = isPlaying
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, item: MusicListResponse)
    }
}