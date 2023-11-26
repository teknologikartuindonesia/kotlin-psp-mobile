package id.co.pspmobile.ui.HomeBottomNavigation.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.databinding.FragmentProfileBinding
import id.co.pspmobile.ui.HomeBottomNavigation.profile.changepassword.ChangePasswordActivity
import id.co.pspmobile.ui.HomeBottomNavigation.profile.edit.EditProfileActivity
import id.co.pspmobile.ui.HomeBottomNavigation.profile.faq.FaqActivity
import id.co.pspmobile.ui.HomeBottomNavigation.profile.language.LanguageActivity

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val viewModel: ProfileViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root


        configureMenu()
        binding.llProfile.setOnClickListener {
            startActivity(Intent(requireContext(), EditProfileActivity::class.java))
        }
        val data = viewModel.getUserData()
        if (data != null) {
            binding.txtProfileName.text = data.user.name
            binding.txtProfileEmail.text =
                if (data.user.email != "") data.user.email
                else if (data.user.phone != "") data.user.phone
                else ""
            val url = "${viewModel.getBaseUrl()}/main_a/image/get/${data.user.photoUrl}/pas"
            Glide.with(requireContext())
                .load(url)
                .placeholder(R.drawable.ic_profile_profile)
                .into(binding.imgProfile)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun configureMenu(){
        val menuList = ArrayList<ProfileMenuModel>()
        menuList.add(ProfileMenuModel("Profile", R.drawable.ic_profile_profile, Intent(requireContext(), EditProfileActivity::class.java)))
        menuList.add(ProfileMenuModel("Change Password", R.drawable.ic_profile_change_password, Intent(requireContext(), ChangePasswordActivity::class.java)))
        menuList.add(ProfileMenuModel("FAQ", R.drawable.ic_profile_faq, Intent(requireContext(), FaqActivity::class.java)))
        menuList.add(ProfileMenuModel("Language", R.drawable.ic_profile_language, Intent(requireContext(), LanguageActivity::class.java)))
        menuList.add(ProfileMenuModel("Logout", R.drawable.ic_profile_logout, Intent(requireContext(), LanguageActivity::class.java)))
        val adapter = ProfileMenuAdapter(viewModel)
        Log.d("ProfileFragment", "configureMenu: ${menuList.size}")
        adapter.setMenuList(menuList, requireContext())
        binding.rvProfile.adapter = adapter
    }
}