package eventer.project.utils

import eventer.project.app.models.objects.EventState
import eventer.project.app.services.EventService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.concurrent.TimeUnit

class EventStateScheduler(private val eventService: EventService) {

    private val scheduler = Scheduler(task = {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching { checkAndUpdateEventStates() }
                .onFailure { it.printStackTrace() }
        }
    })

    /**
     * Starts the scheduler to periodically check and update event states.
     * The task runs every 1 hour.
     */
    fun startScheduler() {
        scheduler.scheduleExecution(Every(1, TimeUnit.HOURS))
    }

    /**
     * Stops the scheduler.
     */
    fun stopScheduler() {
        scheduler.stop()
    }

    /**
     * Logic for checking and updating the states of events based on their start and end dates.
     */
    private suspend fun checkAndUpdateEventStates() {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val events = eventService.getAllEventsList()

        events.forEach { event ->
            val newState = when {
                event.startDate != null && today.date < event.startDate -> EventState.Upcoming
                event.endDate != null && today.date > event.endDate -> EventState.Elapsed
                else -> EventState.InProgress
            }

            if (event.state != newState) {
                eventService.updateEvent(event = event.copy(state = newState))
            }
        }
    }
}