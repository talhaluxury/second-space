package com.secondspace.app

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi

/**
 * Android 12+ (API 31) only. Launched inside the new work profile so the DPC can
 * apply its policies before setup ends. This app sets no extra policies, so it
 * reports success right away. Without this activity, provisioning fails on Android 12+.
 */
@RequiresApi(Build.VERSION_CODES.S)
class PolicyComplianceActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(RESULT_OK)
        finish()
    }
}
