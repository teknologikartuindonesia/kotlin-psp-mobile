package id.co.pspmobile.ui.HomeBottomNavigation.home

import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.co.pspmobile.R
import id.co.pspmobile.data.local.UserPreferences
import id.co.pspmobile.data.network.responses.customapp.AppMenu
import id.co.pspmobile.databinding.ItemHomeMenuBinding
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking

class MenuAdapter: RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private lateinit var menuArray: ArrayList<MenuModel>
    private lateinit var allMenu: ArrayList<MenuModel>
    private var onItemClickListener : View.OnClickListener? = null
    private lateinit var userPreferences: UserPreferences
    private lateinit var baseURL: String
    private lateinit var companyId: String
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
    fun setAllMenuList(menuList: ArrayList<MenuModel>) {
        this.allMenu = menuList
        notifyDataSetChanged()
    }

    fun setMenuList(menuList: ArrayList<MenuModel>, baseURL: String, comp: String) {
        this.menuArray = menuList
        this.baseURL = baseURL
        this.companyId = comp
        notifyDataSetChanged()
    }
    fun setOnclickListener(onItemClickListener: View.OnClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val binding: ItemHomeMenuBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(menuModel: MenuModel) {
            with(binding) {
                if (menuModel.icon == "more.svg"){
                    val imageLoader = ImageLoader.Builder(itemView.context)
                        .components {
                            add(SvgDecoder.Factory())
                        }
                        .build()
                    val imageRequest = ImageRequest.Builder(itemView.context)
                        .data(R.drawable.ic_home_more_menu)
                        .target(imgHomeMenu)
                        .transformations(RoundedCornersTransformation())
                        .build()
                    val disposable = imageLoader.enqueue(imageRequest)
                } else {
                    val imgUrl = menuModel.icon
                    val imageLoader = ImageLoader.Builder(itemView.context)
                        .components {
                            add(SvgDecoder.Factory())
                        }
                        .build()
                    val imageRequest = ImageRequest.Builder(itemView.context)
                        .data(imgUrl)
                        .target(imgHomeMenu)
                        .transformations(RoundedCornersTransformation())
                        .size(40, 40)
                        .scale(Scale.FIT)
                        .build()
                    val disposable = imageLoader.enqueue(imageRequest)
                }

                itemView.setOnClickListener {
                    itemView.context.startActivity(menuModel.path)
                }
                txtHomeMenu.text = menuModel.name
            }
        }
    }
}