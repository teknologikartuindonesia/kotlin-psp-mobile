package id.co.pspmobile.ui.customDialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.databinding.FragmentCustomDialogBinding


@AndroidEntryPoint
class CustomDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCustomDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCustomDialogBinding.inflate(inflater)
        val args = arguments
        binding.apply {

            val title = args?.getString("title")
            val subTitle = args?.getString("subtitle")
            tvTitle.text = title
            tvSubtitle.text = subTitle
            btnCancel.setOnClickListener { dismiss() }
            btnSubmit.setOnClickListener {
                dismiss()
                val intent = Intent("custom-dialog")
                intent.putExtra("feature", args?.getString("feature"))
                intent.putExtra("type", args?.getString("type"))
                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

//        if (args != null) {
//            val title = args.getString("title")
//            val subTitle = args.getString("title")
//            binding.tvTitle.text=title
//            binding.tvSubtitle.text = subTitle
//        }
    }


}