package id.co.pspmobile.ui.transaction.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.FragmentTransactionBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.transaction.TransactionViewModel
import java.time.LocalDate

@AndroidEntryPoint
class TransactionFragment : Fragment() {
    private val viewModel: TransactionViewModel by activityViewModels()

    private lateinit var binding: FragmentTransactionBinding
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var spinnerAdapter : ArrayAdapter<String>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(false)

        viewModel.transactionResponse.observe(viewLifecycleOwner) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                transactionAdapter.setTransactions(it.value.content)
                binding.apply {
                    rvTransaction.setHasFixedSize(true)
                    rvTransaction.adapter = transactionAdapter
                }
            } else if (it is Resource.Failure) {
                requireActivity().handleApiError(binding.rvTransaction, it)
            }
        }

        transactionAdapter = TransactionAdapter()

        val date1 = LocalDate.now()
        val date2 = date1.minusDays(30)
        val date3 = date2.minusDays(30)
        val date4 = date3.minusDays(30)
        val months = listOf<String>(getMonthYear(date1), getMonthYear(date2), getMonthYear(date3), getMonthYear(date4))

        spinnerAdapter = SpinnerAdapter(
            requireContext(),
            R.layout.spinner_list
        )
        spinnerAdapter.addAll(months)
        binding.spinnerMonth.adapter = spinnerAdapter

        binding.spinnerMonth.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val month = spinnerAdapter.getItem(position)!!.split(" ")[0]
                val year = spinnerAdapter.getItem(position)!!.split(" ")[1]

                viewModel.getTransaction(getMonth(month), year.toInt())
            }
        }

        binding.spinnerMonth.setSelection(0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMonthYear(date: LocalDate) : String {
        val monthValue = date.monthValue
        val year = date.year
        var month = ""
        when (monthValue) {
            1 -> {
                month = "Januari"
            }
            2 -> {
                month = "Februari"
            }
            3 -> {
                month = "Maret"
            }
            4 -> {
                month = "April"
            }
            5 -> {
                month = "Mei"
            }
            6 -> {
                month = "Juni"
            }
            7 -> {
                month = "Juli"
            }
            8 -> {
                month = "Agustus"
            }
            9 -> {
                month = "September"
            }
            10 -> {
                month = "Oktober"
            }
            11 -> {
                month = "November"
            }
            12 -> {
                month = "Desember"
            }
        }

        return "$month $year"
    }

    fun getMonth(monthValue: String) : Int {
        var month = 1
        when (monthValue) {
            "Januari" -> {
                month = 1
            }
            "Februari" -> {
                month = 2
            }
            "Maret" -> {
                month = 3
            }
            "April" -> {
                month = 4
            }
            "Mei" -> {
                month = 5
            }
            "Juni" -> {
                month = 6
            }
            "Juli" -> {
                month = 7
            }
            "Agustus" -> {
                month = 8
            }
            "September" -> {
                month = 9
            }
            "Oktober" -> {
                month = 10
            }
            "November" -> {
                month = 11
            }
            "Desember" -> {
                month = 12
            }
        }

        return month
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionBinding.inflate(inflater)
        return binding.root
    }
}