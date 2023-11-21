package id.co.pspmobile.ui.transaction.fragment

import android.content.Context
import android.widget.ArrayAdapter

class SpinnerAdapter(context: Context, resource: Int) :
    ArrayAdapter<String>(context, resource) {

    override fun getCount(): Int {
        val count = super.getCount()
        return if (count > 0) count - 1 else count
    }
}