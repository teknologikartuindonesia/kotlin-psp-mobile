package id.co.pspmobile.ui.topup.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.databinding.LoadStateViewBinding

class LoadStateAdapter : LoadStateAdapter<id.co.pspmobile.ui.topup.history.LoadStateAdapter.LoadStateViewHolder>() {
    inner class LoadStateViewHolder(val binding: LoadStateViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            LoadStateViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.binding.apply {
            progressbar.isVisible = loadState is LoadState.Loading
        }
    }
}