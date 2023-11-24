package id.co.pspmobile.ui.preloader

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.R

@AndroidEntryPoint
class LottieLoaderDialogFragment : DialogFragment() {

    private lateinit var viewModel: LottieLoaderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lottie_loader_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(LottieLoaderViewModel::class.java)

        val lottieAnimationView: LottieAnimationView = view.findViewById(R.id.lottieAnimationView)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                lottieAnimationView.visibility = View.VISIBLE
                lottieAnimationView.playAnimation()
            } else {
                lottieAnimationView.visibility = View.GONE
                lottieAnimationView.cancelAnimation()
                dismiss()
            }
        })

        // Example: Show loader for 3 seconds
        viewModel.showLoader()
//        Handler().postDelayed({ viewModel.hideLoader() }, 3000)
    }
}