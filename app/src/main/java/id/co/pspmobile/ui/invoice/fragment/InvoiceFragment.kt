package id.co.pspmobile.ui.invoice.fragment

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.FragmentInvoiceBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.invoice.InvoiceViewModel
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment
import kotlin.properties.Delegates

@AndroidEntryPoint
class InvoiceFragment() : Fragment() {

    private val viewModel: InvoiceViewModel by activityViewModels()

    private lateinit var binding: FragmentInvoiceBinding
    private lateinit var invoiceAdapter: InvoiceAdapter
    private var page: Int = 0
    private var size: Int = 10
    private var totalContent: Int = 1
    private var isLoading = false

    private lateinit var layoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(context)

        binding.rvInvoice.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                val total = invoiceAdapter.itemCount
                Log.e("total", size.toString())
                Log.w("totalPage", totalContent.toString())
                if (!isLoading && totalContent >= size) {
                    if (visibleItemCount + pastVisibleItem >= total) {
                        page++
                        isLoading = true
                        viewModel.getUnpaidInvoice(page++)
                    }
                } else {

                }
                super.onScrolled(recyclerView, dx, dy)

            }
        })

        viewModel.unpaidInvoiceResponse.observe(viewLifecycleOwner) {
            when (it is Resource.Loading) {
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                invoiceAdapter.setInvoices(it.value.content)
                totalContent = it.value.content.size
                isLoading = false
            } else if (it is Resource.Failure) {
                isLoading = false
                requireActivity().handleApiError(binding.rvInvoice, it)
            }
        }

        setupRecyclerView()
        viewModel.getUnpaidInvoice(page)

        //Declare LocalBroadcastManager
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(finishMessageReceiver, IntentFilter("reload-invoice"))

    }

    private fun setupRecyclerView() {
        binding.rvInvoice.setHasFixedSize(true)
        binding.rvInvoice.layoutManager = layoutManager
        invoiceAdapter = InvoiceAdapter(requireActivity())
        binding.rvInvoice.adapter = invoiceAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInvoiceBinding.inflate(inflater)
        return binding.root
    }

    //Listen from LocalBroadcastManager payment Success
    private val finishMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val reload = intent.getStringExtra("reload")
            if (reload == "reload") {
                invoiceAdapter.clear()
                viewModel.getUnpaidInvoice(page)
            }
        }
    }


    private fun showLottieLoader() {
        val loaderDialogFragment = LottieLoaderDialogFragment()
        loaderDialogFragment.show(parentFragmentManager, "lottieLoaderDialog")

    }

    private fun hideLottieLoader() {
        val loaderDialogFragment =
            parentFragmentManager.findFragmentByTag("lottieLoaderDialog") as LottieLoaderDialogFragment?
        loaderDialogFragment?.dismiss()
    }

}