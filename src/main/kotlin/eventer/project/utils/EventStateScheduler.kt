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

class EventStateScheduler() {

    private var eventService = EventService()

    private val scheduler = Scheduler(task = {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching { checkAndUpdateEventStates() }
                .onFailure { it.printStackTrace() }
        }
    })

    fun startScheduler() {
        scheduler.scheduleExecution(Every(1, TimeUnit.HOURS))
    }

    fun stopScheduler() {
        scheduler.stop()
    }

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