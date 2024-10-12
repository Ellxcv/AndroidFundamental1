package com.example.androidfundamental1.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidfundamental1.R
import com.example.androidfundamental1.adapters.EventAdapter
import com.example.androidfundamental1.api.RetrofitInstance
import com.example.androidfundamental1.databinding.FragmentPastEventsBinding
import com.example.androidfundamental1.db.EventDatabase
import com.example.androidfundamental1.repository.EventRepository
import com.example.androidfundamental1.ui.EventViewModel
import com.example.androidfundamental1.ui.EventViewModelProviderFactory
import com.example.androidfundamental1.util.Status

class PastEventsFragment : Fragment(R.layout.fragment_past_events) {
    private var _binding: FragmentPastEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EventViewModel
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPastEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tampilkan ikon "Up" di fragment Past Events
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.past_events)

        // Initialize ViewModel with Factory
        val eventRepository = EventRepository(
            bangkitAPI = RetrofitInstance.api,
            eventDatabase = EventDatabase.getDatabase(requireContext())
        )
        val factory = EventViewModelProviderFactory(eventRepository)
        viewModel = ViewModelProvider(this, factory)[EventViewModel::class.java]

        // Setup RecyclerView
        setupRecyclerView()

        // Inisialisasi view itemError
        val errorLayout = binding.itemError.root
        errorLayout.visibility = View.GONE // Sembunyikan error layout awalnya

        // Observe past events from ViewModel
        viewModel.pastEventsLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE
                    binding.rvPastEvents.visibility = View.GONE // Sembunyikan RecyclerView saat loading
                    errorLayout.visibility = View.GONE // Sembunyikan pesan error jika ada
                }
                Status.SUCCESS -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    binding.rvPastEvents.visibility = View.VISIBLE // Tampilkan RecyclerView
                    resource.data?.let { events ->
                        eventAdapter.submitList(events)
                    }
                }
                Status.ERROR -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE // Tampilkan pesan error
                    binding.itemError.errorMessage.text = resource.message ?: "Unknown Error"
                }
            }
        }

        // Retry button click listener
        binding.itemError.retryButton.setOnClickListener {
            viewModel.retryFetchEvents() // Atur ulang operasi fetch
        }

        // Search functionality
        // Setup listener untuk klik pada drawable
        binding.searchEdit.setOnClickListener {
            val query = binding.searchEdit.text.toString().trim()
            if (query.isNotEmpty()) {
                viewModel.searchPastEvents(query)
                binding.searchEdit.clearFocus() // Menghilangkan fokus dari EditText setelah pencarian
            } else {
                Toast.makeText(requireContext(), "Please enter a search term", Toast.LENGTH_SHORT).show()
            }
        }

        binding.searchEdit.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = 2 // drawableEnd adalah 2, drawableStart adalah 0
                if (event.rawX >= (binding.searchEdit.right - binding.searchEdit.compoundDrawables[drawableEnd].bounds.width())) {
                    // Tangani klik pada drawableEnd
                    v.performClick() // Panggil performClick untuk memperbaiki warning
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Pencarian event saat tombol "Search" di keyboard ditekan
        binding.searchEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchEdit.text.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.searchPastEvents(query) // Panggil fungsi pencarian
                    binding.searchEdit.clearFocus() // Menghilangkan fokus dari EditText setelah pencarian
                }
                true
            } else {
                false
            }
        }
    }

    private fun setupRecyclerView() {
        eventAdapter = EventAdapter { event ->
            val action = PastEventsFragmentDirections.actionPastEventsFragmentToEventDetailFragment(event.id)
            findNavController().navigate(action)
        }

        binding.rvPastEvents.apply {
            adapter = eventAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}