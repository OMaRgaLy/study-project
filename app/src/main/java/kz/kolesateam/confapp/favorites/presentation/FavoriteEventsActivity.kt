package kz.kolesateam.confapp.favorites.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.details.EventDetailsRouter
import kz.kolesateam.confapp.common.models.EventData
import kz.kolesateam.confapp.events.presentation.view.AllEventsClickListener
import kz.kolesateam.confapp.favorites.domain.FavoriteEventActionObservable
import kz.kolesateam.confapp.common.UpcomingEventsRouter
import kz.kolesateam.confapp.favorites.presentation.view.FavoriteEventsAdapter
import org.koin.android.ext.android.inject

class FavoriteEventsActivity : AppCompatActivity(), AllEventsClickListener {
    private val favoriteEventsViewModel: FavoriteEventsViewModel by inject()
    private val favoriteEventActionObservable: FavoriteEventActionObservable by inject()
    private lateinit var recyclerView: RecyclerView
    private lateinit var goHomeButton: Button
    private lateinit var containerForEmptyActivity: View
    private val favoriteEventsAdapter = FavoriteEventsAdapter(
        favoriteEventsAdapterListener = this,
        favoriteEventActionObservable = favoriteEventActionObservable
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_events)
        initViews()
        observeFavoriteEventsLiveData()
    }

    override fun onStart() {
        super.onStart()
        favoriteEventsViewModel.onStart()
    }
    private fun initViews() {
        recyclerView = findViewById(R.id.activity_favorite_events_recycler_view)
        recyclerView.apply {
            this.adapter = this@FavoriteEventsActivity.favoriteEventsAdapter
        }
        goHomeButton = findViewById(R.id.activity_favorite_events_button_to_home)
        containerForEmptyActivity = findViewById(R.id.activity_favorite_events_content_for_empty_activity)
        setOnClickListeners()
    }

    private fun observeFavoriteEventsLiveData() {
        favoriteEventsViewModel.getAllFavoriteEventsLiveData().observe(this, ::showResult)
    }

    private fun showResult(allEventsList: List<EventData>) {
        if(allEventsList.isNotEmpty()) {
            containerForEmptyActivity.visibility = View.INVISIBLE
        } else {
            containerForEmptyActivity.visibility = View.VISIBLE
        }
        favoriteEventsAdapter.setList(allEventsList)
    }

    private fun setOnClickListeners() {
        goHomeButton.setOnClickListener{
            startActivity(UpcomingEventsRouter().createIntent(this))
            finish()
        }
    }

    override fun onEventClick(eventData: EventData) {
        startActivity(EventDetailsRouter().createIntentForEventDetails(this, eventData.id))
    }

    override fun onFavoritesClicked(eventData: EventData) {
        favoriteEventsViewModel.onFavoriteClick(eventData)
    }
}