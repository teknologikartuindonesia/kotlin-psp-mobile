package id.co.pspmobile.ui.forgotpassword

data class ModelSendOtp(
    val newPassword: String,
    val otp: String,
    val phoneEmail: String,
    val reqTime: String,
    val sendVia: String
)
