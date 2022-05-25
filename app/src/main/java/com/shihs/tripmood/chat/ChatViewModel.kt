package com.shihs.tripmood.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Chat
import com.shihs.tripmood.dataclass.Plan
import com.shihs.tripmood.dataclass.Result
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.network.LoadApiStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: TripMoodRepo, arguments: Plan?) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _plan = MutableLiveData<Plan>().apply {
        value = arguments
    }
    val plan: LiveData<Plan>
        get() = _plan

    private var _chats = MutableLiveData<List<Chat>>()
    val chat: LiveData<List<Chat>>
        get() = _chats

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error

    init {
        getLiveChats()
    }

    fun getLiveChats() {

        if (_plan.value != null) {
            _plan.value!!.id?.let {
                _chats = repository.getLiveChats(it)
            }
        }
    }

    fun postChats(chat: Chat) {

        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result = repository.postChats(chat)) {
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
