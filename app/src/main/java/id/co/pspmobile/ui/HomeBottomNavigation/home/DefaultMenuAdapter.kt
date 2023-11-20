package id.co.pspmobile.ui.HomeBottomNavigation.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.responses.customapp.AppMenu
import id.co.pspmobile.databinding.ItemHomeMenuBinding

class DefaultMenuAdapter: RecyclerView.Adapter<DefaultMenuAdapter.ViewHolder>() {
    private lateinit var menuArray: ArrayList<DefaultMenuModel>
    private lateinit var context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DefaultMenuAdapter.ViewHolder {
        val binding = ItemHomeMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DefaultMenuAdapter.ViewHolder, position: Int) {
        holder.bind(menuArray[position])
    }

    fun setMenuList(menuList: ArrayList<DefaultMenuModel>, context: Context) {
        this.menuArray = menuList
        this.context = context
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return menuArray.size
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