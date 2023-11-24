package id.co.pspmobile.ui.forgotpassword

data class ModelCheckOtp(
    val newPassword: String,
    val otp: String,
    val phoneEmail: String
)
