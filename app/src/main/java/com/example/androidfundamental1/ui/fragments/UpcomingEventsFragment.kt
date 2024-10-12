package com.example.androidfundamental1.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidfundamental1.R
import com.example.androidfundamental1.adapters.EventAdapter
import com.example.androidfundamental1.api.RetrofitInstance
import com.example.androidfundamental1.databinding.FragmentUpcomingEventsBinding
import com.example.androidfundamental1.db.EventDatabase
import com.example.androidfundamental1.repository.EventRepository
import com.example.androidfundamental1.ui.EventViewModel
import com.example.androidfundamental1.ui.EventViewModelProviderFactory
import com.example.androidfundamental1.util.Status

class UpcomingEventsFragment : Fragment(R.layout.fragment_upcoming_events) {
    private var _binding: FragmentUpcomingEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EventViewModel
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up ActionBar
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.upcoming_events)
        }

        // Initialize API and Database
        val bangkitAPI = RetrofitInstance.api
        val eventDatabase = EventDatabase.getDatabase(requireContext())

        // Initialize Repository and ViewModel
        val eventRepository = EventRepository(bangkitAPI, eventDatabase)
        val viewModelProviderFactory = EventViewModelProviderFactory(eventRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[EventViewModel::class.java]

        // Setup RecyclerView
        setupRecyclerView()

        // Inisialisasi view itemError
        val errorLayout = binding.itemError.root
        errorLayout.visibility = View.GONE // Sembunyikan error layout awalnya

        // Observe upcoming events
        viewModel.upcomingEventsLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let { events ->
                        eventAdapter.submitList(events)
                    }
                    binding.paginationProgressBar.visibility = View.GONE
                    errorLayout.visibility = View.GONE // Sembunyikan error layout saat data berhasil dimuat
                    binding.rvUpcomingEvents.visibility = View.VISIBLE // Tampilkan RecyclerView
                }
                Status.ERROR -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE // Tampilkan pesan error
                    binding.rvUpcomingEvents.visibility = View.GONE // Sembunyikan RecyclerView jika ada error
                    binding.itemError.errorMessage.text = resource.message ?: "Unknown Error"
                }
                Status.LOADING -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE
                    binding.rvUpcomingEvents.visibility = View.GONE // Sembunyikan RecyclerView saat loading
                    errorLayout.visibility = View.GONE // Sembunyikan pesan error saat loading
                }
            }
        }

        // Retry button click listener
        binding.itemError.retryButton.setOnClickListener {
            viewModel.retryFetchEvents() // Coba fetch lagi data dari API
        }

        // Fetch upcoming events
        viewModel.fetchUpcomingEvents()
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter { event ->
            val action = UpcomingEventsFragmentDirections.actionToEventDetail(event.id)
            findNavController().navigate(action)
        }

        binding.rvUpcomingEvents.apply {
            adapter = eventAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
