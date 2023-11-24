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

        val userData = viewModel.getUserData()
        val accounts = userData.user.accounts.get(0).callerIdentities

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