package com.example.androidfundamental1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidfundamental1.R
import com.example.androidfundamental1.models.Events

class PastEventAdapter(
    private val onItemClick: (Events) -> Unit // Lambda untuk menangani klik item
) : ListAdapter<Events, PastEventAdapter.PastEventViewHolder>(DiffCallback()) {

    // ViewHolder untuk menampilkan setiap item event
    class PastEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imagePastEvent: ImageView = itemView.findViewById(R.id.imagePastEvent)
        private val titlePastEvent: TextView = itemView.findViewById(R.id.titlePastEvent)
        private val summaryPastEvent: TextView = itemView.findViewById(R.id.summaryPastEvent)

        fun bind(event: Events, onItemClick: (Events) -> Unit) {
            titlePastEvent.text = event.name
            summaryPastEvent.text = event.summary

            // Muat gambar event menggunakan Glide
            Glide.with(itemView.context)
                .load(event.imageLogo) // Menggunakan imageLogo untuk URL gambar
                .into(imagePastEvent)

            // Set content description for accessibility
            imagePastEvent.contentDescription = "${event.name} gambar"

            // Set onClickListener untuk item
            itemView.setOnClickListener {
                onItemClick(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastEventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_past_event, parent, false)
        return PastEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: PastEventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onItemClick)
    }

    // DiffCallback untuk membandingkan item secara efisien
    class DiffCallback : DiffUtil.ItemCallback<Events>() {
        override fun areItemsTheSame(oldItem: Events, newItem: Events): Boolean {
            // Periksa apakah item memiliki ID yang sama
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Events, newItem: Events): Boolean {
            // Periksa apakah konten item sama
            return oldItem == newItem
        }
    }
}
