package id.co.pspmobile.ui.HomeBottomNavigation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.databinding.ItemHomeMenuBinding

class BottomSheetOtherAdapter: RecyclerView.Adapter<BottomSheetOtherAdapter.ViewHolder>() {
    private lateinit var menuArray: ArrayList<DefaultMenuModel>
    private lateinit var context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetOtherAdapter.ViewHolder {
        val binding = ItemHomeMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    fun setMenuList(menuList: ArrayList<DefaultMenuModel>, context: Context) {
        this.menuArray = menuList
        this.context = context
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return menuArray.size
    }

    override fun onBindViewHolder(holder: BottomSheetOtherAdapter.ViewHolder, position: Int) {
        holder.bind(menuArray[position])
    }

    inner class ViewHolder(private val binding: ItemHomeMenuBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(menuModel: DefaultMenuModel) {
            with(binding) {
                txtHomeMenu.text = menuModel.name
                imgHomeMenu.setImageDrawable(itemView.context.getDrawable(menuModel.icon))
                layoutItemHomeMenu.setOnClickListener {
                    context.startActivity(menuModel.path)
                }
            }
        }
    }
}