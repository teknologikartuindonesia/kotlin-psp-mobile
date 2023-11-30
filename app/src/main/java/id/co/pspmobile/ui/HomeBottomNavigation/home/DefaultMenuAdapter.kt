package id.co.pspmobile.ui.HomeBottomNavigation.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.data.network.responses.customapp.AppMenu
import id.co.pspmobile.databinding.ItemHomeMenuBinding
import id.co.pspmobile.ui.HomeActivity

class DefaultMenuAdapter: RecyclerView.Adapter<DefaultMenuAdapter.ViewHolder>() {
    private lateinit var menuArray: ArrayList<DefaultMenuModel>
    private lateinit var otherMenuArray: ArrayList<DefaultMenuModel>
    private lateinit var allMenuArray: ArrayList<DefaultMenuModel>
    private lateinit var context: Context
    private lateinit var act: FragmentActivity
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

    fun setOtherMenuList(menuList: ArrayList<DefaultMenuModel>, context: Context, act: FragmentActivity) {
        this.otherMenuArray = menuList
        this.context = context
        this.act = act
    }
    fun setAllMenuList(menuList: ArrayList<DefaultMenuModel>, context: Context, act: FragmentActivity) {
        this.allMenuArray = menuList
        this.context = context
        this.act = act
    }
    override fun getItemCount(): Int {
        return menuArray.size
    }

    inner class ViewHolder(private val binding: ItemHomeMenuBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(menuModel: DefaultMenuModel) {
            with(binding) {
                txtHomeMenu.text = menuModel.name
                imgHomeMenu.setImageDrawable(itemView.context.getDrawable(menuModel.icon))
                if (menuModel.name == "More"){
                    layoutItemHomeMenu.setOnClickListener {
                        val bottomSheetOtherMenuFragment = BottomSheetOtherMenuFragment(otherMenuArray, context, act)
                        bottomSheetOtherMenuFragment.show(
                            (act as FragmentActivity).supportFragmentManager,
                            bottomSheetOtherMenuFragment.tag
                        )
                    }
                    return@with
                }
                layoutItemHomeMenu.setOnClickListener {
                    context.startActivity(menuModel.path)
                }
            }
        }
    }
}