package id.co.pspmobile.ui.HomeBottomNavigation.profile.faq

import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.databinding.ActivityFaqBinding
import id.co.pspmobile.ui.HomeBottomNavigation.profile.ProfileViewModel
import id.co.pspmobile.ui.Utils.handleApiError
import id.co.pspmobile.ui.dialog.DialogCS
import id.co.pspmobile.ui.digitalCard.CarouselRVAdapter
import id.co.pspmobile.ui.invoice.fragment.SummaryAdapter
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment


@AndroidEntryPoint
class FaqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqBinding
    private var isFromFaq = true
    private val viewModel: FaqViewModel by viewModels()
    private lateinit var faqAdapter: FaqAdapter
    private var faqList = ArrayList<FaqModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)
        calculateHeight()

        isFromFaq = intent.getBooleanExtra("isFromFaq", true)

        viewModel.faqResponse.observe(this@FaqActivity) {
            when (it is Resource.Loading) {
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                for (i in it.value) {
                    faqList.add(
                        FaqModel(
                            i.question.toString(),
                            i.answer.toString(),
                            false,
                            i.openPage.toString()
                        )
                    )

                }
                faqAdapter.setFaqList(faqList)

                binding.apply {
                    rvFaq.setHasFixedSize(true)
                    rvFaq.adapter = faqAdapter
                }

            } else if (it is Resource.Failure) {
            }
        }

        faqAdapter = FaqAdapter(this)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnCs.setOnClickListener {
            val dialog = DialogCS()
            if (!isFromFaq) {
                val bundle = Bundle()
                bundle.putBoolean("isFromHome", false)
                dialog.arguments = bundle
            }
            dialog.show(supportFragmentManager, dialog.tag)
        }
        val intent = intent
        val key = intent.getStringExtra("key")
        val lang  = viewModel.getLanguage().toString()
        if (key == "profile") {
//            showFaqWithToken()
            viewModel.getFaq("PSPMOBILE", lang, "LOGIN")

        } else {
//            showFaq()
            viewModel.getFaq("PSPMOBILE", lang, "LOGIN")

        }

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
    private fun dpToPx(dp: Float): Float {
        val density = resources.displayMetrics.density
        return dp * density
    }
    private fun calculateHeight(){
        val llfaq = binding.llFaq
        val svRv = binding.svRecyclerView
        val llCS = binding.llContactUs
        // Get screen height
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
        val llCSHeight = llCS.height
        // Calculate the desired height
        val desiredHeight = screenHeight - llCSHeight
        // Set the height for the LinearLayout
        val layoutParams = llfaq.layoutParams
        layoutParams.height = desiredHeight.toInt()
        llfaq.layoutParams = layoutParams
    }
}