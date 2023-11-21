package id.co.pspmobile.ui.mutation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.FragmentMutationBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.mutation.MutationViewModel

@AndroidEntryPoint
class MutationFragment : Fragment() {

    private val viewModel: MutationViewModel by activityViewModels()

    private lateinit var binding: FragmentMutationBinding
    private lateinit var mutationAdapter: MutationAdapter
    private var page: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(false)

        viewModel.mutationResponse.observe(viewLifecycleOwner) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                mutationAdapter.setMutations(it.value.content)
                binding.apply {
                    rvMutation.setHasFixedSize(true)
                    rvMutation.adapter = mutationAdapter
                }
            } else if (it is Resource.Failure) {
                requireActivity().handleApiError(binding.rvMutation, it)
            }
        }

        mutationAdapter = MutationAdapter()

        val date = ""
        viewModel.getMutation(date, page)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMutationBinding.inflate(inflater)
        return binding.root
    }
}