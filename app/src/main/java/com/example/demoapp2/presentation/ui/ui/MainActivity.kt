package com.example.demoapp2.presentation.ui.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoapp2.R
import com.example.demoapp2.common.ApiStatus
import com.example.demoapp2.common.Constants.SEARCH_DEFAULT
import com.example.demoapp2.common.Results
import com.example.demoapp2.databinding.ActivityMainBinding
import com.example.demoapp2.domain.data.MusicListResponse
import com.example.demoapp2.domain.data.MusicResponse
import com.example.demoapp2.presentation.ui.adapter.MusicAdapter
import com.example.demoapp2.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var searchStr = SEARCH_DEFAULT
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var musicAdapter: MusicAdapter
    private var oldPlayer = ""
    private var oldPlayerPosition = -1
    private var absoluteAdapterPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        observeData()
        setUpUI()
        mediaPlayer = MediaPlayer()
    }

    private fun initData() {
        viewModel.searchMusic(searchStr)
    }

    private fun observeData() {
        viewModel.searchMusicLiveData.observe(this) { response ->
            binding.apply {
                when (response.status) {
                    ApiStatus.LOADING -> {
                        loadMusicProgressBar.isVisible = true
                    }
                    ApiStatus.SUCCESS -> {
                        loadMusicProgressBar.isVisible = false
                        setUpItemView(response)
                    }
                    ApiStatus.ERROR -> {
                        loadMusicProgressBar.isVisible = false
                    }
                }
            }
        }

        viewModel.pairLiveData.observe(this) {
            musicAdapter.updateView(it.second)
            musicAdapter.notifyItemChanged(absoluteAdapterPosition)
        }
    }

    private fun setUpItemView(response: Results<MusicResponse>) {
        response.data?.musicListResponse?.let { musicList ->
            musicAdapter = MusicAdapter(musicList)

            binding.apply {
                recyclerView.apply {
                    setHasFixedSize(true)
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = musicAdapter
                }
            }

            musicAdapter.setOnClickListener(object : MusicAdapter.OnClickListener {
                override fun onClick(position: Int, item: MusicListResponse) {
                    Toast.makeText(
                        this@MainActivity, "${item.trackName} ${
                            this@MainActivity.resources.getString(
                                R.string.music_is_playing
                            )
                        }", Toast.LENGTH_SHORT
                    ).show()

                    playAudio(item.previewUrl, position)
                }
            })
        }
    }

    private fun setUpUI() {
        binding.apply {
            searchImageView.setOnClickListener {
                searchMusicEditText.isVisible = !searchMusicEditText.isVisible
            }

            searchMusicEditText.setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    this@MainActivity.let { context ->
                        val imm = context.getSystemService(
                            INPUT_METHOD_SERVICE
                        ) as InputMethodManager?
                        imm?.hideSoftInputFromWindow(textView.windowToken, 0)
                    }
                    searchMusicEditText.isVisible = false
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            searchMusicEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(str: Editable?) {
                    val searchText = str.toString()
                    searchStr = searchText
                    /**In Case Default Music Data**/
                    if (searchText.isEmpty()) viewModel.searchMusic(SEARCH_DEFAULT)
                    else viewModel.searchMusic(
                        keyWord = searchText, limit = 50
                    )
                }

                override fun beforeTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // do something
                }

                override fun onTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // do something
                }
            })
        }
    }

    //move to viewModel
    private fun playAudio(previewUrl: String, position: Int) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.reset()
            if (oldPlayer != previewUrl) {
                updateAdapter(isPlaying = false, position = oldPlayerPosition)
                startAudio(position, previewUrl)
            }
        } else {
            try {
                oldPlayer = previewUrl
                startAudio(position, previewUrl)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun startAudio(position: Int, previewUrl: String) {
        oldPlayerPosition = position
        mediaPlayer.apply {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener { mp ->
                mp.start()
                updateAdapter(isPlaying = true, position = position)
            }
            setOnCompletionListener {
                mediaPlayer.reset()
                updateAdapter(isPlaying = false, position = position)
            }
        }
    }

    private fun updateAdapter(isPlaying: Boolean, position: Int) {
        absoluteAdapterPosition = position
        viewModel.updateMusic(isPlaying, position)
    }
}