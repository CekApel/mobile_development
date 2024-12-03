package bangkit.mobiledev.cek_apel.custom_view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import bangkit.mobiledev.cek_apel.R

class EmailEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val errorMsg = context.getString(R.string.email_invalid)
                if (!isValidEmail(s.toString())) {
                    setError(errorMsg, null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}"
        return email.matches(Regex(emailPattern))
    }
}