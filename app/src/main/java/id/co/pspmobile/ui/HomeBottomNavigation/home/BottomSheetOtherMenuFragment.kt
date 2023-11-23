package id.co.pspmobile.ui.HomeBottomNavigation.home

import android.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.databinding.FragmentBottomSheetOtherMenuBinding


@AndroidEntryPoint
class BottomSheetOtherMenuFragment(
    menuArray: ArrayList<DefaultMenuModel>,
    context: Context
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetOtherMenuBinding
    private lateinit var viewModel: BottomSheetOtherMenuViewModel

    private val otherDefaultMenuList = menuArray
    private val ctx = context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetOtherMenuBinding.inflate(inflater)
        binding.apply {
            val menuAdapter = BottomSheetOtherAdapter()
            menuAdapter.setMenuList(otherDefaultMenuList, ctx)
            binding.rvOtherMenu.adapter = menuAdapter
        }
        return binding.root
    }
}