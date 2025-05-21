package fr.ensim.android.artgallery.ui.theme.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.artgallery.R
import fr.ensim.android.artgallery.ui.theme.model.Artwork

class ArtworkAdapter(private val onItemClick: (Artwork) -> Unit) :
    ListAdapter<Artwork, ArtworkAdapter.ArtworkViewHolder>(ArtworkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtworkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artwork, parent, false)
        return ArtworkViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ArtworkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ArtworkViewHolder(itemView: View, private val onItemClick: (Artwork) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val imageView: ImageView = itemView.findViewById(R.id.artwork_image)
        private val titleTextView: TextView = itemView.findViewById(R.id.artwork_title)
        private val artistTextView: TextView = itemView.findViewById(R.id.artist_name)

        fun bind(artwork: Artwork) {
            titleTextView.text = artwork.title
            artistTextView.text = artwork.artist.name

            Glide.with(itemView.context)
                .load(artwork.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(imageView)

            itemView.setOnClickListener {
                onItemClick(artwork)
            }
        }
    }

    class ArtworkDiffCallback : DiffUtil.ItemCallback<Artwork>() {
        override fun areItemsTheSame(oldItem: Artwork, newItem: Artwork): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Artwork, newItem: Artwork): Boolean {
            return oldItem == newItem
        }
    }
}