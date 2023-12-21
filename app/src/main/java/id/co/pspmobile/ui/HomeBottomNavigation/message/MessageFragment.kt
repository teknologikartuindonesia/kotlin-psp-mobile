package id.co.pspmobile.ui.HomeBottomNavigation.message

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.FragmentMessageBinding
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment

@AndroidEntryPoint
class MessageFragment : Fragment() {

    private lateinit var binding: FragmentMessageBinding
    private val viewModel: MessageViewModel by viewModels()

    private var page: Int = 0
    private var size: Int = 5
    private var totalPage: Int = 1
    private var isLoading = false

    private var isNotificationTab = true

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var notificationAdapter: NotificationMessageAdapter
    private lateinit var broadcastAdapter: BroadcastMessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("MessageFragment", "onCreateView: opened")
        binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MessageFragment", "onViewCreated: opened")

        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(context)
        Log.d("NotificationMessage", "onViewCreated: opened")

        binding.rvNotificationMessage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstVisibleItemPosition()
                if (isNotificationTab){
                    val total = notificationAdapter.itemCount

                    if (!isLoading && total == totalPage) {
                        if (visibleItemCount + pastVisibleItem >= total) {
                            page++
                            isLoading = true
                            viewModel.getNotificationMessage(page++)
                        }
                    }
                } else {
                    val total = broadcastAdapter.itemCount

                    if (!isLoading && total == totalPage) {
                        if (visibleItemCount + pastVisibleItem >= total) {
                            page++
                            isLoading = true
                            viewModel.getBroadcastMessage(page++)
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy)

            }
        })

        viewModel.notificationMessageResponse.observe(viewLifecycleOwner) {
            when (it is Resource.Loading) {
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                notificationAdapter.setNotificationMessage(it.value.content as ArrayList)
                totalPage = it.value.content.size
                isLoading = false
            } else if (it is Resource.Failure) {
                isLoading = false
                requireActivity().handleApiError(binding.rvNotificationMessage, it)
            }
        }

        viewModel.broadcastMessageResponse.observe(viewLifecycleOwner) {
            when (it is Resource.Loading) {
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                broadcastAdapter.setBroadcastMessage(it.value.content as ArrayList)
                totalPage = it.value.content.size
                isLoading = false
            } else if (it is Resource.Failure) {
                isLoading = false
                requireActivity().handleApiError(binding.rvNotificationMessage, it)
            }
        }

        binding.btnBroadcast.background = null
        binding.btnBroadcast.setTextColor(resources.getColor(R.color.black))

        binding.btnNotification.setOnClickListener {
            isNotificationTab = true
            binding.btnNotification.background = resources.getDrawable(R.drawable.tab_layout_bg)
            binding.btnNotification.setTextColor(resources.getColor(R.color.white))
            binding.btnBroadcast.background = null
            binding.btnBroadcast.setTextColor(resources.getColor(R.color.black))
            page = 0
            setupRecyclerView()
            notificationAdapter.clearList()
            viewModel.getNotificationMessage(page)
        }

        binding.btnBroadcast.setOnClickListener {
            isNotificationTab = false
            binding.btnBroadcast.background = resources.getDrawable(R.drawable.tab_layout_bg)
            binding.btnBroadcast.setTextColor(resources.getColor(R.color.white))
            binding.btnNotification.background = null
            binding.btnNotification.setTextColor(resources.getColor(R.color.black))
            page = 0
            setupRecyclerView()
            broadcastAdapter.clearList()
            viewModel.getBroadcastMessage(page)
        }

        setupRecyclerView()
        viewModel.getNotificationMessage(page)

        binding.swipeRefreshLayout.setOnRefreshListener {
            if (isNotificationTab){
                notificationAdapter.clearList()
                viewModel.getNotificationMessage(page)
            } else {
                broadcastAdapter.clearList()
                viewModel.getBroadcastMessage(page)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        binding.rvNotificationMessage.setHasFixedSize(true)
        binding.rvNotificationMessage.layoutManager = layoutManager
        if (isNotificationTab) {
            notificationAdapter = NotificationMessageAdapter(requireActivity())
            binding.rvNotificationMessage.adapter = notificationAdapter
        } else {
            broadcastAdapter = BroadcastMessageAdapter(requireActivity())
            binding.rvNotificationMessage.adapter = broadcastAdapter
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
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