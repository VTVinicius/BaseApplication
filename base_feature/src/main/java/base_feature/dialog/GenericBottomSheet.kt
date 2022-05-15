package base_feature.dialog

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import base_feature.core.BaseBottomSheet
import base_feature.utils.extensions.getColor
import com.example.base_feature.databinding.GenericErrorBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.parcelize.Parcelize


class GenericBottomSheet : BaseBottomSheet<GenericErrorBottomSheetBinding>() {

    private val args by lazy { Args.fromBundle(arguments) }
    private var onButtonPressed: (() -> Unit)? = null

    override fun onCreateViewBinding(inflater: LayoutInflater): GenericErrorBottomSheetBinding {
        return GenericErrorBottomSheetBinding.inflate(inflater)
    }

    override fun setupView() {
        super.setupView()
        setErrorInformation()
    }

    private fun setErrorInformation() {
        binding.btnClose.apply {
            isVisible = args.closeButtonIsVisible
            setOnClickListener {
                dismiss()
            }
        }
        binding.genericErrorImageView.apply {
            isVisible = args.drawable != null
            args.drawable?.let { setBackgroundResource(it) }
        }
        binding.genericErrorTitle.text = args.title
        binding.genericErrorDescription.text = args.description
        args.textColor?.let {
            binding.genericErrorDescription.setTextColor(getColor(it))
        }
        binding.genericButton.run {
            isVisible = true
            text = args.buttonText
            setOnClickListener {
                onButtonPressed?.invoke()
                dismiss()
            }
        }
        binding.genericErrorButton.isVisible = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    companion object {

        @Parcelize
        private data class Args(
            val title: String = "",
            val description: CharSequence = "",
            val buttonText: String = "",
            @DrawableRes
            val drawable: Int? = null,
            val closeButtonIsVisible: Boolean = false,
            val textColor: Int? = null
        ) : Parcelable {
            fun toBundle() = Bundle().also { it.putParcelable(ARGS, this) }

            companion object {
                private const val ARGS = "args"

                fun fromBundle(args: Bundle?) = args?.getParcelable(ARGS) ?: Args()
            }
        }

        fun newInstance(
            title: String = "",
            description: CharSequence = "",
            buttonText: String = "",
            onPressed: (() -> Unit)? = null,
            @DrawableRes
            drawable: Int? = null,
            closeButtonIsVisible: Boolean = true,
            textColor: Int? = null
        ): GenericBottomSheet {
            val args = Args(
                title,
                description,
                buttonText,
                drawable,
                closeButtonIsVisible,
                textColor
            )
            return GenericBottomSheet().apply {
                arguments = args.toBundle()
                onButtonPressed = onPressed
            }
        }
    }
}