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

class EventAdapter(
    private val onItemClick: (Events) -> Unit // Lambda untuk menangani klik item
) : ListAdapter<Events, EventAdapter.EventViewHolder>(DiffCallback()) {

    // ViewHolder untuk menampilkan setiap item event
    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventLogo: ImageView = itemView.findViewById(R.id.eventLogo)
        private val eventName: TextView = itemView.findViewById(R.id.eventName)
        private val eventCategory: TextView = itemView.findViewById(R.id.eventCategory)
        private val eventDescription: TextView = itemView.findViewById(R.id.eventSummary)
        private val eventTime: TextView = itemView.findViewById(R.id.eventTime)
        private val eventCityName: TextView = itemView.findViewById(R.id.eventCityName)

        fun bind(event: Events, onItemClick: (Events) -> Unit) {
            eventName.text = event.name
            eventCategory.text = event.category
            eventDescription.text = event.summary

            // Gunakan string resource dengan placeholder untuk waktu
            eventTime.text = itemView.context.getString(R.string.event_time_format, event.beginTime, event.endTime)

            eventCityName.text = event.cityName

            // Muat logo event menggunakan Glide
            Glide.with(itemView.context)
                .load(event.imageLogo) // Menggunakan imageLogo untuk URL gambar
                .into(eventLogo)

            // Set content description for accessibility
            eventLogo.contentDescription = "${event.name} logo"

            // Set onClickListener untuk item
            itemView.setOnClickListener {
                onItemClick(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
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
