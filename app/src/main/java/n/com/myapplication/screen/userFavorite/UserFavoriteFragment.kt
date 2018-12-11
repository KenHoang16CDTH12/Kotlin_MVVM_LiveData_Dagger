package n.com.myapplication.screen.userFavorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_user_favorite.*
import n.com.myapplication.R
import n.com.myapplication.base.BaseFragment
import n.com.myapplication.base.recyclerView.OnItemClickListener
import n.com.myapplication.data.model.User
import n.com.myapplication.databinding.FragmentUserFavoriteBinding
import n.com.myapplication.util.extension.notNull
import n.com.myapplication.util.liveData.Status
import n.com.myapplication.util.liveData.autoCleared
import n.com.myapplication.screen.user.UserAdapter


class UserFavoriteFragment : BaseFragment(), OnItemClickListener<User> {

    private lateinit var viewModel: UserFavoriteViewModel

    private var binding by autoCleared<FragmentUserFavoriteBinding>()
    private var adapter by autoCleared<UserAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = UserAdapter(context = context as AppCompatActivity).apply {
            registerItemClickListener(this@UserFavoriteFragment)
        }
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        viewModel = UserFavoriteViewModel.create(this, viewModelFactory)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_favorite, container,
                false)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun setUpView() {
        recyclerView.adapter = adapter
    }

    override fun bindView() {
        viewModel.repoList.observe(this, Observer { resource ->
            if (resource.status == Status.LOADING) {
                dialogManager?.hideLoading()
                resource.data.notNull {
                    adapter.updateData(it)
                }
            }
        })
    }

    override fun onItemViewClick(item: User, position: Int) {
        item.isFavorite = !item.isFavorite
        viewModel.pressFavorite(item)
    }

    companion object {
        fun newInstance() = UserFavoriteFragment()
        const val TAG = "UserFavoriteFragment"
    }

}
