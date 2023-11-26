package id.co.pspmobile.ui.HomeBottomNavigation.profile.faq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.co.pspmobile.databinding.ItemFaqBinding

class FaqAdapter: RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    private lateinit var faqArray: ArrayList<FaqModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqAdapter.ViewHolder {
        val binding = ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FaqAdapter.ViewHolder, position: Int) {
        holder.bind(faqArray[position])
    }

    fun setFaqList(faqList: ArrayList<FaqModel>) {
        this.faqArray = faqList
        notifyDataSetChanged()
    }

    fun showHide(index: Int){
        faqArray[index].show = !faqArray[index].show
        notifyItemChanged(index)
    }

    override fun getItemCount(): Int {
        return faqArray.size
    }

    inner class ViewHolder(private val binding: ItemFaqBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(faqModel: FaqModel) {
            with(binding) {
                txtQuestion.text = faqModel.question
                txtAnswer.text = faqModel.answer
                if (faqModel.show) {
                    imgFaqArrow.setImageResource(id.co.pspmobile.R.drawable.ic_arrow_up)
                    llFaqContent.visibility = ViewGroup.VISIBLE
                    txtAnswer.visibility = ViewGroup.VISIBLE
                } else {
                    imgFaqArrow.setImageResource(id.co.pspmobile.R.drawable.ic_arrow_down)
                    llFaqContent.visibility = ViewGroup.GONE
                    txtAnswer.visibility = ViewGroup.GONE
                }
                llFaqQuestion.setOnClickListener {
                    faqModel.show = !faqModel.show
                    notifyItemChanged(adapterPosition)
                }
            }
        }

    }
}