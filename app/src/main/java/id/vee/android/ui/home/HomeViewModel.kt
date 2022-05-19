package id.vee.android.ui.home

import androidx.lifecycle.ViewModel
import id.vee.android.data.VeeDataSource
import id.vee.android.ui.GeneralViewModel

class HomeViewModel(
    private val repository: VeeDataSource
) : GeneralViewModel(repository) {
    
}