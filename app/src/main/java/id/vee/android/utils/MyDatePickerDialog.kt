package id.vee.android.utils

import android.app.DatePickerDialog
import android.os.Build
import androidx.fragment.app.FragmentActivity
import java.util.*

class MyDatePickerDialog(
    activity: FragmentActivity,
    onShowDateClicked: (Int, Int, Int) -> Unit
) {
    private val calendar = Calendar.getInstance()
    private val year: Int
        get() = calendar.get(Calendar.YEAR)
    private val month: Int
        get() = calendar.get(Calendar.MONTH)
    private val day: Int
        get() = calendar.get(Calendar.DAY_OF_MONTH)

    private val dialog = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        DatePickerDialog(
            activity,
            { _, year, month, day -> onShowDateClicked.invoke(year, month, day) },
            year,
            month,
            day
        )
    } else {
        DatePickerDialog(activity).apply {
            setOnDateSetListener { _, year, month, day ->
                onShowDateClicked.invoke(year, month, day)
            }
        }
    }

    fun show() {
        dialog.show()
    }
}