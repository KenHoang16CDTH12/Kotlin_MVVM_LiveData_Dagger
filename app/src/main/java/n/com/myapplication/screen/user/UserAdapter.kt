package n.com.myapplication.screen.user

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import n.com.myapplication.R
import n.com.myapplication.base.recyclerView.LoadMoreAdapter
import n.com.myapplication.base.recyclerView.OnItemClickListener
import n.com.myapplication.data.model.User
import n.com.myapplication.databinding.ItemUserBinding

class UserAdapter(context: Context) : LoadMoreAdapter<User>(context) {

  override fun getItemViewTypeLM(position: Int): Int {
    return 0
  }

  @NonNull
  override fun onCreateViewHolderLM(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val binding =
        DataBindingUtil.inflate<ItemUserBinding>(layoutInflater, R.layout.item_user, parent, false)
    return ItemViewHolder(binding, itemClickListener)
  }

  override fun onBindViewHolderLM(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as ItemViewHolder).bind(getItem(position), position)
  }

  internal class ItemViewHolder(
      private val binding: ItemUserBinding,
      private val itemClickListener: OnItemClickListener<User>?,
      private val itemViewModel: ItemUserViewModel =
          ItemUserViewModel(itemClickListener)) : RecyclerView.ViewHolder(
      binding.root) {

    init {
      binding.viewModel = itemViewModel
    }

    fun bind(user: User?, position: Int) {
      itemViewModel.position = position
      itemViewModel.setData(user)
      binding.executePendingBindings()
    }
  }
}