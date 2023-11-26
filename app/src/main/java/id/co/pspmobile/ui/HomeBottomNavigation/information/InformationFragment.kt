package id.co.pspmobile.ui.HomeBottomNavigation.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.FragmentInformationBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible

@AndroidEntryPoint
class InformationFragment : Fragment() {

    private lateinit var binding : FragmentInformationBinding
    private val viewModel: InformationViewModel by viewModels()
    private lateinit var infoAdapter: InformationAdapter
    private var page = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.informationResponse.observe(requireActivity()) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                infoAdapter.setInformations(it.value.content)
                binding.apply {
                    rvInfo.setHasFixedSize(true)
                    rvInfo.adapter = infoAdapter
                }
            } else if (it is Resource.Failure) {
                requireActivity().handleApiError(binding.rvInfo, it)
            }
        }

        infoAdapter = InformationAdapter()
        infoAdapter.setBaseUrl(viewModel.getBaseUrl())
        infoAdapter.setOnItemClickListerner { view ->
//            val infoTarget = view!!.tag as InfoResponseDto
//            val intent = Intent(this@InfoActivity, InfoDetailActivity::class.java)
//            intent.putExtra("info", infoTarget as Serializable)
//            startActivity(intent)
        }

        viewModel.getInformation(page)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInformationBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}