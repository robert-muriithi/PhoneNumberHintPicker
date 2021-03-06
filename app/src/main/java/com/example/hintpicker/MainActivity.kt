package com.example.hintpicker

import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hintpicker.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.CredentialsApi
import com.google.android.gms.auth.api.credentials.HintRequest


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var NUMBER_PICKER_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.verifyBtn.setOnClickListener { verify() }
    }

    override fun onStart() {
        super.onStart()
        selectPhone()
    }

    private fun selectPhone() {

        val hintPicker = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val intent1 = Credentials.getClient(applicationContext).getHintPickerIntent(hintPicker)

        try {
            startIntentSenderForResult(
                intent1.intentSender,
                NUMBER_PICKER_REQUEST,
                null,
                0,
                0,
                0,
                Bundle()
            )
        } catch (e: SendIntentException) {
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NUMBER_PICKER_REQUEST && resultCode == RESULT_OK) {
            // Obtain the phone number from the result
            val credentials: Credential? = data?.getParcelableExtra(Credential.EXTRA_KEY)
            credentials.apply {
                if (credentials != null) {
                    binding.mobileNumberET.setText(credentials.id.substring(0))
                }
            }

        } else if (requestCode == NUMBER_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
            Toast.makeText(this, "no number found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verify() {
        binding.mobileNumberET.onEditorAction(EditorInfo.IME_ACTION_DONE)
        invalidateVerificationUI(false)
    }

    private fun invalidateVerificationUI(isEnabled: Boolean) {
        if (isEnabled) {
            binding.mobileNumberET.setText("")
        } else {
            val dialog = ProgressDialog(this)
            dialog.setTitle("Verifying")
            dialog.setMessage("Please Wait..")
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }
        binding.verifyBtn.isEnabled = isEnabled
        binding.mobileNumberET.isEnabled = isEnabled
    }


}