package id.vee.android.ui.home

import id.vee.android.data.local.ThemeInterface
import id.vee.android.domain.usecase.VeeUseCase
import id.vee.android.ui.GeneralViewModel

class HomeViewModel(
    useCase: VeeUseCase,
    pref: ThemeInterface
) : GeneralViewModel(useCase, pref)