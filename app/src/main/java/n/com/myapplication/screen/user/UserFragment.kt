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
import n.com.myapplication.base.recyclerView.OnItemClickListener
import n.com.myapplication.base.recyclerView.diffCallback.UserDiffCallback
import n.com.myapplication.data.model.User
import n.com.myapplication.databinding.FragmentUserBinding
import n.com.myapplication.extension.notNull
import n.com.myapplication.extension.showToast
import n.com.myapplication.liveData.Status
import n.com.myapplication.liveData.autoCleared
import n.com.myapplication.widget.superRecyclerView.SuperRecyclerView


class UserFragment : BaseFragment(), OnItemClickListener<User>, SuperRecyclerView.LoadDataListener {

  private lateinit var viewModel: UserViewModel

  private var binding by autoCleared<FragmentUserBinding>()
  private var adapter by autoCleared<UserAdapter>()

  override fun createView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View {
    viewModel = UserViewModel.create(this, viewModelFactory)
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
    binding.viewModel = viewModel
    binding.setLifecycleOwner(this)
    return binding.root
  }

  override fun setUpView() {
    adapter = UserAdapter(context = context!!)
    adapter.registerItemClickListener(this)

    recyclerView.setAdapter(adapter)
    recyclerView.setLayoutManager(LinearLayoutManager(context?.applicationContext))
    recyclerView.setLoadDataListener(this)

    edtSearch.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_SEARCH) {
        recyclerView.resetState()
        viewModel.searchUser(Status.REFRESH_DATA)
        return@setOnEditorActionListener true
      }
      return@setOnEditorActionListener false
    }

    viewModel.initRxSearch(editText = edtSearch)

    viewModel.query.value = "cat"
    viewModel.searchUser(Status.LOADING)
  }

  override fun bindView() {
    viewModel.repoList.observe(this, Observer { resource ->
      val data = resource.data
      when (resource.status) {
        Status.SUCCESS -> {
          dialogManager?.hideLoading()
          data.notNull { updateData(it) }
        }
        Status.LOADING -> {
          dialogManager?.hideLoading()
          data.notNull { updateData(it) }
        }
        Status.SEARCH_DATA -> {
          data.notNull {
            recyclerView.refreshAdapter(newSize = it.size)
            updateData(it)
          }
        }
        Status.REFRESH_DATA -> {
          data.notNull {
            recyclerView.stopRefreshData()
            recyclerView.refreshAdapter(newSize = it.size)
            updateData(it)
          }
        }
        Status.LOAD_MORE -> {
          data.notNull {
            recyclerView.stopLoadMore(newSize = it.size)
            updateData(it)
          }
        }
        Status.ERROR -> {
          recyclerView.stopAllStatusLoadData()
          resource.error?.getMessageError().notNull { activity?.showToast(it) }
        }
      }
    })
  }

  override fun onItemViewClick(item: User, position: Int) {
    activity?.showToast(item.fullName + "")
  }

  override fun onLoadMore(page: Int) {
    viewModel.searchUser(Status.LOAD_MORE, page = page)
  }

  override fun onRefreshData() {
    viewModel.searchUser(Status.REFRESH_DATA)
  }

  private fun updateData(newData: MutableList<User>) {
    val callBack = UserDiffCallback(adapter.getData(), newData)
    val diffResult = DiffUtil.calculateDiff(callBack)
    adapter.updateData(newData = newData, diffResult = diffResult)
  }

  companion object {
    fun newInstance() = UserFragment()
    const val TAG = "UserFragment"
  }

}
