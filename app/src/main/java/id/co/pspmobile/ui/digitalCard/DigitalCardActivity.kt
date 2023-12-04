package id.co.pspmobile.ui.digitalCard
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.data.network.Resource
import id.co.pspmobile.data.network.digitalCard.DigitalCardDto
import id.co.pspmobile.data.network.model.ModelDigitalCard
import id.co.pspmobile.databinding.ActivityDigitalCardBinding
import id.co.pspmobile.ui.Utils.formatCurrency
import id.co.pspmobile.ui.digitalCard.fragment.BottomSheetCardDigitalHelpFragment
import id.co.pspmobile.ui.digitalCard.fragment.BottomSheetSetLimitFragment
import id.co.pspmobile.ui.preloader.LottieLoaderDialogFragment
import java.lang.Math.abs


@AndroidEntryPoint
class DigitalCardActivity : AppCompatActivity() {
    private val viewModel: DigitalCardViewModel by viewModels()
    private lateinit var binding: ActivityDigitalCardBinding
    private lateinit var itemCard: DigitalCardDto
    private var activePosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDigitalCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        viewPager.apply {
            clipChildren = false  // No clipping the left and right items
            clipToPadding = false  // Show the viewpager in full width without clipping the padding
            offscreenPageLimit = 3  // Render the left and right items
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER // Remove the scroll effect
        }
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }
        viewPager.setPageTransformer(compositePageTransformer)
        viewPager.setPageTransformer(compositePageTransformer)

        viewModel.getDigitalCard(0)
        viewModel.digitalCardResponse.observe(this) {
            when(it is Resource.Loading){
                true -> showLottieLoader()
                else -> hideLottieLoader()
            }
            if (it is Resource.Success) {
                itemCard = it.value

                binding.tvBatasHarian.text = formatCurrency(it.value[0].limitDaily!!)
                binding.tvBatasMaks.text = formatCurrency(it.value[0].limitMax!!)

                viewPager.adapter = CarouselRVAdapter(it.value, this)
            } else if (it is Resource.Failure) {
//                handleApiError(binding.viewPager, it)
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                activePosition = position
//                Toast.makeText(this@DigitalCardActivity, position.toString(), Toast.LENGTH_SHORT).show()

                binding.tvBatasHarian.text = formatCurrency(itemCard[position].limitDaily!!)
                binding.tvBatasMaks.text = formatCurrency(itemCard[position].limitMax!!)

            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })


        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.syncHistory.setOnClickListener {
            startActivity(Intent(this, HistorySyncDigitalCardActivity::class.java))

        }
        binding.btnSetLimit.setOnClickListener {
            val bottomSheetDialogFragment: BottomSheetDialogFragment =
                BottomSheetSetLimitFragment(itemCard[activePosition], ::setLimitCallback)
            bottomSheetDialogFragment.show(
                (this as FragmentActivity).supportFragmentManager,
                bottomSheetDialogFragment.tag
            )
        }
        binding.btnHelp.setOnClickListener {
            val bottomSheetDialogFragment: BottomSheetDialogFragment =
                BottomSheetCardDigitalHelpFragment()
            bottomSheetDialogFragment.show(
                (this as FragmentActivity).supportFragmentManager,
                bottomSheetDialogFragment.tag
            )
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

    private fun setLimitCallback(modelDigitalCard: ModelDigitalCard) {
        itemCard[activePosition] = modelDigitalCard
        binding.tvBatasHarian.text = formatCurrency(modelDigitalCard.limitDaily!!)
        binding.tvBatasMaks.text = formatCurrency(modelDigitalCard.limitMax!!)
    }

}
