package com.example.androidfundamental1.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidfundamental1.models.DetailEvent
import com.example.androidfundamental1.models.Events
import com.example.androidfundamental1.repository.EventRepository
import com.example.androidfundamental1.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    // LiveData untuk menyimpan daftar upcoming events
    private val _upcomingEventsLiveData = MutableLiveData<Resource<List<Events>>>()
    val upcomingEventsLiveData: LiveData<Resource<List<Events>>> get() = _upcomingEventsLiveData

    // LiveData untuk menyimpan daftar past events
    private val _pastEventsLiveData = MutableLiveData<Resource<List<Events>>>()
    val pastEventsLiveData: LiveData<Resource<List<Events>>> get() = _pastEventsLiveData

    // LiveData untuk menyimpan detail event
    private val _eventDetailLiveData = MutableLiveData<Resource<DetailEvent>>()
    private val eventDetailLiveData: LiveData<Resource<DetailEvent>> get() = _eventDetailLiveData

    init {
        fetchUpcomingEvents() // Ambil upcoming events
        fetchPastEvents() // Ambil past events
    }

    // Fungsi untuk mengambil upcoming events
    fun fetchUpcomingEvents() {
        viewModelScope.launch {
            _upcomingEventsLiveData.value = Resource.loading() // Set status loading
            delay(1000)
            try {
                val events = eventRepository.getUpcomingEvents() // Ambil data dari repository
                if (events != null) {
                    _upcomingEventsLiveData.value = Resource.success(events.listEvents) // Set status success
                } else {
                    _upcomingEventsLiveData.value = Resource.error("No upcoming events found") // Set status error
                }
            } catch (e: Exception) {
                _upcomingEventsLiveData.value = Resource.error("Failed to fetch upcoming events: ${e.message}") // Set status error
            }
        }
    }

    // Fungsi untuk mengambil past events
    private fun fetchPastEvents() {
        viewModelScope.launch {
            _pastEventsLiveData.value = Resource.loading() // Set status loading
            delay(1000)
            try {
                val events = eventRepository.getPastEvents() // Ambil data dari repository
                if (events != null) {
                    Log.d("EventViewModel", "Past events fetched: ${events.listEvents}")
                    _pastEventsLiveData.value = Resource.success(events.listEvents) // Set status success
                } else {
                    _pastEventsLiveData.value = Resource.error("No past events found") // Set status error
                }
            } catch (e: Exception) {
                _pastEventsLiveData.value = Resource.error("Failed to fetch past events: ${e.message}") // Set status error
                Log.e("EventViewModel", "Error fetching past events: ${e.message}")
            }
        }
    }

    // Fungsi untuk mencari past events berdasarkan keyword
    fun searchPastEvents(query: String) {
        viewModelScope.launch {
            _pastEventsLiveData.value = Resource.loading() // Set status loading
            delay(1000)
            try {
                val events = eventRepository.searchPastEvents(query) // Ambil data dari repository
                if (events != null) {
                    _pastEventsLiveData.value = Resource.success(events.listEvents) // Set status success
                } else {
                    _pastEventsLiveData.value = Resource.error("No past events found for query: $query") // Set status error
                }
            } catch (e: Exception) {
                _pastEventsLiveData.value = Resource.error("Failed to search past events: ${e.message}") // Set status error
            }
        }
    }

    // Fungsi untuk mengambil detail event berdasarkan ID
    fun getEventDetail(eventId: Int): LiveData<Resource<DetailEvent>> {
        viewModelScope.launch {
            _eventDetailLiveData.value = Resource.loading() // Set status loading
            try {
                val detailEvent = eventRepository.getEventDetail(eventId)
                if (detailEvent != null) {
                    _eventDetailLiveData.value = Resource.success(detailEvent)
                } else {
                    _eventDetailLiveData.value = Resource.error("Event detail not found")
                }
            } catch (e: Exception) {
                _eventDetailLiveData.value = Resource.error("Failed to fetch event detail: ${e.message}")
            }
        }
        return eventDetailLiveData
    }

    fun retryFetchEvents() {
        fetchUpcomingEvents()
        fetchPastEvents()
    }
}
