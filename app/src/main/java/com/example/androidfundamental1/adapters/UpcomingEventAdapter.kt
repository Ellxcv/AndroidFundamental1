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

class UpcomingEventAdapter(
    private val onItemClick: (Events) -> Unit // Lambda untuk menangani klik item
) : ListAdapter<Events, UpcomingEventAdapter.UpcomingEventViewHolder>(DiffCallback()) {

    // ViewHolder untuk menampilkan setiap item event
    class UpcomingEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageEvent: ImageView = itemView.findViewById(R.id.imageEvent)
        private val titleEvent: TextView = itemView.findViewById(R.id.titleEvent)

        fun bind(event: Events, onItemClick: (Events) -> Unit) {
            titleEvent.text = event.name

            // Muat gambar event menggunakan Glide
            Glide.with(itemView.context)
                .load(event.imageLogo) // Menggunakan imageLogo untuk URL gambar
                .into(imageEvent)

            // Set content description for accessibility
            imageEvent.contentDescription = "${event.name} gambar"

            // Set onClickListener untuk item
            itemView.setOnClickListener {
                onItemClick(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingEventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_upcoming_event, parent, false)
        return UpcomingEventViewHolder(view)
    }

    override fun onBindViewHolder(holder: UpcomingEventViewHolder, position: Int) {
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
