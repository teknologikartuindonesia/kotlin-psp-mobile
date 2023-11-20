package id.co.pspmobile.ui.HomeBottomNavigation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var menuArray: ArrayList<MenuModel>? = null
    private var otherMenuArray: ArrayList<MenuModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        configureMenu()
        getBalance()
        getInfoHeadline()
        if (viewModel.getUserData().activeCompany.customApps){
            getCustomAppData()
        }

        return root
    }

    fun getBalance(){

    }

    fun getInfoHeadline(){

    }

    fun getCustomAppData(){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun configureMenu(){
        val menuList = ArrayList<MenuModel>()
        menuList.add(MenuModel("Top up", R.drawable.ic_home_black_24dp))
        menuList.add(MenuModel("Invoice", R.drawable.ic_home_black_24dp))
        menuList.add(MenuModel("Mutation", R.drawable.ic_home_black_24dp))
        menuList.add(MenuModel("Transaction", R.drawable.ic_home_black_24dp))
        menuList.add(MenuModel("Attendance", R.drawable.ic_home_black_24dp))
        menuList.add(MenuModel("Digital Card", R.drawable.ic_home_black_24dp))
        menuList.add(MenuModel("Account", R.drawable.ic_home_black_24dp))
        menuList.add(MenuModel("Donation", R.drawable.ic_home_black_24dp))
        menuList.add(MenuModel("Schedule", R.drawable.ic_home_black_24dp))
        menuList.add(MenuModel("Calendar", R.drawable.ic_home_black_24dp))
        menuList.add(MenuModel("Support", R.drawable.ic_home_black_24dp))

        // split 7 menuList to menuArray then the rest to otherMenuArray
        menuArray = ArrayList(menuList.subList(0, 7))
        menuArray!!.add(MenuModel("More", R.drawable.ic_home_black_24dp))
        otherMenuArray = ArrayList(menuList.subList(7, menuList.size))

        val rv = binding.rvMenu
        val rvSpanCount = 4
        val layoutManager = GridLayoutManager(requireContext(), rvSpanCount, GridLayoutManager.VERTICAL, false)
        rv.layoutManager = layoutManager


        val menuAdapter = MenuAdapter()
        menuAdapter.setMenuList(menuArray!!)
        menuAdapter.setOnclickListener { view ->
            Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
        }

        rv.adapter = menuAdapter
    }
}