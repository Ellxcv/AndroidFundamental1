package com.example.androidfundamental1.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.androidfundamental1.R
import com.example.androidfundamental1.api.RetrofitInstance
import com.example.androidfundamental1.databinding.FragmentEventDetailBinding
import com.example.androidfundamental1.db.EventDatabase
import com.example.androidfundamental1.repository.EventRepository
import com.example.androidfundamental1.ui.EventViewModel
import com.example.androidfundamental1.ui.EventViewModelProviderFactory
import com.example.androidfundamental1.util.Status

class EventDetailFragment : Fragment(R.layout.fragment_event_detail) {
    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: EventViewModel
    private val args: EventDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bangkitAPI = RetrofitInstance.api
        val eventDatabase = EventDatabase.getDatabase(requireContext())
        val eventRepository = EventRepository(bangkitAPI, eventDatabase)
        val viewModelProviderFactory = EventViewModelProviderFactory(eventRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[EventViewModel::class.java]

        val eventId = args.eventId
        binding.paginationProgressBar.visibility = View.VISIBLE
        binding.fab.isEnabled = false

        // Ambil detail event dari ViewModel dan simpan datanya
        viewModel.getEventDetail(eventId).observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    val detailEvent = resource.data
                    detailEvent?.event?.let { event ->
                        binding.tvEventName.text = event.name
                        binding.tvEventDescription.text = Html.fromHtml(event.description, Html.FROM_HTML_MODE_LEGACY)
                        binding.tvEventDescription.movementMethod = LinkMovementMethod.getInstance()
                        binding.tvEventTimeAndLocation.text = getString(R.string.event_time_and_location_format, event.beginTime, event.endTime, event.cityName)
                        binding.tvEventQuota.text = getString(R.string.remaining_quota_format, event.quota - event.registrants)
                        Glide.with(this).load(event.mediaCover).into(binding.ivEventImage)
                        binding.tvEventOwner.text = getString(R.string.event_owner_format, event.ownerName)
                        binding.fab.isEnabled = true
                        binding.fab.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                            startActivity(intent)
                        }
                    }
                }
                Status.ERROR -> {
                    binding.paginationProgressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

