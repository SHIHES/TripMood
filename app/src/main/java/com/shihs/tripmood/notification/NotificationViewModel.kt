package com.shihs.tripmood.notification

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shihs.tripmood.dataclass.Invite
import com.shihs.tripmood.dataclass.Result
import com.shihs.tripmood.dataclass.User
import com.shihs.tripmood.dataclass.source.TripMoodRepo
import com.shihs.tripmood.network.LoadApiStatus
import com.shihs.tripmood.util.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NotificationViewModel(private val repository: TripMoodRepo) : ViewModel() {

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<LoadApiStatus>()

    val status: LiveData<LoadApiStatus>
        get() = _status

    private val _error = MutableLiveData<String>()

    val error: LiveData<String>
        get() = _error


    private val _receiveInvites = MutableLiveData<List<Invite>>()

    val receiveInvites: LiveData<List<Invite>>
        get() = _receiveInvites

    private val _replyInvites = MutableLiveData<List<Invite>>()

    val replyInvites: LiveData<List<Invite>>
        get() = _replyInvites

    private val _allUserInvites = MutableLiveData<List<Invite>>()

    val allUserInvites: LiveData<List<Invite>>
        get() = _allUserInvites


    var allInvitesList = mutableListOf<Invite>()

    var receiveInvitesList = mutableListOf<Invite>()

    var replyInvitesList = mutableListOf<Invite>()



    init {
        getReceiveInvite()
    }


    fun getReceiveInvite(){
        coroutineScope.launch {
        _status.value = LoadApiStatus.LOADING

            receiveInvitesList.clear()

        when (val result =  repository.getReceiveInvite()) {
            is Result.Success -> {
                 receiveInvitesList = result.data.toMutableList()
                 _receiveInvites.value = result.data!!

                Log.d("QAQQQQQ", "receiveInvitesList ${_receiveInvites.value}")
                Log.d("QAQQQQQ", "receiveInvitesList $receiveInvitesList")
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
    } }

    fun getReplyInvite(){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            replyInvitesList.clear()

            when (val result =  repository.getSendReply()) {
                is Result.Success -> {
                     replyInvitesList = result.data.toMutableList()
                    _replyInvites.value = result.data!!
                    Log.d("QAQQQQQ", "receiveInvitesList ${_replyInvites.value}")
                    Log.d("QAQQQQQ", "receiveInvitesList $replyInvitesList")
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
        } }



    fun addInvites(){

        allInvitesList.clear()

        Log.d("QAQQQQQ", "$receiveInvitesList, $replyInvitesList")

        allInvitesList = (receiveInvitesList + replyInvitesList).toMutableList()

        _allUserInvites.value = allInvitesList
    }



    fun acceptInviteChangeStatus(inviteID: String){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result =  repository.acceptInviteChangeStatus(inviteID = inviteID)) {
                is Result.Success -> {
                    Log.d("QAQQQQQ", "acceptInviteChangeStatus ${result.data}")
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
        } }

    fun acceptInviteAddUserToPlan(invite: Invite){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING
            val inviter = User(name = invite.senderName, image = invite.senderPhotoUrl, uid = invite.senderID)
            when (val result =  repository.acceptInviteAddUserToPlan(planID = invite.invitePlanID!!, user = inviter)) {
                is Result.Success -> {
                    Log.d("QAQQQQQ", "acceptInviteAddUserToPlan ${result.data}")
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



    fun refusedInvite(inviteID: String){
        coroutineScope.launch {
            _status.value = LoadApiStatus.LOADING

            when (val result =  repository.refusedInvite(inviteID = inviteID)) {
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
        } }




}