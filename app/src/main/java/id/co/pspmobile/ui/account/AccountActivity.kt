package id.co.pspmobile.ui.account

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.responses.checkcredential.CallerIdentity
import id.co.pspmobile.databinding.ActivityAccountBinding

@AndroidEntryPoint
class AccountActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAccountBinding
    private val viewModel: AccountViewModel by viewModels()
    private lateinit var accountAdapter: AccountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val accounts = ArrayList<CallerIdentity>()
        val callerIdentity1 = CallerIdentity("192007019", "1", "AMEL UPDATE",
            "2022-12-28-ttsavcnf-", emptyList(), "", )
        accounts.add(callerIdentity1)
        val callerIdentity2 = CallerIdentity("543520", "1", "NIAFADHILA1",
            "2022-12-28-nkffsshg-", emptyList(), "", )
        accounts.add(callerIdentity2)

//        val accounts = intent.getSerializableExtra("accounts") as List<CallerIdentity>

        accountAdapter = AccountAdapter()
        accountAdapter.setBaseUrl(viewModel.getBaseUrl())
        accountAdapter.setAccounts(accounts)
        binding.apply {
            rvAccount.setHasFixedSize(true)
            rvAccount.adapter = accountAdapter
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}