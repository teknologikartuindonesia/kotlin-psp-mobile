package id.co.pspmobile.ui.HomeBottomNavigation.profile.faq

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.faq.FaqRepository
import id.co.pspmobile.data.network.faq.FaqRes
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaqViewModel @Inject constructor(
    private val faqRespository: FaqRepository
) : ViewModel() {

    private var _faqResponse: MutableLiveData<Resource<FaqRes>> = MutableLiveData()
    val faqResponse: MutableLiveData<Resource<FaqRes>> get() = _faqResponse
    fun getFaq(category: String,lang: String, tag: String) = viewModelScope.launch {
        _faqResponse.value = Resource.Loading
        _faqResponse.value = faqRespository.getFaq(category,lang, tag)
    }
}