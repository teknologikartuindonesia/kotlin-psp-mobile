package id.co.pspmobile.ui.transaction.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.FragmentOldTransactionBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.transaction.TransactionViewModel
import java.time.LocalDate
import java.time.Month
import java.time.Year

@AndroidEntryPoint
class OldTransactionFragment : Fragment() {
    private val viewModel: TransactionViewModel by activityViewModels()

    private lateinit var binding: FragmentOldTransactionBinding
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var monthSpinnerAdapter : ArrayAdapter<String>
    private lateinit var yearSpinnerAdapter : ArrayAdapter<String>

    @RequiresApi(Build.VERSION_CODES.O)
    private var targetYear = LocalDate.now().year
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(false)

        viewModel.oldTransactionResponse.observe(viewLifecycleOwner) {
            binding.progressbar.visible(it is Resource.Loading)
            if (it is Resource.Success) {
                transactionAdapter.setTransactions(it.value.content)
                binding.apply {
                    rvOldTransaction.setHasFixedSize(true)
                    rvOldTransaction.adapter = transactionAdapter
                }
            } else if (it is Resource.Failure) {
                requireActivity().handleApiError(binding.rvOldTransaction, it)
            }
        }

        transactionAdapter = TransactionAdapter()

        monthSpinnerAdapter = SpinnerAdapter(
            requireContext(),
            R.layout.spinner_list
        )
        binding.spinnerMonth.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.getOldTransaction(getMonth(monthSpinnerAdapter.getItem(position).toString()), targetYear)
            }
        }

        yearSpinnerAdapter = SpinnerAdapter(
            requireContext(),
            R.layout.spinner_list
        )
        yearSpinnerAdapter.addAll(getYearForSpinner())
        binding.spinnerYear.adapter = yearSpinnerAdapter

        binding.spinnerYear.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                targetYear = yearSpinnerAdapter.getItem(position)!!.toInt()
                val months = getMonthForSpinner(targetYear)
                monthSpinnerAdapter.addAll(months)
                binding.spinnerMonth.adapter = monthSpinnerAdapter

                binding.spinnerMonth.setSelection(months.size - 1)
            }
        }

        binding.spinnerYear.setSelection(0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getMonthForSpinner(year: Int) : List<String> {
        val months = listOf<String>("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli",
            "Agustus", "September", "Oktober", "November", "Desember")

        val over3Month = LocalDate.now().minusDays(90)

        return if (over3Month.year == year) {
            val maxMonth = over3Month.monthValue
            val availableMonth = ArrayList<String>()
            for (i in 0 until maxMonth) {
                availableMonth.add(months[i])
            }
            availableMonth
        } else {
            months
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getYearForSpinner() : List<String> {
        val years = ArrayList<String>()
        years.add(Year.now().value.toString())
        years.add(Year.now().minusYears(1).value.toString())
        years.add(Year.now().minusYears(2).value.toString())
        return years
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
        binding = FragmentOldTransactionBinding.inflate(inflater)
        return binding.root
    }
}