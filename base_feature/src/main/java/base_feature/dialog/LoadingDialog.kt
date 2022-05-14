package base_feature.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import base_feature.utils.extensions.setGone
import com.example.base_feature.databinding.LoadingDialogBinding

class LoadingDialog: BaseFullScreenDialog() {


    var isLoadingVisible = true

    private var _binding: LoadingDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = LoadingDialogBinding.inflate(LayoutInflater.from(context))
        return AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    override fun onStart() {
        super.onStart()
        if (!isLoadingVisible) binding.dialogProgressBar.setGone()
        dialog?.window?.run {
            dialog?.setCancelable(false)
            setBackgroundDrawableResource(android.R.color.transparent)
            attributes = attributes.run { this }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}