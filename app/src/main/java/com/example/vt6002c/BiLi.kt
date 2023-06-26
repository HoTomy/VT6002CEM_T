package com.example.vt6002c

class BiLi : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bi_li)

        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)

                    Toast.makeText(applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@BiLi, Input::class.java)
                    startActivity(intent)

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }

            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Please login for Your Personal Health Record")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText(" ")
            .build()
        val biometricLoginButton =
            findViewById<Button>(R.id.biometric_login)


        biometricLoginButton.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                biometricLoginButton.text = "Please login with your fingerprint."
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                biometricLoginButton.text = "No biometric features available on this device."
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                biometricLoginButton.text = "Biometric features are currently unavailable."
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                // Prompts the user to create credentials that your app accepts.
                biometricLoginButton.text = "Biometric features are not enrolled."
        }
    }

    override fun onResume() {
        super.onResume()
        val biometricStatusTextView =
            findViewById<Button>(R.id.biometric_login)
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                biometricStatusTextView.text = "App can authenticate using biometrics."
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                biometricStatusTextView.text = "No biometric features available on this device."
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                biometricStatusTextView.text = "Biometric features are currently unavailable."
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                // Prompts the user to create credentials that your app accepts.
                biometricStatusTextView.text = "Biometric features are not enrolled."
        }
    }


}