package id.co.pspmobile.ui.mutation.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.FragmentOldMutationBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.mutation.MutationViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class OldMutationFragment : Fragment() {
    private val viewModel: MutationViewModel by activityViewModels()

    private lateinit var binding: FragmentOldMutationBinding
    private lateinit var mutationAdapter: MutationAdapter
    private var page: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(false)

        viewModel.oldMutationResponse.observe(viewLifecycleOwner) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                mutationAdapter.setMutations(it.value.content)
                binding.apply {
                    rvOldMutation.setHasFixedSize(true)
                    rvOldMutation.adapter = mutationAdapter
                }
            } else if (it is Resource.Failure) {
                requireActivity().handleApiError(binding.rvOldMutation, it)
            }
        }

        mutationAdapter = MutationAdapter(requireContext())

//        val startDate = LocalDate.now().minusMonths(6)
        val endDate = LocalDate.now().minusMonths(3)
//        viewModel.getOldMutation(startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +
//                "/" + endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
//            page)
        viewModel.getOldMutation("01-01-2020" + "/" +
                endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
            page)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOldMutationBinding.inflate(inflater)
        return binding.root
    }
}