package id.co.pspmobile.ui.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.co.pspmobile.R
import id.co.pspmobile.databinding.ActivityIntroBinding
import id.co.pspmobile.ui.Utils
import id.co.pspmobile.ui.Utils.slideAnimation
import id.co.pspmobile.ui.Utils.startNewActivity
import id.co.pspmobile.ui.Utils.visible
import id.co.pspmobile.ui.login.LoginActivity

class IntroActivity : AppCompatActivity() {

    var step = 0
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.btnLogo.setOnClickListener {
            step = 0
            binding.imgIntro1.visible(true)
            binding.imgIntro2.visible(false)
            binding.imgIntro3.visible(false)
            binding.imgIntro1.visible(true)
            binding.imgIntro2.visible(false)
            binding.imgIntro3.visible(false)
        }

        binding.btnNext.setOnClickListener {
            when(step) {
                0 -> {
                    binding.imgIntro2.slideAnimation(Utils.SlideDirection.LEFT, Utils.SlideType.SHOW)
                    binding.imgIntro1.visible(false)
                    binding.imgIntro1.visible(false)
                    binding.imgIntro2.visible(true)
                    binding.imgPager1.setImageResource(R.drawable.inactive_dot)
                    binding.imgPager2.setImageResource(R.drawable.active_dot)
                    binding.imgPager3.setImageResource(R.drawable.inactive_dot)

                    binding.txtTitle.text = resources.getText(R.string.digital_invoice)
                    binding.txtSubtitle.text = resources.getText(R.string.subtitle_intro_2)
                    step += 1
                }
                1 -> {
                    binding.imgIntro3.slideAnimation(Utils.SlideDirection.LEFT, Utils.SlideType.SHOW)
                    binding.imgIntro2.visible(false)
                    binding.imgIntro2.visible(false)
                    binding.imgIntro3.visible(true)
                    binding.imgPager1.setImageResource(R.drawable.inactive_dot)
                    binding.imgPager2.setImageResource(R.drawable.inactive_dot)
                    binding.imgPager3.setImageResource(R.drawable.active_dot)
                    binding.txtTitle.text = resources.getText(R.string.absensi)
                    binding.txtSubtitle.text = resources.getText(R.string.subtitle_intro_3)
                    step += 1
                }
                else -> {
                    // done
                    startNewActivity(LoginActivity::class.java)
                }
            }

        }
    }
}