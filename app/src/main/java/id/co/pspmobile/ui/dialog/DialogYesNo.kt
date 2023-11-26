package id.co.pspmobile.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import id.co.pspmobile.databinding.DialogYesNoBinding

class DialogYesNo(
    val title: String,
    val message: String,
    val yes: String,
    val no: String,
    val yesListener: () -> Unit,
    val noListener: () -> Unit
): DialogFragment() {

    private lateinit var binding: DialogYesNoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogYesNoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.text = title
        binding.tvSubtitle.text = message
        binding.btnSubmit.text = yes
        binding.btnCancel.text = no
        binding.btnSubmit.setOnClickListener {
            yesListener()
            dismiss()
        }
        binding.btnCancel.setOnClickListener {
            noListener()
            dismiss()
        }
    }

}