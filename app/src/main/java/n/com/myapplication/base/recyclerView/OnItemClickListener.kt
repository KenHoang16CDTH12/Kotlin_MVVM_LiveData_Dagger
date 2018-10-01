package n.com.myapplication.base.recyclerView

import n.com.myapplication.util.Constant.POSITION_DEFAULT

/**
 * OnItemClickListener
 *
 * @param <T> Data from item click
</T> */

interface OnItemClickListener<T> {
  fun onItemViewClick(item: T, position: Int = POSITION_DEFAULT)
}