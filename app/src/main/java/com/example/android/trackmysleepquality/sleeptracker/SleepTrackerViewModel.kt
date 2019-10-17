package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()


    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var tonight = MutableLiveData<SleepNight?>()

    val nights = database.getAllNights()

    val nightsString = Transformations.map(nights){nights-> formatNights(nights, application.resources) }

    val startButtonVisible = Transformations.map(tonight){ null == it }

    val stopButtonVisible = Transformations.map(tonight){null != it}

    val clearButtonVisible = Transformations.map(nights){ it?.isNotEmpty() }

    private val _showSnackBarEvent : MutableLiveData<Boolean> = MutableLiveData()

    val showSnackBarEvent : LiveData<Boolean>
    get() = _showSnackBarEvent

    fun doneShowingSnackbar(){
        _showSnackBarEvent.value = false
    }

    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()

    val navigateToSleepQuality : LiveData<SleepNight>
    get() = _navigateToSleepQuality

    fun doneNavigating(){
        _navigateToSleepQuality.value = null
    }


    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = database.getTonight()
            if (night?.endTimeMilli != night?.startTimeMilli){
                night = null
            }
            night
        }
    }

    //Onstart tracking button

    fun onStartTracking(){
        uiScope.launch {
            val newNight = SleepNight()
            insert(newNight)

            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(newNight: SleepNight) {
        withContext(Dispatchers.IO){
            database.insert(newNight)
        }
    }

    //COMMON PATTERN WITH USE OF COROUTINES
//    fun someWorkNeedsToBeDone(){
//        uiScope.launch {
//            suspendFunction()
//        }
//    }
//
//    suspend fun suspendFunction() {
//        withContext(Dispatchers.IO){longRunningWork()}
//    }


    //define handlers for stop tracking button
    fun onStopTracking(){
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch

            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)

            //trigger navigation
            _navigateToSleepQuality.value = oldNight
        }
    }

    private suspend fun update(oldNight : SleepNight){
        withContext(Dispatchers.IO){
            database.update(oldNight)
        }
    }


    //define handler for the onClear button
    fun onClear(){
        uiScope.launch {
            clear()
            tonight.value = null
            _showSnackBarEvent.value = true
        }
    }

    private  suspend  fun clear(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }


    //DEFINE A NEW CLICK LISTENER FOR RECYCLER VIEW INDIVIDUAL ITEM CLICK

    private val _navigateToSleepDataQuality = MutableLiveData<Long>()

    val navigateToSleepDataQuality: LiveData<Long>
        get() = _navigateToSleepDataQuality

    fun onSleepNightClicked(id: Long){
        _navigateToSleepDataQuality.value = id
    }

    fun onSleepDataQualityNavigated(){
        _navigateToSleepDataQuality.value = null
    }





    //destroy viewModel here
    override fun onCleared() {
        super.onCleared()

        //close all coroutine jobs running
        viewModelJob.cancel()
    }
}

