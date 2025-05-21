package fr.ensim.android.artgallery

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.artgallery.ArtworkDetailActivity
import fr.ensim.android.artgallery.ui.theme.adapter.ArtworkAdapter
import fr.ensim.android.artgallery.ui.theme.model.Artwork
import fr.ensim.android.artgallery.ui.theme.viewmodel.ArtViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var artworkAdapter: ArtworkAdapter
    private lateinit var viewModel: ArtViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[ArtViewModel::class.java]

        setupRecyclerView()
        observeArtworks()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.artwork_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        artworkAdapter = ArtworkAdapter { artwork ->
            openArtworkDetail(artwork)
        }
        recyclerView.adapter = artworkAdapter
    }

    private fun observeArtworks() {
        viewModel.artworks.observe(this) { artworks ->
            artworkAdapter.submitList(artworks)
        }
    }

    private fun openArtworkDetail(artwork: Artwork) {
        val intent = Intent(this, ArtworkDetailActivity::class.java)
        intent.putExtra(ArtworkDetailActivity.EXTRA_ARTWORK_ID, artwork.id)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    viewModel.searchArtworks(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    viewModel.loadAllArtworks()
                } else {
                    viewModel.searchArtworks(newText)
                }
                return true
            }
        })

        return true
    }
}