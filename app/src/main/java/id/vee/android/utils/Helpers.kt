package id.vee.android.utils

import android.text.TextUtils
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import id.vee.android.domain.model.Token
import id.vee.android.ui.GeneralViewModel

fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.bearer(): String {
    return "Bearer $this"
}

fun Int.padStart(length: Int, padChar: Char = '0'): String {
    return this.toString().padStart(length, padChar)
}

fun checkEmptyEditText(view: EditText): Boolean {
    if (view.text == null) {
        view.error = "Field cannot be empty"
        return false
    }
    return true
}

fun getCurrentUnix(): Long {
    return System.currentTimeMillis() / 1000
}

fun checkTokenAvailability(
    viewModel: GeneralViewModel,
    token: Token,
    lifecycleOwner: LifecycleOwner,
    callback: (Token) -> Unit
) {
    val milis = (token.expiredAt) - getCurrentUnix()
    if (milis < 3) {
        viewModel.refreshToken(token.refreshToken)
        viewModel.refreshResponse.observe(lifecycleOwner) { response ->
            response.data?.let { tokenEntity ->
                token.accessToken = tokenEntity.accessToken
                token.expiredAt = getCurrentUnix() + tokenEntity.expiredAt
                viewModel.saveToken(token)
                callback(token)
            }
        }
    } else {
        callback(token)
    }
}