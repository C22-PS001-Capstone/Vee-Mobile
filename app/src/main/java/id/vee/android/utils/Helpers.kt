package id.vee.android.utils

import android.text.TextUtils
import android.widget.EditText

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
    if(view.text == null){
        view.error = "Field cannot be empty"
        return false
    }
    return true
}