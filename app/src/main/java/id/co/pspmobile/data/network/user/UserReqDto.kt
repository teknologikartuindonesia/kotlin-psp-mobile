package id.co.pspmobile.data.network.user

import id.co.pspmobile.data.network.responses.checkcredential.SocmedAccount
import id.co.pspmobile.data.network.responses.checkcredential.SourceOfFund


data class UserReqDto(
    var name: String,
    var phone: String,
    var email: String,
    var address: String,
    var nik: String,
    var gender: String,
    var placeOfBirth: String,
    var dateOfBirth: String,
    var religion: String,
    var maritalStatus: String,
    var photoUrl: String,
    var socmedAccounts: List<SocmedAccount>,
    var active: Boolean = true,
    var transactionUnlimited: Boolean,
    var roles: List<String>,
    var callerId: String,
    var callerName: String,
    var callerTitle: String,
    var tags: List<String>,
    var banks: List<Any>,
    var sourceOfFund: SourceOfFund,
    var note: String
)
