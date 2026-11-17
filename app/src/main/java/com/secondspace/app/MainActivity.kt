package com.secondspace.app

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class MainActivity : ComponentActivity() {

    // ViewModel survives rotation and other configuration changes
    private val viewModel: MainViewModel by viewModels()

    // Activity Result API: receives RESULT_OK / RESULT_CANCELED from the provisioning screen
    private val provisioningLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            viewModel.onProvisioningResult(result.resultCode)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SecondSpaceTheme {
                val state by viewModel.uiState.collectAsStateWithLifecycle()
                MainScreen(
                    state = state,
                    onCreateProfile = ::startProvisioning,
                    onOpenSettings = ::openSettings,
                    onDismissMessage = viewModel::dismissMessage
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Re-check after returning from Settings or the provisioning flow
        viewModel.refreshStatus()
    }

    private fun startProvisioning() {
        // Step 1: ask the system if a work profile can be created right now
        if (!viewModel.isProvisioningAllowed()) {
            viewModel.onProvisioningNotAllowed()
            return
        }
        // Step 2: launch the system provisioning screen.
        // Android 8-11: system calls AdminReceiver.onProfileProvisioningComplete when done.
        // Android 12+: system also calls GetProvisioningModeActivity and PolicyComplianceActivity.
        try {
            provisioningLauncher.launch(ProvisioningHelper.buildProvisioningIntent(this))
        } catch (e: ActivityNotFoundException) {
            // Some brands remove the managed provisioning screen
            viewModel.onNoProvisioningActivity()
        }
    }

    private fun openSettings() {
        try {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        } catch (e: ActivityNotFoundException) {
            viewModel.onNoProvisioningActivity()
        }
    }
}
