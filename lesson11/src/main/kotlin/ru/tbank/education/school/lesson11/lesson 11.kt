package ru.tbank.education.school.lesson11

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.math.BigInteger
import java.net.URL
import java.time.Duration
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

// Задание 1
object CreateThreads {
    fun run(): List<Thread> {
        val threads = listOf("Thread-A", "Thread-B", "Thread-C").map {
            Thread {
                println(Thread.currentThread().name)
                repeat(5) {
                    println("Hello from: $it")
                    Thread.sleep(500)
                }
            }.apply { this.name = name }
        }
        threads.forEach { it.start() }
        threads.forEach { it.join() }
        return threads
    }
}

// Задание 2 + 3
object RaceCondition {
    fun run(): Int {
        // val = AtomicInteger(0)
        var counter = 0
        val lock = Any()
        val threads = (1..10).map {
            Thread {
                /**
                 *  repeat(1000){
                 *      counter.incrementAndGet()
                 *  }
                 */
                synchronized(lock) {
                    repeat(1000) { counter++ }
                }
            }
        }
        threads.forEach { it.start() }
        threads.forEach { it.join() }
        return counter
    }
}

// Задание 4
object Deadlock {
    fun runDeadlock() {
        val lockA = Any()
        val lockB = Any()

        val threadA = Thread {
            synchronized(lockA) {
                println("Processing A")
                Thread.sleep(100)
                synchronized(lockB) {
                    println("Processing B")
                }
            }
            println("Done")
        }

        val threadB = Thread {
            synchronized(lockB) {
                println("Processing B")
                Thread.sleep(100)
                synchronized(lockA) {
                    println("Processing A")
                }
            }
            println("Done")
        }

        threadA.start()
        threadB.start()

        threadA.join()
        threadB.join()
    }

    fun runFixed(): Boolean {
        val lockA = Any()
        val lockB = Any()

        val threadA = Thread {
            synchronized(lockA) {
                println("Thread A: acquired lockA")
                Thread.sleep(100)
                println("Thread A: waiting for lockB")
                synchronized(lockB) {
                    println("Thread A: acquired lockB")
                }
            }
            println("Thread A: Done")
        }

        val threadB = Thread {
            synchronized(lockA) {
                println("Thread B: acquired lockA")
                Thread.sleep(100)
                println("Thread B: waiting for lockB")
                synchronized(lockB) {
                    println("Thread B: acquired lockB")
                }
            }
            println("Thread B: Done")
        }

        threadA.start()
        threadB.start()

        threadA.join()
        threadB.join()

        return true
    }
}

// Задание 5
object ExecutorServiceExample {
    fun run(): List<String> {
        val executor = Executors.newFixedThreadPool(4)
        val results = mutableListOf<String>()

        repeat(20) { index ->
            executor.submit {
                val threadName = Thread.currentThread().name
                val message = "Task $index executed by $threadName"
                println(message)

                synchronized(results) {
                    results.add(message)
                }

                Thread.sleep(200)
            }
        }

        executor.shutdown()
        executor.awaitTermination(1, TimeUnit.MINUTES)

        return results
    }
}

// Задание 6
object FutureFactorial {
    fun run(): Map<Int, BigInteger> {
        val executor = Executors.newFixedThreadPool(4)
        val futures = mutableMapOf<Int, Future<BigInteger>>()

        for (i in 1..10) {
            futures[i] = executor.submit<BigInteger> {
                (1..i).fold(BigInteger.ONE) { acc, n ->
                    acc.multiply(BigInteger.valueOf(n.toLong()))
                }
            }
        }

        val results = mutableMapOf<Int, BigInteger>()
        futures.forEach { (num, future) ->
            results[num] = future.get()
        }

        executor.shutdown()
        return results
    }
}

// Задача 7
object CoroutineLaunch {
    fun run(): List<String> = runBlocking {
        val results = mutableListOf<String>()

        val jobs = listOf("A", "B", "C").map { name ->
            launch {
                repeat(5) {
                    delay(500)
                    synchronized(results) {
                        results.add("Coroutine-$name: $it")
                    }
                }
            }
        }

        jobs.joinAll()
        return@runBlocking results
    }
}

// Задача 8
object AsyncAwait {
    fun run(): Long = runBlocking {
        val deferreds = listOf(
            async { (1L..250000L).sum() },
            async { (250001L..500000L).sum() },
            async { (500001L..750000L).sum() },
            async { (750001L..1000000L).sum() }
        )

        deferreds.sumOf { it.await() }
    }
}

// Задача 9
object StructuredConcurrency {
    fun run(failingCoroutineIndex: Int): Int = runBlocking {
        var completedCount = 0

        coroutineScope {
            val jobs = List(5) { index ->
                launch {
                    try {
                        if (index == failingCoroutineIndex) {
                            throw RuntimeException("Error in coroutine $index")
                        }
                        delay(1000)
                        completedCount++
                    } catch (e: Exception) {
                        println("Coroutine $index failed")
                    }
                }
            }
        }

        return@runBlocking completedCount
    }
}

// Задача 10
object WithContextIO {
    fun run(filePaths: List<String>): Map<String, String> = runBlocking {
        filePaths.map { path ->
            async(Dispatchers.IO) {
                path to File(path).readText()
            }
        }.associate { it.await() }
    }
}

// Задача 11
data class DownloadStats(
    val totalTime: Duration,
    val successful: Int,
    val failed: Int
)

object ImageDownloader {
    fun run(urls: List<String>, outputDir: String): DownloadStats = runBlocking {
        File(outputDir).mkdirs()
        val start = Instant.now()
        var success = 0
        var failed = 0

        withContext(Dispatchers.IO) {
            urls.forEachIndexed { index, url ->
                launch {
                    try {
                        val file = File(outputDir, "image_${index + 1}.jpg")
                        URL(url).openStream().use { input ->
                            file.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                        synchronized(this) {
                            success++
                            println("Downloaded ${success + failed}/${urls.size}")
                        }
                    } catch (e: Exception) {
                        synchronized(this) {
                            failed++
                            println("Downloaded ${success + failed}/${urls.size}")
                        }
                    }
                }
            }
        }

        return@runBlocking DownloadStats(
            totalTime = Duration.between(start, Instant.now()),
            successful = success,
            failed = failed
        )
    }
}