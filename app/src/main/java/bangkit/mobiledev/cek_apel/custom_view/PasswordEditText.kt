package bangkit.mobiledev.cek_apel.custom_view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import bangkit.mobiledev.cek_apel.R

class PasswordEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val errorMsg = context.getString(R.string.password_short)
                if (s.length < 6) {
                    setError(errorMsg, null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
    }
}