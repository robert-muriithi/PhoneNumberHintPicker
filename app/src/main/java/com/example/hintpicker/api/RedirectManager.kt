package com.example.hintpicker.api

import android.util.Log

class RedirectManager {
    private val truSdk = TruSDK.getInstance()

    fun openCheckUrl(checkUrl: String): String {
        Log.d("RedirectManager", "Triggering open check url $checkUrl")
        return truSdk.openCheckUrl(checkUrl)
    }
}