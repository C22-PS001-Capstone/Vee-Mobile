package id.vee.android.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class MyNumberEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private var decimalSeparator: Char = ','
    private var thousandSeparator: Char = '.'

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Do nothing.
            }

            override fun afterTextChanged(s: Editable) {
                removeTextChangedListener(this)

                try {
                    var givenString: String = s.toString()
                    if (givenString.contains(thousandSeparator)) {
                        givenString = givenString.replace(thousandSeparator.toString(), "")
                    }
                    val doubleVal: Double = givenString.toDouble()

                    val unusualSymbols = DecimalFormatSymbols()
                    unusualSymbols.decimalSeparator = decimalSeparator
                    unusualSymbols.groupingSeparator = thousandSeparator

                    val formatter = DecimalFormat("#,##0.##", unusualSymbols)
                    formatter.groupingSize = 3
                    val formattedString = formatter.format(doubleVal)

                    setText(formattedString)
                    text?.let { setSelection(it.length) }
                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                addTextChangedListener(this)
            }
            })
            textAlignment = View.TEXT_ALIGNMENT_VIEW_START
            paddingRight
        }
    }