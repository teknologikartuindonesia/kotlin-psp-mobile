package id.co.pspmobile.ui.donation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.donation.DonationRepository
import id.co.pspmobile.data.network.donation.DonationResDto
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DonationViewModel @Inject constructor(
    private val donationRepository: DonationRepository
) : ViewModel() {

    private var _donationResponse: MutableLiveData<Resource<DonationResDto>> = MutableLiveData()
    val donationResponse: LiveData<Resource<DonationResDto>> get() = _donationResponse

    fun getAllDonation(page: Int) = viewModelScope.launch {
        _donationResponse.value = Resource.Loading
        _donationResponse.value = donationRepository.getAllDonation(page, 10)
    }

}