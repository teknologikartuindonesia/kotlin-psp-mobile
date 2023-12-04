package id.co.pspmobile.ui.digitalCard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.digitalCard.CardReqDto
import id.co.pspmobile.data.network.digitalCard.DigitalCardDto
import id.co.pspmobile.data.network.digitalCard.DigitalCardRepository
import id.co.pspmobile.data.network.model.ModelDigitalCard
import id.co.pspmobile.data.network.responses.checkcredential.CallerIdentity
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.network.responses.checkcredential.UserX
import id.co.pspmobile.data.network.user.UserRepository
import id.co.pspmobile.data.network.user.UserReqDto
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DigitalCardViewModel @Inject constructor(
    private val digitalCardRepository: DigitalCardRepository,
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences

) : ViewModel() {

    private var _digitalCardResponse: MutableLiveData<Resource<DigitalCardDto>> = MutableLiveData()
    val digitalCardResponse: LiveData<Resource<DigitalCardDto>> get() = _digitalCardResponse

    private val _updateDigitalCardResponse: MutableLiveData<Resource<CardReqDto>> = MutableLiveData()
    val updateDigitalCardResponse: LiveData<Resource<CardReqDto>> get() = _updateDigitalCardResponse

    private val _updateAccount: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val updateAccount: LiveData<Resource<Unit>> get() = _updateAccount

    fun getDigitalCard(page: Int) = viewModelScope.launch {
        _digitalCardResponse.value = Resource.Loading
        _digitalCardResponse.value = digitalCardRepository.getDigitalCard(page, 10)
    }


    fun updateDigitalCard(
        modelDigitalCard: ModelDigitalCard
    ) = viewModelScope.launch {
        _updateDigitalCardResponse.value = Resource.Loading

        val cardReqDto = CardReqDto(
            modelDigitalCard.id,
            modelDigitalCard.accountId,
            modelDigitalCard.nfcId,
            modelDigitalCard.companyId,
            modelDigitalCard.limitMax,
            modelDigitalCard.limitDaily,
            modelDigitalCard.cardBalance)

        _updateDigitalCardResponse.value = digitalCardRepository.updateDigitalCard(cardReqDto)
    }

    fun updateAccount(
        user: UserX,
        modelDigitalCard: ModelDigitalCard
    ) = viewModelScope.launch {
        _updateAccount.value = Resource.Loading

        var callerIdentity: CallerIdentity? = null
        for (identity: CallerIdentity in user.accounts[0].callerIdentities) {
            if (identity.callerId == modelDigitalCard.callerId) {
                callerIdentity = identity
                break
            }
        }

        var tags: List<String> = listOf()
        if (user.tags != null) {
            tags = user.tags.split(",")
        }
        val userReqDto = UserReqDto(
            user.name,
            user.phone.toString(),
            user.email.toString(),
            user.address,
            user.nik,
            user.gender,
            user.placeOfBirth,
            user.dateOfBirth,
            user.religion,
            user.maritalStatus,
            user.photoUrl,
            user.socmedAccounts,
            user.accounts[0].active,
            user.accounts[0].transactionUnlimited,
            user.accounts[0].roles,
            callerIdentity!!.callerId,
            callerIdentity!!.name,
            callerIdentity!!.title,
            tags,
            user.banks,
            user.accounts[0].sourceOfFund,
            user.accounts[0].note)

        _updateAccount.value = userRepository.updateAccount(userReqDto)
    }

   fun getUserData():CheckCredentialResponse {
       return userPreferences.getUserData()
   }

    fun saveUserData(checkCredentialResponse: CheckCredentialResponse) = viewModelScope.launch {
        userPreferences.saveUserData(checkCredentialResponse)
    }

}