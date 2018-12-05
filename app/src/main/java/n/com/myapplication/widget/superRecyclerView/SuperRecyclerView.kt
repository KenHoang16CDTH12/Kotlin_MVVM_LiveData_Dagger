package n.com.myapplication.widget.superRecyclerView

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import n.com.myapplication.R
import n.com.myapplication.base.recyclerView.EndlessRecyclerOnScrollListener
import n.com.myapplication.base.recyclerView.LoadMoreAdapter
import n.com.myapplication.extension.notNull
import n.com.myapplication.util.Constant
import n.com.myapplication.util.LogUtils
import n.com.myapplication.widget.pullToRefresh.PullRefreshLayout

class SuperRecyclerView : FrameLayout {

  private var recyclerView: RecyclerView? = null
  private var onScrollListener: RecyclerView.OnScrollListener? = null
  private var swipeRefreshLayout: PullRefreshLayout? = null

  private var loadMoreAdapter: LoadMoreAdapter<*>

  private var isRefresh = false
  private var currentPage = Constant.PAGE_DEFAULT

  private var loadDataListener: LoadDataListener? = null

  constructor(@NonNull context: Context) : super(context) {
    initView()
  }

  constructor(@NonNull context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
    initView()
  }

  constructor(@NonNull context: Context, @Nullable attrs: AttributeSet,
      defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    initView()
  }

  private fun initView() {
    val view = LayoutInflater.from(context).inflate(R.layout.super_recyclerview, this, false)
    recyclerView = view.findViewById(R.id.super_recyclerView)
    recyclerView?.hasFixedSize()
    recyclerView?.itemAnimator = DefaultItemAnimator()
    swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
    swipeRefreshLayout?.setColorSchemeColors(*colorForSmart)
    addView(view)
  }

  fun setAdapter(adapter: RecyclerView.Adapter<*>) {
    loadMoreAdapter = adapter as LoadMoreAdapter<*>
    recyclerView?.adapter = adapter
  }

  fun setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
    if (layoutManager is GridLayoutManager) {
      val spanSize = layoutManager.spanCount
      layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
          return if (loadMoreAdapter.isLoadMore
              && position == loadMoreAdapter.bottomItemPosition()) {
            spanSize
          } else 1
        }
      }
    }
    recyclerView?.layoutManager = layoutManager

    onScrollListener = object : EndlessRecyclerOnScrollListener(layoutManager) {
      override fun onLoadMore(currentPage: Int) {
        startLoadMore()
      }
    }
    recyclerView?.addOnScrollListener(onScrollListener as EndlessRecyclerOnScrollListener)

    swipeRefreshLayout?.setOnRefreshListener { this.startRefreshData() }
  }


  fun setHasFixedSize(isFixed: Boolean) {
    recyclerView?.setHasFixedSize(isFixed)
  }

  fun setEnableSwipe(isEnable: Boolean) {
    swipeRefreshLayout?.isEnabled = isEnable
  }

  fun startRefreshData() {
    isRefresh = true
    currentPage = Constant.PAGE_DEFAULT

    swipeRefreshLayout?.setRefreshing(true)
    loadDataListener?.onRefreshData()
  }

  fun stopRefreshData() {
    if (!isRefresh) return
    isRefresh = false
    swipeRefreshLayout?.setRefreshing(false)
  }

  fun startLoadMore() {
    if (loadMoreAdapter.isLoadMore || isRefresh) {
      return
    }
    currentPage++
    loadMoreAdapter.onStartLoadMore()
    loadDataListener?.onLoadMore(currentPage)
  }

  fun stopLoadMore(newSize: Int = 0) {
    loadMoreAdapter.onStopLoadMore(newSize = newSize)
  }

  fun stopAllStatusLoadData() {
    stopRefreshData()
    stopLoadMore()
  }

  fun refreshAdapter(newSize: Int = 0) {
    loadMoreAdapter.notNull { adapter ->
      stopLoadMore()
      if (newSize == 0) adapter.clearData()
      resetState()
    }
  }

  fun disableAnimateRecyclerView() {
    (recyclerView?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
  }

  fun enableAnimateRecyclerView() {
    (recyclerView?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = true
  }

  fun resetState() {
    (onScrollListener as EndlessRecyclerOnScrollListener).reset()
  }

  fun setLoadDataListener(listener: LoadDataListener) {
    loadDataListener = listener
  }

  fun onRealse() {

  }

  interface LoadDataListener {
    fun onLoadMore(page: Int)

    fun onRefreshData()
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    loadDataListener = null
    loadMoreAdapter.unRegisterItemClickListener()
    swipeRefreshLayout?.setOnRefreshListener(null)
    swipeRefreshLayout = null
    recyclerView?.removeOnScrollListener(onScrollListener as EndlessRecyclerOnScrollListener)
    recyclerView = null
    LogUtils.d(TAG, Constant.RELEASED)
  }

  companion object {

    private val TAG = SuperRecyclerView::class.java.simpleName

    val colorForSmart = intArrayOf(Color.rgb(30, 186, 177), Color.rgb(0xF7, 0xD2, 0x3E),
        Color.rgb(0xF7, 0xD2, 0x3E), Color.rgb(0x34, 0xA3, 0x50))

    val colorForRing = intArrayOf(Color.rgb(0xF7, 0xD2, 0x3E), Color.rgb(0xF7, 0xD2, 0x3E),
        Color.rgb(30, 186, 177), Color.rgb(0x34, 0xA3, 0x50))
  }
}
