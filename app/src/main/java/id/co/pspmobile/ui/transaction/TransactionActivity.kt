package id.co.pspmobile.ui.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.databinding.ActivityTransactionBinding
import id.co.pspmobile.ui.transaction.fragment.OldTransactionFragment
import id.co.pspmobile.ui.transaction.fragment.TransactionFragment
import id.co.pspmobile.ui.transaction.fragment.ViewPagerAdapter

val tabTitleArray = arrayOf(
    "Last 3 Month",
    "Over 3 Month"
)
val tabTitleArrayIndo = arrayOf(
    "3 Bulan Terakhir",
    "Diatas 3 Bulan"
)
@AndroidEntryPoint
class TransactionActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTransactionBinding
    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ViewPagerAdapter(
            TransactionFragment(),
            OldTransactionFragment(),
            supportFragmentManager,
            lifecycle
        )
        binding.apply {
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = if (viewModel.getLanguage() == "en") tabTitleArray[position]
                else tabTitleArrayIndo[position]
            }.attach()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}