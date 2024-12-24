package eventer.project.utils

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Scheduler(private val task: Runnable) {
    private val executor = Executors.newScheduledThreadPool(1)

    /**
     * Plans execution of the task in periodical intervals
     */
    fun scheduleExecution(every: Every) {
        val taskWrapper = Runnable {
            task.run()
        }
        executor.scheduleWithFixedDelay(taskWrapper, every.n, every.n, every.unit)
    }

    /**
     * Stops the scheduler and waits for running tasks to complete.
     */
    fun stop() {
        executor.shutdown()
        try {
            executor.awaitTermination(1, TimeUnit.HOURS)
        } catch (e: InterruptedException) {
        }
    }
}

data class Every(val n: Long, val unit: TimeUnit)