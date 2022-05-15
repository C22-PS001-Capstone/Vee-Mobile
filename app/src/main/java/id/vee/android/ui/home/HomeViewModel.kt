package id.vee.android.ui.home

import androidx.lifecycle.ViewModel
import id.vee.android.data.VeeDataSource

class HomeViewModel constructor(
    private val mVeeRepository: VeeDataSource
) : ViewModel() {
}