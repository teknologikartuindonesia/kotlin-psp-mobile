package id.co.pspmobile.ui.mutation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.databinding.ActivityMutationBinding
import id.co.pspmobile.ui.mutation.fragment.MutationFragment
import id.co.pspmobile.ui.mutation.fragment.OldMutationFragment
import id.co.pspmobile.ui.mutation.fragment.ViewPagerAdapter

val tabTitleArray = arrayOf(
    "Last 3 Month",
    "Over 3 Month"
)
val tabTitleArrayIndo = arrayOf(
    "3 Bulan Terakhir",
    "Diatas 3 Bulan"
)

@AndroidEntryPoint
class MutationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMutationBinding
    private val viewModel: MutationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMutationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = ViewPagerAdapter(
            MutationFragment(),
            OldMutationFragment(),
            supportFragmentManager,
            lifecycle)
        binding.apply {
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text =
                    if (viewModel.getLanguage() == "en") tabTitleArray[position]
                    else tabTitleArrayIndo[position]
            }.attach()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}