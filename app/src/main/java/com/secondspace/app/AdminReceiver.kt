package com.secondspace.app

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Device admin receiver. Inside the new work profile this app is the "profile owner",
 * and the system calls onProfileProvisioningComplete once the profile has been created.
 */
class AdminReceiver : DeviceAdminReceiver() {

    override fun onProfileProvisioningComplete(context: Context, intent: Intent) {
        val dpm = context.getSystemService(DevicePolicyManager::class.java)
        val admin = ComponentName(context.applicationContext, AdminReceiver::class.java)

        // Name shown for the profile in system UI
        dpm.setProfileName(admin, context.getString(R.string.work_profile_name))

        // Without this call the profile stays disabled and its apps do not appear
        dpm.setProfileEnabled(admin)

        Toast.makeText(context, R.string.toast_profile_ready, Toast.LENGTH_LONG).show()
    }
}
