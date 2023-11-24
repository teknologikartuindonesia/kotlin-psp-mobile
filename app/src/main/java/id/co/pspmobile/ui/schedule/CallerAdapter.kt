package id.co.pspmobile.ui.schedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import id.co.pspmobile.data.network.responses.checkcredential.CallerIdentity

class CallerAdapter(context: Context, private val callers: List<CallerIdentity>) :
    ArrayAdapter<CallerIdentity>(context, android.R.layout.simple_spinner_item, callers) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_item, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = callers[position].name

        return view
    }
}