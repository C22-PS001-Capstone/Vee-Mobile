package id.vee.android.ui.home

import id.vee.android.data.VeeDataSource
import id.vee.android.data.local.ThemeInterface
import id.vee.android.ui.GeneralViewModel

class HomeViewModel(
    repository: VeeDataSource,
    pref: ThemeInterface
) : GeneralViewModel(repository, pref)