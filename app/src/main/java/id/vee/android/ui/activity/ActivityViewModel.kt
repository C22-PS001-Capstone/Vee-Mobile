package id.vee.android.ui.activity

import androidx.lifecycle.ViewModel
import id.vee.android.data.VeeDataSource

class ActivityViewModel constructor(
    private val mVeeRepository: VeeDataSource
) : ViewModel() {
}
