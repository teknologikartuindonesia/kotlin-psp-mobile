package id.co.pspmobile.ui.digitalCard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.digitalCard.DigitalCardDto
import id.co.pspmobile.databinding.ActivityDigitalCardBinding
import id.co.pspmobile.databinding.ActivityHistorySyncDigitalCardBinding
import id.co.pspmobile.ui.invoice.fragment.HistorySyncDigitalCardAdapter
import id.co.pspmobile.ui.invoice.fragment.SummaryAdapter
import kotlinx.coroutines.runBlocking
import kotlin.math.log

@AndroidEntryPoint
class HistorySyncDigitalCardActivity : AppCompatActivity() {
    private val viewModel: DigitalCardViewModel by viewModels()
    private lateinit var binding: ActivityHistorySyncDigitalCardBinding
    private lateinit var historySyncDigitalCardAdapter: HistorySyncDigitalCardAdapter

    private lateinit var layoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutManager = LinearLayoutManager(this)
        binding = ActivityHistorySyncDigitalCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var history = runBlocking { viewModel.getSyncDigitalCard() }
        Log.e("test", history.toString())
//        historySyncDigitalCardAdapter.setHistorySyncDigitalCard(history.data)
//
//
//        binding.apply {
//            rvHistorySync.setHasFixedSize(true)
//            rvHistorySync.adapter = historySyncDigitalCardAdapter
//        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}