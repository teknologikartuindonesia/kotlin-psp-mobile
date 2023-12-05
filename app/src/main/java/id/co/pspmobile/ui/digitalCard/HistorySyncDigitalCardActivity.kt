package id.co.pspmobile.ui.digitalCard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.local.SharePreferences
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.digitalCard.DigitalCardDto
import id.co.pspmobile.data.network.responses.checkcredential.CheckCredentialResponse
import id.co.pspmobile.data.network.responses.digitalCard.CardDataItem
import id.co.pspmobile.data.network.responses.digitalCard.NewDigitalCardData
import id.co.pspmobile.data.network.responses.digitalCard.SyncDigitalCard
import id.co.pspmobile.databinding.ActivityDigitalCardBinding
import id.co.pspmobile.databinding.ActivityHistorySyncDigitalCardBinding
import id.co.pspmobile.ui.invoice.fragment.HistorySyncDigitalCardAdapter
import id.co.pspmobile.ui.invoice.fragment.SummaryAdapter
import kotlinx.coroutines.flow.first
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
        layoutManager.reverseLayout = true
        binding = ActivityHistorySyncDigitalCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        historySyncDigitalCardAdapter = HistorySyncDigitalCardAdapter(viewModel)

        val existing =
            SharePreferences.getNewSyncDigitalCard(this)
        val cardDataList = mutableListOf<CardDataItem>()

        for (item in existing!!.dataList) {
            for (historyEntry in item.history.sortedBy { it }) {
                cardDataList.add(CardDataItem(item.nfcId, mutableListOf(historyEntry)))
            }
        }

        binding.apply {
            rvHistorySync.setHasFixedSize(true)
            rvHistorySync.adapter = historySyncDigitalCardAdapter
            rvHistorySync.layoutManager = layoutManager
        }

        if (existing != null) {
            Log.d("HistorySyncDigitalCardActivity", "onCreate: ${existing?.dataList}")
            historySyncDigitalCardAdapter.setHistorySyncDigitalCard(cardDataList)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}