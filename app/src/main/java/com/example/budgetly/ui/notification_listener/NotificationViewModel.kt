package com.example.budgetly.ui.notification_listener

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetly.domain.usecases.db_usecases.notification.NotificationUseCases
import com.example.budgetly.ui.notification_listener.events.NotificationEvent
import com.example.budgetly.ui.notification_listener.states.NotificationState
import com.example.budgetly.utils.log
import com.example.budgetly.utils.permissions.PermissionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationUseCases: NotificationUseCases
) : ViewModel() {

    private val _notificationState = MutableStateFlow(NotificationState())
    val notificationState: StateFlow<NotificationState> = _notificationState.asStateFlow()

    private val _expandedNotifications = MutableStateFlow<List<Long>>(mutableListOf())
    val expandedNotifications: StateFlow<List<Long>> = _expandedNotifications.asStateFlow()

    private fun toggleNotification(notificationId:Long){
        if(_expandedNotifications.value.contains(notificationId)){
            _expandedNotifications.value =  _expandedNotifications.value.filter { it != notificationId }
        }else{
            _expandedNotifications.value += notificationId
        }
    }
    init {
        onEvent(NotificationEvent.LoadNotifications)
    }

    fun onEvent(event: NotificationEvent) {
        when (event) {
            is NotificationEvent.LoadNotifications -> {
                loadNotifications()
            }
            is NotificationEvent.DeleteNotification -> {
                _notificationState.value.tempNotificationId?.let {
                    viewModelScope.launch {
                        notificationUseCases.deleteNotificationById(it)
                    }
                }
            }

            is NotificationEvent.DeleteAllNotifications -> {
                viewModelScope.launch {
                    notificationUseCases.deleteAllNotifications()
                }
            }

            is NotificationEvent.ShowDeleteAllDialog -> showDeleteAllDialog(event.show)
            is NotificationEvent.ShowDeleteNotificationDialog -> showDeleteNotificationDialog(event.show)
            is NotificationEvent.SetTempNotification -> setTempNotificationId(event.id)
            is NotificationEvent.SetPermissionStatus -> setPermissionStatus(event.permissionStatus)
            is NotificationEvent.ToggleNotification -> toggleNotification(event.notificationId)
        }
    }

    private fun showDeleteAllDialog(show: Boolean) {
        _notificationState.update {
            it.copy(
                showDeleteAllDialog = show
            )
        }
    }
    private fun setPermissionStatus(permissionStatus: PermissionStatus) {
        _notificationState.update {
            it.copy(
                notificationPermissionStatus = permissionStatus
            )
        }
    }

    private fun setTempNotificationId(id: Long) {
        _notificationState.update {
            it.copy(
                tempNotificationId = id
            )
        }
    }

    private fun showDeleteNotificationDialog(show: Boolean) {
        _notificationState.update {
            it.copy(
                showDeleteNotificationDialog = show
            )
        }
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            notificationUseCases.getAllNotifications().collectLatest { notifications ->
                log("notification.count: ${notifications.count()}")
                _notificationState.update {
                    it.copy(
                        notifications = notifications,
                        isLoading = false
                    )
                }
            }
        }
    }
}
