package n.com.myapplication.base.recyclerView

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import n.com.myapplication.extension.notNull

/**
 * Base Adapter.
 *
 * @param <V> is a type extend from {@link RecyclerView.ViewHolder}
 * @param <T> is a Object
 */

abstract class BaseRecyclerViewAdapter<T, V : RecyclerView.ViewHolder>
constructor(
    protected val context: Context,
    protected var layoutInflater: LayoutInflater = LayoutInflater.from(context),
    protected var dataList: MutableList<T> = mutableListOf()) : RecyclerView.Adapter<V>() {

  protected var itemClickListener: OnItemClickListener<T>? = null

  protected var handler = Handler()
  protected lateinit var runnable: Runnable

  override fun getItemCount(): Int {
    return dataList.size
  }

  fun getData(): MutableList<T> {
    return dataList
  }

  fun updateData(newData: MutableList<T>?, diffResult: DiffUtil.DiffResult) {
    runnable = Runnable {
      newData.notNull { it ->
        diffResult.dispatchUpdatesTo(this)
        dataList.clear()
        dataList.addAll(it)
      }
    }
    handler.post(runnable)
  }

  fun clearData() {
    dataList.notNull {
      it.clear()
      notifyDataSetChanged()
    }
  }

  fun getItem(position: Int): T? {
    return if (position < 0 || position >= dataList.size) {
      null
    } else dataList[position]
  }

  fun addItem(data: T, position: Int) {
    dataList.add(position, data)
    notifyItemInserted(position)
  }

  fun removeItem(position: Int) {
    if (position < 0 || position >= dataList.size) {
      return
    }
    dataList.removeAt(position)
    notifyItemRemoved(position)
  }

  fun replaceItem(item: T, position: Int) {
    if (position < 0 || position >= dataList.size) {
      return
    }
    dataList[position] = item
    notifyItemChanged(position)
  }

  fun registerItemClickListener(onItemClickListener: OnItemClickListener<T>) {
    itemClickListener = onItemClickListener
  }

  fun unRegisterItemClickListener() {
    itemClickListener = null
  }

  fun onClearCallBackLoadMore() {
    handler.notNull {
      handler.removeCallbacks(runnable)
    }
  }

}