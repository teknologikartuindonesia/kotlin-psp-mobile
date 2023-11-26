package id.co.pspmobile.ui.HomeBottomNavigation.profile.faq

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R
import id.co.pspmobile.databinding.ActivityFaqBinding

@AndroidEntryPoint
class FaqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqBinding
    private var faqList = ArrayList<FaqModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        showFaq()

    }

    fun showFaq(){
        faqList.add(FaqModel(resources.getString(R.string.faq_q_forgot_username), resources.getString(R.string.faq_a_forgot_username), false))
        faqList.add(FaqModel(resources.getString(R.string.faq_q_forgot_password), resources.getString(R.string.faq_a_forgot_password), false))
        faqList.add(FaqModel(resources.getString(R.string.faq_q_topup), resources.getString(R.string.faq_a_topup), false))
        faqList.add(FaqModel(resources.getString(R.string.faq_q_pay_invoice), resources.getString(R.string.faq_a_pay_invoice), false))
        faqList.add(FaqModel(resources.getString(R.string.faq_q_invoice_not_shown), resources.getString(R.string.faq_a_invoice_not_shown), false))

        val faqAdapter = FaqAdapter()
        faqAdapter.setFaqList(faqList)
        binding.rvFaq.adapter = faqAdapter
    }
}