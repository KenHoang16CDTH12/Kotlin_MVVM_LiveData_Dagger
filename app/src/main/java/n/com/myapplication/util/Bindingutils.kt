package n.com.myapplication.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import n.com.myapplication.extension.notNull

object BindingUtils {

  @JvmStatic
  @BindingAdapter("app:imageUrl")
  fun setImageUrl(imageView: ImageView, url: String) {
    url.notNull {
      GlideApp.with(imageView.context).load(it).into(imageView)
    }
  }

  @JvmStatic
  @BindingAdapter("app:visibility")
  fun setVisible(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.GONE
  }

  @JvmStatic
  @BindingAdapter("app:invisible")
  fun setInvisible(view: View, show: Boolean) {
    view.visibility = if (show) View.VISIBLE else View.INVISIBLE
  }

  @JvmStatic
  @BindingAdapter("app:isRefreshing")
  fun setSwipeRefreshing(view: SwipeRefreshLayout, isRefreshing: Boolean) {
    view.isRefreshing = isRefreshing
  }

  @JvmStatic
  @BindingAdapter("app:hasFixedSize")
  fun setHasFixedSize(view: RecyclerView, isFixed: Boolean) {
    view.setHasFixedSize(isFixed)
  }

}