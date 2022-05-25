package com.shihs.tripmood.plan.createplan

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Result
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CreatePlanViewModel(private val repository: TripMoodRepo) : ViewModel() {

    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    private val _imageStatus = MutableLiveData<LoadApiStatus>()

    val imageStatus: LiveData<LoadApiStatus>
        get() = _imageStatus

    val imageUriCallback = MutableLiveData<Uri>()

    fun uploadImage(uri: Uri) {
        coroutineScope.launch {

            _imageStatus.value = LoadApiStatus.LOADING

            when (val result = repository.uploadImage(localUri = uri)) {
                is Result.Success -> {
                    imageUriCallback.value = result.data!!
                    _imageStatus.value = LoadApiStatus.DONE
                }
                is Result.Fail -> {
                    _imageStatus.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _imageStatus.value = LoadApiStatus.ERROR
                }
                else -> {
                    _imageStatus.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    fun postNewPlan(plan: Plan) {

        coroutineScope.launch {

            _status.value = LoadApiStatus.LOADING

            when (val result = repository.postPlan(plan = plan)) {
                is Result.Success -> {
                    _error.value = null
                    _status.value = LoadApiStatus.DONE
                }
                is Result.Fail -> {
                    _error.value = result.error
                    _status.value = LoadApiStatus.ERROR
                }
                is Result.Error -> {
                    _error.value = result.exception.toString()
                    _status.value = LoadApiStatus.ERROR
                }
                else -> {
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }
}
