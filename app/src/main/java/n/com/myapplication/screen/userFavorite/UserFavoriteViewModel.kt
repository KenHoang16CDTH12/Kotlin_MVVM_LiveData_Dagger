package n.com.myapplication.screen.userFavorite

import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch
import n.com.myapplication.base.BaseViewModel
import n.com.myapplication.data.model.User
import n.com.myapplication.util.liveData.Resource
import n.com.myapplication.util.liveData.Status
import n.com.myapplication.data.source.repositories.AppDBRepository
import javax.inject.Inject

class UserFavoriteViewModel
@Inject constructor(
        private val appDB: AppDBRepository) : BaseViewModel() {

    var repoList = MediatorLiveData<Resource<MutableList<User>>>()

    init {
        viewModelScope?.launch {
            repoList.addSource(appDB.getUsers()) { response ->
                repoList.value = Resource.multiStatus(Status.LOADING, response.toMutableList())
            }
        }
    }

    fun pressFavorite(user: User) {
        viewModelScope?.launch {
            appDB.insertOrUpdateUser(user)
        }
    }

    companion object {
        fun create(fragment: Fragment,
                factory: ViewModelProvider.Factory): UserFavoriteViewModel {
            return ViewModelProvider(fragment, factory).get(UserFavoriteViewModel::class.java)
        }
    }
}