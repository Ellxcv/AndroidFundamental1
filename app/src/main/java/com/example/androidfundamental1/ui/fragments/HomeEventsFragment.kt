package com.example.androidfundamental1.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidfundamental1.R
import com.example.androidfundamental1.adapters.UpcomingEventAdapter
import com.example.androidfundamental1.adapters.PastEventAdapter
import com.example.androidfundamental1.api.RetrofitInstance
import com.example.androidfundamental1.databinding.FragmentHomeEventsBinding
import com.example.androidfundamental1.databinding.ItemErrorBinding
import com.example.androidfundamental1.db.EventDatabase
import com.example.androidfundamental1.repository.EventRepository
import com.example.androidfundamental1.ui.EventViewModel
import com.example.androidfundamental1.ui.EventViewModelProviderFactory
import com.example.androidfundamental1.util.Status

class HomeEventsFragment : Fragment(R.layout.fragment_home_events) {
    private var _binding: FragmentHomeEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EventViewModel
    private lateinit var upcomingEventAdapter: UpcomingEventAdapter
    private lateinit var pastEventAdapter: PastEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi view itemError
        val errorLayout = binding.itemError.root
        val errorBinding = ItemErrorBinding.bind(binding.itemError.root)

        // Sembunyikan ikon "Up" di fragment Home
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.title = "Home"

        // Initialize ViewModel with Factory
        val eventRepository = EventRepository(
            bangkitAPI = RetrofitInstance.api,
            eventDatabase = EventDatabase.getDatabase(requireContext())
        )
        val factory = EventViewModelProviderFactory(eventRepository)
        viewModel = ViewModelProvider(this, factory)[EventViewModel::class.java]

        // Setup RecyclerViews
        setupUpcomingRecyclerView()
        setupPastRecyclerView()

        // Observe upcoming events from ViewModel
        viewModel.upcomingEventsLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE // Sembunyikan error layout saat loading
                    errorBinding.root.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    errorLayout.visibility = View.GONE // Sembunyikan error layout jika sukses
                    resource.data?.let { events ->
                        upcomingEventAdapter.submitList(events)
                    }
                }
                Status.ERROR -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE // Tampilkan error layout jika ada error
                    errorBinding.root.visibility = View.VISIBLE
                    binding.itemError.errorMessage.text = resource.message ?: "Unknown Error"
                    errorBinding.errorMessage.text = "Error message here"
                }
            }
        }

        // Observe past events from ViewModel
        viewModel.pastEventsLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE // Sembunyikan error layout saat loading
                }
                Status.SUCCESS -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    errorLayout.visibility = View.GONE // Sembunyikan error layout jika sukses
                    resource.data?.let { events ->
                        pastEventAdapter.submitList(events)
                    }
                }
                Status.ERROR -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE // Tampilkan error layout jika ada error
                    binding.itemError.errorMessage.text = resource.message ?: "Unknown Error"
                }
            }
        }

        // Aksi untuk tombol Retry
        binding.itemError.root.findViewById<Button>(R.id.retryButton).setOnClickListener {
            viewModel.retryFetchEvents() // Atur ulang operasi fetch
        }
    }

    private fun setupUpcomingRecyclerView() {
        upcomingEventAdapter = UpcomingEventAdapter { event ->
            val action = HomeEventsFragmentDirections.actionHomeEventsFragmentToEventDetailFragment(event.id)
            findNavController().navigate(action)
        }

        binding.rvUpcomingEvents.apply {
            adapter = upcomingEventAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupPastRecyclerView() {
        pastEventAdapter = PastEventAdapter { event ->
            val action = HomeEventsFragmentDirections.actionHomeEventsFragmentToEventDetailFragment(event.id)
            findNavController().navigate(action)
        }

        binding.rvPastEvents.apply {
            adapter = pastEventAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
