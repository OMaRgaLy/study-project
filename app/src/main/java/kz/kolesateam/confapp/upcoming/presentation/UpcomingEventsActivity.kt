package kz.kolesateam.confapp.upcoming.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kz.kolesateam.confapp.R
import kz.kolesateam.confapp.details.EventDetailsRouter
import kz.kolesateam.confapp.common.models.BranchData
import kz.kolesateam.confapp.common.models.EventData
import kz.kolesateam.confapp.favorites.presentation.FavoriteEventsActivity
import kz.kolesateam.confapp.favorites.domain.FavoriteEventActionObservable
import kz.kolesateam.confapp.upcoming.presentation.view.UpcomingEventsAdapter
import kz.kolesateam.confapp.upcoming.presentation.models.UpcomingEventsListItem
import kz.kolesateam.confapp.common.AllEventsRouter
import kz.kolesateam.confapp.common.utils.ProgressState
import kz.kolesateam.confapp.upcoming.presentation.view.UpcomingEventsClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

const val BRANCH_ID = "branch_id"
const val BRANCH_TITLE = "branch_title"

class UpcomingEventsActivity : AppCompatActivity(), UpcomingEventsClickListener {

    private val upcomingEventsViewModel: UpcomingEventsViewModel by viewModel()
    private val favoriteEventActionObservable: FavoriteEventActionObservable by inject()
    private val upcomingEventsAdapter = UpcomingEventsAdapter(
        upcomingEventsAdapterListener = this,
        favoriteEventActionObservable = favoriteEventActionObservable
    )

    private lateinit var upcomingEventsProgressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var buttonToFavorites: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_events)
        initViews()
        observeUpcomingEventsLiveData()
        upcomingEventsViewModel.onStart()
    }

    private fun observeUpcomingEventsLiveData() {
        upcomingEventsViewModel.getProgressLiveData().observe(this, ::handleProgressBarState)
        upcomingEventsViewModel.getUpcomingEventsLiveData().observe(this, ::showResult)
        upcomingEventsViewModel.getErrorLiveData().observe(this, ::showError)
    }

    private fun initViews() {
        upcomingEventsProgressBar = findViewById(R.id.upcoming_events_activity_progress_bar)

        recyclerView = findViewById(R.id.activity_upcoming_events_recycler_view)
        recyclerView.apply {
            this.adapter = this@UpcomingEventsActivity.upcomingEventsAdapter
        }

        buttonToFavorites = findViewById(R.id.bottom_button_to_favorites)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        buttonToFavorites.setOnClickListener {
            val intent = Intent(this, FavoriteEventsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleProgressBarState(
        progressState: ProgressState,
    ) {
        upcomingEventsProgressBar.isVisible = progressState is ProgressState.Loading
    }

    private fun showError(errorMessage: Exception) {
        Toast.makeText(this, errorMessage.localizedMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showResult(upcomingEventsList: List<UpcomingEventsListItem>) {
        upcomingEventsAdapter.setList(upcomingEventsList)
    }

    override fun onBranchClick(branchData: BranchData) {
        val intent = AllEventsRouter(this@UpcomingEventsActivity).createIntent(branchData)
        startActivity(intent)
    }

    override fun onEventClick(eventData: EventData) {
        startActivity(EventDetailsRouter().createIntentForEventDetails(this, eventData.id))
    }

    override fun onFavoritesClicked(eventData: EventData) {
        upcomingEventsViewModel.onFavoriteClick(eventData)
    }
}