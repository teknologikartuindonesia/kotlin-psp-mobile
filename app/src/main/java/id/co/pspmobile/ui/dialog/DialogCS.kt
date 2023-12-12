package id.co.pspmobile.ui.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.co.pspmobile.databinding.DialogCsBinding

@AndroidEntryPoint
class DialogCS: DialogFragment() {

    private lateinit var binding: DialogCsBinding
    private val viewModel : DialogCsViewModel by viewModels()

    var name = ""
    var callerName = ""
    var institute = ""
    val version = "0.0.1"
    var isFromHome = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val args = arguments
        if(args == null){
            isFromHome = true
            binding.edNameCs.visibility = View.GONE
            binding.edInstituteCs.visibility = View.GONE
        } else {
            isFromHome = false
            binding.edNameCs.visibility = View.VISIBLE
            binding.edInstituteCs.visibility = View.VISIBLE
        }

        binding.btnSubmit.setOnClickListener {
            if (isFromHome){
                val userData = viewModel.getUserData()
                name = userData.user.name!!
                if (userData.user.accounts[0].callerIdentities.size == 1){
                    callerName = userData.user.accounts[0].callerIdentities[0].name!!
                } else if (userData.user.accounts[0].callerIdentities.size > 1){
                    callerName = userData.user.accounts[0].callerIdentities[0].name!!
                    for (i in 1 until userData.user.accounts[0].callerIdentities.size){
                        callerName += ", ${userData.user.accounts[0].callerIdentities[i].name}"
                    }
                } else {
                    callerName = ""
                }
                name = "$name orang tua dari $callerName"
                institute = userData.activeCompany.name!!
            }else{
                name = binding.edNameCs.text.toString()
                institute = binding.edInstituteCs.text.toString()
            }

            name = name.replace(" ", "%20")
            institute = institute.replace(" ", "%20")

            val message = binding.edMessageCs.text.toString().replace(" ", "%20")

            val appName =
                context?.packageManager?.let { it1 ->
                    context?.applicationInfo?.loadLabel(it1).
                    toString().replace(" ", "%20") }
            var url =
                "https://wa.me/${viewModel.getCsNumber()}?text=Halo,%20saya%20$name%20dari%20$institute%20menemui%20kendala%20sebagai%20berikut:%0A%0A$message";
            url += "%0A%0ADikirim%20dari%20aplikasi:%0A$appName%0Aversi%20$version";

            Log.d("Message", url)

            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }
}