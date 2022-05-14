package base_feature.dialog

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import base_feature.core.BaseBottomSheet
import base_feature.utils.extensions.getDrawableResource
import base_feature.utils.extensions.removeDrag
import base_feature.utils.extensions.setStateExpanded
import com.example.base_feature.databinding.GenericErrorBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.parcelize.Parcelize


class GenericErrorBottomSheet: BaseBottomSheet<GenericErrorBottomSheetBinding>() {

    private val args by lazy { Args.fromBundle(arguments) }
    private var onButtonPressed: (() -> Unit)? = null
    private var onBackButtonPressed: (() -> Unit)? = null

    override fun onCreateViewBinding(inflater: LayoutInflater): GenericErrorBottomSheetBinding {
        return GenericErrorBottomSheetBinding.inflate(inflater)
    }

    override fun onResume() {
        super.onResume()
        removeDrag()
        setStateExpanded()
    }

    override fun setupView() {
        super.setupView()

        setErrorInformation()
    }

    private fun setErrorInformation() {
        binding.btnBack.apply {
            isVisible = args.backButtonIsVisible
            setOnClickListener {
                onBackButtonPressed?.invoke()
                dismiss()
            }
        }
        binding.genericErrorTitle.text = args.title
        binding.genericErrorDescription.text = args.description
        binding.genericErrorButton.run {
            text = args.buttonText

            setOnClickListener {
                onButtonPressed?.invoke()
                dismiss()
            }

            args.buttonDrawable.let {
                if (it == null) {
                    icon = null
                    return
                }

                icon = getDrawableResource(it)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { view ->
                setupFullHeight(view)
            }
        }
        return dialog
    }


    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    companion object {

        @Parcelize
        private data class Args(
            val title: String = "",
            val description: String = "",
            val buttonText: String = "",
            @DrawableRes
            val buttonDrawable: Int? = null,
            val backButtonIsVisible: Boolean = false
        ) : Parcelable {
            fun toBundle() = Bundle().also { it.putParcelable(ARGS, this) }

            companion object {
                private const val ARGS = "args"

                fun fromBundle(args: Bundle?) = args?.getParcelable(ARGS) ?: Args()
            }
        }

        fun newInstance(
            title: String = "",
            description: String = "",
            buttonText: String = "Voltar",
            onPressed: (() -> Unit)? = null,
            @DrawableRes
            drawable: Int? = null,
            backButtonIsVisible: Boolean = false,
            backButtonListener: (() -> Unit)? = null
        ): GenericErrorBottomSheet {
            val args = Args(
                title,
                description,
                buttonText,
                drawable,
                backButtonIsVisible
            )
            return GenericErrorBottomSheet().apply {
                arguments = args.toBundle()
                onButtonPressed = onPressed
                onBackButtonPressed = backButtonListener
            }
        }
    }
}