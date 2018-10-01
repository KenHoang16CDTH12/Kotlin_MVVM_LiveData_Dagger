package n.com.myapplication.screen.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_user.*
import n.com.myapplication.R
import n.com.myapplication.base.BaseFragment
import n.com.myapplication.base.recyclerView.EndlessRecyclerOnScrollListener
import n.com.myapplication.base.recyclerView.OnItemClickListener
import n.com.myapplication.base.recyclerView.diffCallback.UserDiffCallback
import n.com.myapplication.data.model.User
import n.com.myapplication.databinding.FragmentUserBinding
import n.com.myapplication.extension.notNull
import n.com.myapplication.extension.showToast
import n.com.myapplication.liveData.autoCleared


class UserFragment : BaseFragment(), OnItemClickListener<User> {

  private lateinit var viewModel: UserViewModel
  private lateinit var scrollListener: EndlessRecyclerOnScrollListener

  private var binding by autoCleared<FragmentUserBinding>()
  private var adapter by autoCleared<UserAdapter>()

  override fun createView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    viewModel = UserViewModel.create(this, viewModelFactory)
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
    binding.viewModel = viewModel
    return binding.root
  }

  override fun setUpView() {
    adapter = UserAdapter(context = context!!)
    adapter.registerItemClickListener(this)

    val layoutManager = LinearLayoutManager(context?.applicationContext)
    scrollListener = object : EndlessRecyclerOnScrollListener(layoutManager) {
      override fun onLoadMore(currentPage: Int) {
        viewModel.searchUser(isLoadMore = true)
      }
    }
    recyclerView.adapter = adapter
    recyclerView.layoutManager = layoutManager
    recyclerView.hasFixedSize()
    recyclerView.addOnScrollListener(scrollListener)

    swipeLayout.setColorSchemeColors(resources.getColor(R.color.colorAccent))
    swipeLayout.setOnRefreshListener {
      adapter.clearData()
      scrollListener.reset()
      viewModel.searchUser(isRefresh = true)
    }

    edtSearch.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_SEARCH) {
        adapter.clearData()
        scrollListener.reset()
        viewModel.searchUser(isLoading = true)
        return@setOnEditorActionListener true
      }
      return@setOnEditorActionListener false
    }

    viewModel.initRxSearch(editText = edtSearch)
  }

  override fun bindView() {
    viewModel.isLoading.observe(this, Observer { isLoading ->
      if (isLoading) showLoading() else hideLoading()
    })

    viewModel.isLoadMore.observe(this, Observer { isLoadMore ->
      if (isLoadMore) adapter.onStartLoadMore() else adapter.onStopLoadMore()
    })

    viewModel.isRefresh.observe(this, Observer { isRefresh ->
      swipeLayout.isRefreshing = isRefresh
      if (isRefresh) showLoading() else hideLoading()
    })

    viewModel.isError.observe(this, Observer { isError ->
      isError.getMessageError().notNull { activity?.showToast(it) }
    })

    viewModel.repoList.observe(this, Observer { newData ->
      val diffResult = DiffUtil.calculateDiff(UserDiffCallback(adapter.getData(), newData))
      adapter.addData(newData = newData, diffResult = diffResult)
    })
  }

  override fun onDestroy() {
    super.onDestroy()
    recyclerView.removeOnScrollListener(scrollListener)
    swipeLayout.setOnRefreshListener(null)
  }


  override fun onItemViewClick(item: User, position: Int) {
    activity?.showToast(item.fullName + "")
  }


  companion object {
    fun newInstance() = UserFragment()
    const val TAG = "UserFragment"
  }

}
