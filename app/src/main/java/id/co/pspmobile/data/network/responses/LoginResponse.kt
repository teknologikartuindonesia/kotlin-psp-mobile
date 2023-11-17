package id.co.pspmobile.data.network.responses

import com.google.errorprone.annotations.Keep

@Keep
data class LoginResponse (
    val firstLogin: Boolean,
    val user: User
)