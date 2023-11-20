package id.co.pspmobile.ui.invoices

import ViewPagerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import id.co.pspmobile.R
import id.co.pspmobile.databinding.ActivityInvoicesBinding

class InvoicesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInvoicesBinding

    private lateinit var pager: ViewPager // creating object of ViewPager
    private lateinit var tab: TabLayout  // creating object of TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoices)

        pager = findViewById(R.id.viewPager)
        tab = findViewById(R.id.tabs)

        // Initializing the ViewPagerAdapter
        val adapter = ViewPagerAdapter(supportFragmentManager)

        // add fragment to the list
        adapter.addFragment(InvoiceFragment(), "Tagihan")
        adapter.addFragment(HistoryFragment(), "Riwayat")
        adapter.addFragment(SummaryFragment(), "Ringkasan")

        // Adding the Adapter to the ViewPager
        pager.adapter = adapter

        // bind the viewPager with the TabLayout.
        tab.setupWithViewPager(pager)
    }
}