package com.secondspace.app

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi

/**
 * Android 12+ (API 31) only. The system starts this activity to ask the DPC which
 * provisioning mode it wants. We always answer "managed profile".
 * No UI: we set the result and finish immediately.
 */
@RequiresApi(Build.VERSION_CODES.S)
class GetProvisioningModeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Modes the system is willing to offer for this provisioning request
        val allowedModes = intent.getIntegerArrayListExtra(
            DevicePolicyManager.EXTRA_PROVISIONING_ALLOWED_PROVISIONING_MODES
        )

        // If managed-profile mode is not on offer, cancel instead of giving a wrong answer
        if (allowedModes != null &&
            !allowedModes.contains(DevicePolicyManager.PROVISIONING_MODE_MANAGED_PROFILE)
        ) {
            setResult(RESULT_CANCELED)
            finish()
            return
        }

        val result = Intent().putExtra(
            DevicePolicyManager.EXTRA_PROVISIONING_MODE,
            DevicePolicyManager.PROVISIONING_MODE_MANAGED_PROFILE
        )
        setResult(RESULT_OK, result)
        finish()
    }
}
