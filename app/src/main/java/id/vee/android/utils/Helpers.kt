package id.vee.android.utils

import android.text.TextUtils
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import id.vee.android.domain.model.Token
import id.vee.android.ui.GeneralViewModel
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*


fun String.toMD5(): String {
    try {
        // Create MD5 Hash
        val digest: MessageDigest = MessageDigest
            .getInstance("MD5")
        digest.update(this.toByteArray())
        val messageDigest: ByteArray = digest.digest()

        // Create Hex String
        val hexString = StringBuilder()
        for (aMessageDigest in messageDigest) {
            var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
            while (h.length < 2) h = "0$h"
            hexString.append(h)
        }
        return hexString.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return ""
}

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

fun String.formatDate(format: String = "dd MMM yyyy"): String? {
    val dateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.parse(this)
    return dateTime?.let {
        SimpleDateFormat(format, Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }.format(it)
    }
}

fun String.trimDottedString(): String {
    return if (this.contains(".")) {
        this.replace(".", "")
    } else
        this
}