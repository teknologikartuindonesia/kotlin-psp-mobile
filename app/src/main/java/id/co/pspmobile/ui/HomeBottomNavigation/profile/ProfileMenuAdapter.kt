package id.co.pspmobile.ui.HomeBottomNavigation.profile

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.databinding.ItemProfileMenuBinding
import id.co.pspmobile.ui.Utils.logout
import id.co.pspmobile.ui.dialog.DialogYesNo

class ProfileMenuAdapter: RecyclerView.Adapter<ProfileMenuAdapter.ProfileMenuViewHolder>() {
    private lateinit var menuArray: ArrayList<ProfileMenuModel>
    private lateinit var context: Context
    private lateinit var currentActivity: Activity
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileMenuAdapter.ProfileMenuViewHolder {
        val binding = ItemProfileMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileMenuAdapter.ProfileMenuViewHolder, position: Int) {
        holder.bind(menuArray[position])
    }

    override fun getItemCount(): Int {
        return menuArray.size
    }

    fun setMenuList(menuList: ArrayList<ProfileMenuModel>, context: Context) {
        this.menuArray = menuList
        this.context = context
        notifyDataSetChanged()
    }

    inner class ProfileMenuViewHolder(private val binding: ItemProfileMenuBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(menuModel: ProfileMenuModel) {
            with(binding) {
                imgProfileMenu.setImageResource(menuModel.icon)
                txtProfileMenu.text = menuModel.name
                llProfileMenu.setOnClickListener {
                    if (menuModel.name == "Logout") {
                        val dialog = DialogYesNo(
                            "Logout",
                            "Apakah anda yakin ingin logout?",
                            "Ya",
                            "Tidak",
                            {
                                context.logout()
                            },
                            noListener = {

                            }
                        )
                        dialog.show((context as FragmentActivity).supportFragmentManager, "Dialog Yes No")
                        return@setOnClickListener
                    }
                    context.startActivity(menuModel.path)
                }
            }
        }

    }
}