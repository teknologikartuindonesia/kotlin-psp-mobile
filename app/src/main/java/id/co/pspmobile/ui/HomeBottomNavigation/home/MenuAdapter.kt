package id.co.pspmobile.ui.HomeBottomNavigation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.databinding.ItemHomeMenuBinding

class MenuAdapter: RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private lateinit var menuArray: ArrayList<MenuModel>
    private var onItemClickListener : View.OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.ViewHolder {
        val binding = ItemHomeMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuAdapter.ViewHolder, position: Int) {
        holder.bind(menuArray[position])
    }

    override fun getItemCount(): Int {
        return menuArray.size
    }

    fun setMenuList(menuList: ArrayList<MenuModel>) {
        this.menuArray = menuList
        notifyDataSetChanged()
    }
    fun setOnclickListener(onItemClickListener: View.OnClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val binding: ItemHomeMenuBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(menuModel: MenuModel) {
            with(binding) {
                imgHomeMenu.setImageResource(menuModel.menuImage)
                txtHomeMenu.text = menuModel.menuName
            }
        }
    }
}