package id.co.pspmobile.ui.invoice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.databinding.ActivityInvoiceBinding
import id.co.pspmobile.ui.invoice.fragment.HistoryFragment
import id.co.pspmobile.ui.invoice.fragment.InvoiceFragment
import id.co.pspmobile.ui.invoice.fragment.SummaryFragment
import id.co.pspmobile.ui.invoice.fragment.ViewPagerAdapter

val tabTitleArray = arrayOf(
    "Invoice",
    "History",
    "Summary"
)

@AndroidEntryPoint
class InvoiceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInvoiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = ViewPagerAdapter(
            InvoiceFragment(),
            HistoryFragment(),
            SummaryFragment(),
            supportFragmentManager,
            lifecycle)
        binding.apply {
            viewPager.adapter = adapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitleArray[position]
            }.attach()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}