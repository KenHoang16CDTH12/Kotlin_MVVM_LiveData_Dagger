package n.com.myapplication.base.recyclerView.diffCallback

import androidx.recyclerview.widget.DiffUtil
import n.com.myapplication.data.model.User
import javax.annotation.Nullable

class UserDiffCallback
(@Nullable private var olds: List<User>, @Nullable private var news: List<User>) : DiffUtil.Callback() {

  override fun getOldListSize(): Int {
    return olds.size
  }

  override fun getNewListSize(): Int {
    return news.size
  }


  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return olds[oldItemPosition].id == news[newItemPosition].id
  }


  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
    return olds[oldItemPosition] == news[newItemPosition]
  }


}