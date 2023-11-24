package id.co.pspmobile.ui.donation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.donation.DonationDto
import id.co.pspmobile.data.network.donation.DonationRepository
import id.co.pspmobile.data.network.responses.balance.BalanceResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonationDetailViewModel @Inject constructor(
    private val donationRepository: DonationRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private var _donationResponse: MutableLiveData<Resource<DonationDto>> = MutableLiveData()
    val donationResponse: LiveData<Resource<DonationDto>> get() = _donationResponse

    fun getDonationById(donationId: String) = viewModelScope.launch {
        _donationResponse.value = Resource.Loading
        _donationResponse.value = donationRepository.getDonationById(donationId)
    }

    fun getBalanceData() : BalanceResponse {
        return userPreferences.getBalanceData()
    }

}