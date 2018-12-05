package n.com.myapplication.base.recyclerView

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import n.com.myapplication.R
import n.com.myapplication.databinding.ItemLoadMoreBinding

abstract class LoadMoreAdapter<T>
constructor(context: Context) : BaseRecyclerViewAdapter<T, RecyclerView.ViewHolder>(context) {

  companion object {
    const val TYPE_PROGRESS = 0xFFFF
    const val TAG = "LoadMoreAdapter"
  }

  var isLoadMore = false

  @NonNull
  override fun getItemViewType(position: Int): Int {
    if (isLoadMore && position == bottomItemPosition()) {
      return TYPE_PROGRESS
    }
    return getItemViewTypeLM(position)
  }

  @NonNull
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    if (TYPE_PROGRESS == viewType) {
      val bottomBinding = DataBindingUtil.inflate<ItemLoadMoreBinding>(
          layoutInflater, R.layout.item_load_more, parent, false
      )
      return ItemLoadMoreViewHolder(bottomBinding)
    }
    return onCreateViewHolderLM(parent, viewType)
  }

  @NonNull
  override fun onBindViewHolder(@NonNull holder: RecyclerView.ViewHolder, position: Int) {
    if (holder.itemViewType == TYPE_PROGRESS) {
      (holder as ItemLoadMoreViewHolder).bind(isLoadMore)
      return
    }
    onBindViewHolderLM(holder, position)
  }

  fun bottomItemPosition(): Int {
    return itemCount - 1
  }

  fun onStartLoadMore() {
    if (dataList.isEmpty() || isLoadMore) return
    isLoadMore = true

    handler.post {
      dataList.add(null as T)
      notifyItemInserted(bottomItemPosition())
    }
  }

  fun onStopLoadMore(newSize: Int = 0) {
    if (!isLoadMore) return
    isLoadMore = false

    handler.post {
      dataList.removeAt(bottomItemPosition())
      val oldSize = dataList.size
      if (oldSize >= newSize) {
        notifyItemRemoved(oldSize)
      }
    }
  }

  class ItemLoadMoreViewHolder(
      private val binding: ItemLoadMoreBinding,
      private val itemViewModel: ItemLoadMoreViewModel = ItemLoadMoreViewModel()) : RecyclerView.ViewHolder(
      binding.root) {

    init {
      binding.viewModel = itemViewModel
    }

    fun bind(isLoadMore: Boolean) {
      itemViewModel.visibleProgressBar.set(isLoadMore)
      binding.executePendingBindings()
    }

  }

  protected abstract fun onCreateViewHolderLM(parent: ViewGroup,
      viewType: Int): RecyclerView.ViewHolder

  protected abstract fun onBindViewHolderLM(holder: RecyclerView.ViewHolder, position: Int)

  protected abstract fun getItemViewTypeLM(position: Int): Int
}