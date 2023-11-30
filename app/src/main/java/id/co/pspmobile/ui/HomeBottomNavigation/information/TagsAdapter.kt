package id.co.pspmobile.ui.HomeBottomNavigation.information

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.R
import id.co.pspmobile.databinding.ItemTagsBinding

class TagsAdapter(val context: Context): RecyclerView.Adapter<TagsAdapter.TagsViewHolder>() {
    var defaultTags = listOf<String>()
    private var selectedTag = mutableListOf<String>()
    private var onItemClickListener : View.OnClickListener? = null
    var clickedTag = ""
    private var isAll = true

    fun setOnItemClickListener(onItemClickListener: View.OnClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setDefaultTag(defaultTag: List<String>) {
        this.defaultTags = defaultTag
        notifyDataSetChanged()
    }

    fun setSelectedTag(selectedTag: List<String>) {
        this.selectedTag = selectedTag as MutableList<String>
        notifyDataSetChanged()
    }

    fun setAll(isAll: Boolean) {
        this.isAll = isAll
        notifyDataSetChanged()
    }
    inner class TagsViewHolder(private val binding: ItemTagsBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onItemClickListener?.onClick(it)
                Log.d("TagsAdapter", "TagsViewHolder: clicked")
            }
        }
        fun bind(tag: String) {
            binding.btnTag.setOnClickListener {
                Log.d("TagsAdapter", "TagsViewHolder: btn clicked")
                clickedTag = tag
                onItemClickListener?.onClick(it)
            }
            if(isAll){
                binding.btnTag.background = context.resources.getDrawable(R.drawable.tab_layout_outline)
                binding.btnTag.text = tag
                binding.btnTag.setTextColor(context.resources.getColor(R.color.primary))
            }else{
                if (selectedTag.contains(tag)) {
                    binding.btnTag.background = context.resources.getDrawable(R.drawable.tab_layout_bg)
                    binding.btnTag.text = tag
                    binding.btnTag.setTextColor(context.resources.getColor(R.color.white))
                } else {
                    binding.btnTag.background = context.resources.getDrawable(R.drawable.tab_layout_outline)
                    binding.btnTag.text = tag
                    binding.btnTag.setTextColor(context.resources.getColor(R.color.primary))
                }
            }
//            if (tag != "info"){
//                if (selectedTag.contains(tag)) {
//                    binding.btnTag.background = context.resources.getDrawable(R.drawable.tab_layout_bg)
//                    binding.btnTag.text = tag
//                    binding.btnTag.setTextColor(context.resources.getColor(R.color.white))
//                } else {
//                    binding.btnTag.background = context.resources.getDrawable(R.drawable.tab_layout_outline)
//                    binding.btnTag.text = tag
//                    binding.btnTag.setTextColor(context.resources.getColor(R.color.primary))
//                }
//            }
//            if (selectedTag.contains("info")){
//                if (tag == "info") {
//                    binding.btnTag.background = context.resources.getDrawable(R.drawable.tab_layout_bg)
//                    binding.btnTag.text = tag
//                    binding.btnTag.setTextColor(context.resources.getColor(R.color.white))
//                } else {
//                    binding.btnTag.background = context.resources.getDrawable(R.drawable.tab_layout_outline)
//                    binding.btnTag.text = tag
//                    binding.btnTag.setTextColor(context.resources.getColor(R.color.primary))
//                }
//            } else {
//                if (selectedTag.contains(tag)) {
//                    binding.btnTag.background = context.resources.getDrawable(R.drawable.tab_layout_bg)
//                    binding.btnTag.text = tag
//                    binding.btnTag.setTextColor(context.resources.getColor(R.color.white))
//                } else {
//                    binding.btnTag.background = context.resources.getDrawable(R.drawable.tab_layout_outline)
//                    binding.btnTag.text = tag
//                    binding.btnTag.setTextColor(context.resources.getColor(R.color.primary))
//                }
//            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsAdapter.TagsViewHolder {
        val binding = ItemTagsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TagsAdapter.TagsViewHolder, position: Int) {
        holder.bind(defaultTags[position])
    }

    override fun getItemCount(): Int {
        return defaultTags.size
    }
}