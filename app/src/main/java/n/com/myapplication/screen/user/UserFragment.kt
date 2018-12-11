package n.com.myapplication.screen.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_user.*
import n.com.myapplication.R
import n.com.myapplication.base.BaseFragment
import n.com.myapplication.base.recyclerView.OnItemClickListener
import n.com.myapplication.data.model.User
import n.com.myapplication.databinding.FragmentUserBinding
import n.com.myapplication.util.extension.isNull
import n.com.myapplication.util.extension.notNull
import n.com.myapplication.util.liveData.Status
import n.com.myapplication.util.liveData.autoCleared
import n.com.myapplication.widget.superRecyclerView.SuperRecyclerView


class UserFragment : BaseFragment(), OnItemClickListener<User>, SuperRecyclerView.LoadDataListener {

    private lateinit var viewModel: UserViewModel

    private var binding by autoCleared<FragmentUserBinding>()
    private var adapter by autoCleared<UserAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = UserAdapter(context = context as AppCompatActivity).apply {
            registerItemClickListener(this@UserFragment)
        }
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        viewModel = UserViewModel.create(this, viewModelFactory)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun setUpView() {
        recyclerView.apply {
            setAdapter(adapter)
            setLayoutManager(LinearLayoutManager(context?.applicationContext))
            setLoadDataListener(this@UserFragment)
        }

        edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchUser(Status.REFRESH_DATA)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        viewModel.initRxSearch(editText = edtSearch)

        viewModel.repoList.value?.data.isNull {
            viewModel.query.value = "cat"
            viewModel.searchUser(Status.LOADING)
        }
    }

    override fun bindView() {
        viewModel.repoList.observe(this, Observer { resource ->
            val data = resource.data
            when (resource.status) {
                Status.SUCCESS -> {
                    dialogManager?.hideLoading()
                    data.notNull { adapter.updateData(it) }
                }
                Status.LOADING -> {
                    dialogManager?.hideLoading()
                    data.notNull { adapter.updateData(it) }
                }
                Status.SEARCH_DATA -> {
                    data.notNull {
                        recyclerView.refreshAdapter(newSize = it.size)
                        adapter.updateData(it)
                    }
                }
                Status.REFRESH_DATA -> {
                    data.notNull {
                        recyclerView.stopRefreshData()
                        recyclerView.refreshAdapter(newSize = it.size)
                        adapter.updateData(it)
                    }
                }
                Status.LOAD_MORE -> {
                    data.notNull {
                        recyclerView.stopLoadMore()
                        adapter.updateData(it)
                    }
                }
                Status.ERROR -> {
                    recyclerView.stopAllStatusLoadData()
                    onHandleError(resource.error)
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        recyclerView.stopAllStatusLoadData()
    }

    override fun onItemViewClick(item: User, position: Int) {
        item.isFavorite = !item.isFavorite
        viewModel.pressFavorite(item)
    }

    override fun onLoadMore(page: Int) {
        viewModel.searchUser(Status.LOAD_MORE, page = page)
    }

    override fun onRefreshData() {
        viewModel.searchUser(Status.REFRESH_DATA)
    }

    companion object {
        fun newInstance() = UserFragment()
        const val TAG = "UserFragment"
    }

}
