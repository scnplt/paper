package dev.sertan.android.paper.customview

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.widget.doAfterTextChanged
import dev.sertan.android.paper.R
import dev.sertan.android.paper.databinding.LayoutPaperInputBinding

internal class PaperInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = LayoutPaperInputBinding.inflate(LayoutInflater.from(context), this)

    var endIconMode: Int
        get() = binding.inputLayout.endIconMode
        set(value) {
            binding.inputLayout.endIconMode = value
        }

    var error: CharSequence? = null

    var errorEnabled: Boolean
        get() = binding.inputLayout.isErrorEnabled
        set(value) {
            with(binding.inputLayout) {
                isErrorEnabled = value
                error = this@PaperInputLayout.error.takeIf { value }
            }
        }

    var hint: CharSequence?
        get() = binding.inputLayout.hint
        set(value) {
            binding.inputLayout.hint = value
        }

    var inputText: CharSequence?
        get() = binding.editText.text
        set(value) {
            binding.editText.setText(value)
        }

    var inputType: Int
        get() = binding.editText.inputType
        set(value) {
            binding.editText.inputType = value
        }

    var maxLength: Int
        get() = binding.editText.maxWidth
        set(value) {
            binding.editText.maxWidth = value
        }

    var maxLines: Int
        get() = binding.editText.maxLines
        set(value) {
            binding.editText.maxLines = value
        }

    var prefixText: CharSequence?
        get() = binding.inputLayout.prefixText
        set(value) {
            binding.inputLayout.prefixText = value
        }

    var suffixText: CharSequence?
        get() = binding.inputLayout.suffixText
        set(value) {
            binding.inputLayout.suffixText = value
        }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.PaperInputLayout).use {
            endIconMode = it.getInt(R.styleable.PaperInputLayout_endIconMode, endIconMode)
            error = it.getString(R.styleable.PaperInputLayout_error)
            hint = it.getString(R.styleable.PaperInputLayout_hint)
            inputText = it.getString(R.styleable.PaperInputLayout_inputText)
            inputType = it.getInt(R.styleable.PaperInputLayout_android_inputType, inputType)
            maxLength = it.getInt(R.styleable.PaperInputLayout_android_maxLength, maxLength)
            maxLines = it.getInt(R.styleable.PaperInputLayout_maxLines, maxLines)
            prefixText = it.getString(R.styleable.PaperInputLayout_prefixText)
            suffixText = it.getString(R.styleable.PaperInputLayout_suffixText)
        }
    }

    fun doAfterTextChanged(listener: (Editable?) -> Unit) {
        binding.editText.doAfterTextChanged(listener)
    }
}
