package id.co.pspmobile.data.network.responses.profile

data class UserResponse(
    val accounts: List<Account>,
    val address: String,
    val banks: List<Any>,
    val dateOfBirth: String,
    var email: String,
    val firebase: Firebase,
    val gender: String,
    val id: String,
    val maritalStatus: String,
    val name: String,
    val nik: String,
    val openfire: Openfire,
    var phone: String,
    var photoUrl: String,
    val placeOfBirth: String,
    val regDate: String,
    val religion: String,
    val socmedAccounts: List<SocmedAccount>,
    val tags: Any,
    val validationStatus: Boolean
)