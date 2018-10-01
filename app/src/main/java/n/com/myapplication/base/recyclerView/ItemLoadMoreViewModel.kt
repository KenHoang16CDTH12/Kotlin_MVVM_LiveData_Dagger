package n.com.myapplication.base.recyclerView

import androidx.databinding.ObservableBoolean

data class ItemLoadMoreViewModel(
    val visibleProgressBar: ObservableBoolean = ObservableBoolean(false))