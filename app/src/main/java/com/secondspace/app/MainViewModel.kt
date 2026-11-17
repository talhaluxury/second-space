package com.secondspace.app

import android.app.Application
import android.app.admin.DevicePolicyManager
import android.content.pm.CrossProfileApps
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/** Which dialog (if any) the UI should show. Kept in the ViewModel so it survives rotation. */
enum class UiMessage { NONE, NOT_ALLOWED, SUCCESS, CANCELLED, NO_ACTIVITY }

data class UiState(
    val profileExists: Boolean = false,
    val message: UiMessage = UiMessage.NONE
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext = application
    private val dpm: DevicePolicyManager =
        application.getSystemService(DevicePolicyManager::class.java)
    private val crossProfileApps: CrossProfileApps =
        application.getSystemService(CrossProfileApps::class.java)

    private val _uiState = MutableStateFlow(UiState(profileExists = detectProfile()))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    /**
     * A work profile exists only if this app is the profile owner (we run inside it) or
     * this app is installed in another profile of the same user (CrossProfileApps).
     * Plain "more than one profile" is NOT used: dual-app/clone profiles on some phones
     * would be counted by mistake.
     */
    private fun detectProfile(): Boolean =
        dpm.isProfileOwnerApp(appContext.packageName) ||
            runCatching { crossProfileApps.targetUserProfiles.isNotEmpty() }.getOrDefault(false)

    fun refreshStatus() {
        _uiState.update { it.copy(profileExists = detectProfile()) }
    }

    /** Ask the system whether it will currently allow creating a work profile (API 24+). */
    fun isProvisioningAllowed(): Boolean =
        runCatching {
            dpm.isProvisioningAllowed(DevicePolicyManager.ACTION_PROVISION_MANAGED_PROFILE)
        }.getOrDefault(false)

    fun onProvisioningNotAllowed() = showMessage(UiMessage.NOT_ALLOWED)

    fun onNoProvisioningActivity() = showMessage(UiMessage.NO_ACTIVITY)

    fun onProvisioningResult(resultCode: Int) {
        // android.app.Activity.RESULT_OK == -1
        val message = if (resultCode == android.app.Activity.RESULT_OK) {
            UiMessage.SUCCESS
        } else {
            UiMessage.CANCELLED
        }
        _uiState.update { it.copy(profileExists = detectProfile(), message = message) }
    }

    fun dismissMessage() = showMessage(UiMessage.NONE)

    private fun showMessage(message: UiMessage) {
        _uiState.update { it.copy(message = message) }
    }
}
