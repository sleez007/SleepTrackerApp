package com.example.android.trackmysleepquality.sleepquality

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.*

class SleepQualityViewModel(
        val database: SleepDatabaseDao,
        private var sleepNightKey : Long = 0L
): ViewModel() {

    init {
        println(sleepNightKey.toString() +" I am a mad food jonkie")
    }

    private val viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToSleepTracker : MutableLiveData<Boolean?> = MutableLiveData()

    val navigateToSleepTracker : LiveData<Boolean?>
    get() = _navigateToSleepTracker

    fun doneNavigating(){
        _navigateToSleepTracker.value = null
    }


    //CREATE CLICK HANDLER

    fun onSetQuality(quality : Int) {
        uiScope.launch{
            withContext(Dispatchers.IO){
                val tonight = database.get(sleepNightKey)?: return@withContext
                tonight.sleepQuality = quality
                database.update(tonight)
            }

            _navigateToSleepTracker.value= true
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
