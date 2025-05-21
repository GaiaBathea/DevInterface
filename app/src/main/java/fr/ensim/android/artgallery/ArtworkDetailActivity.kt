package com.example.artgallery


import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.artgallery.adapter.ArtworkAdapter
import com.example.artgallery.viewmodel.ArtworkDetailViewModel

class ArtworkDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: ArtworkDetailViewModel
    private lateinit var relatedWorksAdapter: ArtworkAdapter

    private lateinit var artworkImage: ImageView
    private lateinit var titleText: TextView
    private lateinit var artistText: TextView
    private lateinit var dateText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var artistBioText: TextView
    private lateinit var relatedWorksRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artwork_detail)

        val artworkId = intent.getLongExtra(EXTRA_ARTWORK_ID, -1)
        if (artworkId == -1L) {
            finish()
            return
        }

        viewModel = ViewModelProvider(this)[ArtworkDetailViewModel::class.java]
        viewModel.loadArtworkDetails(artworkId)

        findViews()
        setupRelatedWorksRecyclerView()
        observeData()
    }

    private fun findViews() {
        artworkImage = findViewById(R.id.artwork_image)
        titleText = findViewById(R.id.artwork_title)
        artistText = findViewById(R.id.artist_name)
        dateText = findViewById(R.id.creation_date)
        descriptionText = findViewById(R.id.artwork_description)
        artistBioText = findViewById(R.id.artist_bio)
        relatedWorksRecyclerView = findViewById(R.id.related_works_recycler_view)
    }

    private fun setupRelatedWorksRecyclerView() {
        relatedWorksRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        relatedWorksAdapter = ArtworkAdapter { artwork ->
            // Just reload this activity with the new artwork ID
            val intent = intent
            intent.putExtra(EXTRA_ARTWORK_ID, artwork.id)
            finish()
            startActivity(intent)
        }
        relatedWorksRecyclerView.adapter = relatedWorksAdapter
    }

    private fun observeData() {
        viewModel.artwork.observe(this) { artwork ->
            titleText.text = artwork.title
            artistText.text = artwork.artist.name
            dateText.text = artwork.year.toString()
            descriptionText.text = artwork.description

            Glide.with(this)
                .load(artwork.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(artworkImage)
        }

        viewModel.artistBio.observe(this) { bio ->
            artistBioText.text = bio
        }

        viewModel.relatedWorks.observe(this) { relatedWorks ->
            relatedWorksAdapter.submitList(relatedWorks)
        }
    }

    companion object {
        const val EXTRA_ARTWORK_ID = "artwork_id"
    }
}