package id.co.pspmobile.ui.mutation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.report.MutationResDto
import id.co.pspmobile.data.network.report.ReportRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MutationViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    fun getLanguage() = userPreferences.getLanguage()

    private var _mutationResponse: MutableLiveData<Resource<MutationResDto>> = MutableLiveData()
    val mutationResponse: LiveData<Resource<MutationResDto>> get() = _mutationResponse

    private var _oldMutationResponse: MutableLiveData<Resource<MutationResDto>> = MutableLiveData()
    val oldMutationResponse: LiveData<Resource<MutationResDto>> get() = _oldMutationResponse

    fun getMutation(date: String, page: Int) = viewModelScope.launch {
        _mutationResponse.value = Resource.Loading
        _mutationResponse.value = reportRepository.getMutation(date, 10, page)
    }

    fun getOldMutation(date: String, page: Int) = viewModelScope.launch {
        _oldMutationResponse.value = Resource.Loading
        _oldMutationResponse.value = reportRepository.getOldMutation(date, 10, page)
    }

}