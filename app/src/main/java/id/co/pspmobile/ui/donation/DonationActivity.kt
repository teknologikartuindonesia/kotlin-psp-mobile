package id.co.pspmobile.ui.donation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.donation.DonationDto
import id.co.pspmobile.databinding.ActivityDonationBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.donation.detail.DonationDetailActivity
import id.co.pspmobile.ui.mutation.fragment.MutationAdapter
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment
import java.io.Serializable

@AndroidEntryPoint
class DonationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDonationBinding
    private val viewModel: DonationViewModel by viewModels()
    private lateinit var donationAdapter: DonationAdapter

    private var page: Int = 0
    private var size: Int = 5
    private var totalPage: Int = 1
    private var isLoading = false
    private lateinit var layoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutManager = LinearLayoutManager(this)

        viewModel.donationResponse.observe(this) {
            when (it is Resource.Loading) {
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                totalPage = it.value.totalPages
                Log.d("totalPageDonation", "totalPage: $totalPage")
                val listResponse = it.value.content
                Log.d("listResponseDonation", it.value.toString())
                if (listResponse != null){
                    donationAdapter.setDonations(listResponse)
                }
                isLoading = false
            } else if (it is Resource.Failure) {
                isLoading = false
                handleApiError(binding.rvDonation, it)
            }
        }


        binding.rvDonation.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total  = donationAdapter.itemCount
                if (!isLoading && page < totalPage){
                    if (visibleItemCount + pastVisibleItem>= total){
                        page++
                        getData(false)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        binding.swipeRefresh.setOnRefreshListener {
            page = 0
            getData(true)
            binding.swipeRefresh.isRefreshing = false
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        setupRecyclerView()
        getData(false)
    }

    fun getData(isOnRefresh: Boolean){
        isLoading = true
        Log.d("getData", "last30")
        viewModel.getAllDonation(page)
    }

    private fun setupRecyclerView() {
        binding.rvDonation.setHasFixedSize(true)
        binding.rvDonation.layoutManager = layoutManager
        donationAdapter = DonationAdapter(this)
        binding.rvDonation.adapter = donationAdapter
    }

    private fun showLottieLoader() {
        val loaderDialogFragment = LottieLoaderDialogFragment()
        loaderDialogFragment.show(supportFragmentManager, "lottieLoaderDialog")

    }

    private fun hideLottieLoader() {
        val loaderDialogFragment =
            supportFragmentManager.findFragmentByTag("lottieLoaderDialog") as LottieLoaderDialogFragment?
        loaderDialogFragment?.dismiss()
    }
}