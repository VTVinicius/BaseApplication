package base_feature.utils.extensions

import android.app.Activity
import android.app.DatePickerDialog
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Base64
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.example.base_feature.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

fun Fragment.hideActionBar() = (activity as? AppCompatActivity)?.supportActionBar?.hide()

fun Fragment.showActionBar() = (activity as? AppCompatActivity)?.supportActionBar?.show()

fun Fragment.setToolbarLogo(@DrawableRes resId: Int?) {
    if (resId == null) {
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayUseLogoEnabled(false)
    } else {
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayUseLogoEnabled(true)
        (activity as? AppCompatActivity)?.supportActionBar?.setLogo(resId)
    }
}

fun Fragment.showSoftKeyboard(view: View, forceOpen: Boolean = false) {
    val inputMethodManager =
        requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    view.requestFocus()

    if (forceOpen) {
        inputMethodManager?.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    } else {
        inputMethodManager?.showSoftInput(view, 0)
    }
}

fun Fragment.hideKeyboard(forceClose: Boolean = false) {
    val inputManager =
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            ?: return
    val currentFocusedView = requireActivity().currentFocus ?: return
    if (forceClose) {
        inputManager.hideSoftInputFromWindow(currentFocusedView.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    } else {
        inputManager.hideSoftInputFromWindow(currentFocusedView.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun Fragment.toast(message: String): Toast = Toast.makeText(context, message, Toast.LENGTH_LONG)

fun Fragment.setToolbarTitle(title: String) {
    (context as? AppCompatActivity)?.title = title
}

fun Fragment.setNavigationIcon(id: Int?) {
    (activity as AppCompatActivity?)?.supportActionBar?.apply {
        id?.let { setHomeAsUpIndicator(it) }
        setDisplayHomeAsUpEnabled(id != null)
    }
    showActionBar()
}

fun Fragment.getFont(fontId: Int) = ResourcesCompat.getFont(requireContext(), fontId)

fun Fragment.makeCall(phoneNumber: String) {
    startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")))
}

fun Fragment.openWhatsapp(number: String, message: String = "") {
    val packageName = "com.whatsapp"
    if (!isPackageInstalled(packageName)) {
        openPackageInStore(packageName)
        return
    }

    startActivity(
        Intent(Intent.ACTION_VIEW, Uri.parse("smsTo:$number/*")).apply {
            data = Uri.parse("http://api.whatsapp.com/send?phone=$number&text=$message")
        }
    )
}

fun Fragment.isPackageInstalled(packageName: String) = try {
    requireContext().packageManager.getPackageInfo(packageName, 0)
    true
} catch (e: PackageManager.NameNotFoundException) {
    false
}

fun Fragment.openPackageInStore(packageName: String) = try {
    val appStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
    appStoreIntent.setPackage("com.android.vending")
    startActivity(appStoreIntent)
} catch (exception: ActivityNotFoundException) {
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    } catch (e: Exception) {
        Unit
    }
}

fun Fragment.getColor(@ColorRes colorId: Int) = ContextCompat.getColor(requireContext(), colorId)
fun Fragment.getDrawable(@ColorRes drawableId: Int) =
    ContextCompat.getColor(requireContext(), drawableId)

fun Fragment.getDrawableResource(drawableId: Int) =
    ContextCompat.getDrawable(requireContext(), drawableId).apply {
        this?.setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    }

fun Fragment.openShareIntent(message: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, message)
        type = "text/plain"
    }
    startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.send_to)))
}

fun Fragment.copyToClipboard(text: String, label: String = "clipboard") {
    val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
}

fun Fragment.openDetailInSettings() = Intent().run {
    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    addCategory(Intent.CATEGORY_DEFAULT)
    data = Uri.parse("package:" + requireContext().packageName)
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    startActivity(this)
}

fun Fragment.softInputAdjustNothing() {
    requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
}

fun Fragment.softInputAdjustResize() {
    requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
}

fun Fragment.softInputAdjustPan() {
    requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
}

fun Fragment.addFlags(flags: List<Int>) {
    flags.map { requireActivity().window.addFlags(it) }
}

fun Fragment.clearFlags(flags: List<Int>) {
    flags.map { requireActivity().window.clearFlags(it) }
}


fun Fragment.addOnBackPressedCallback(
    owner: LifecycleOwner,
    onBackPressed: (() -> Unit)?
) {
    (requireActivity() as? AppCompatActivity)?.onBackPressedDispatcher?.addCallback(
        owner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed?.invoke()
            }
        }
    )
}

fun Fragment.createDatePicker(
    date: Date? = null,
    minDate: Long? = null,
    maxDate: Date? = null,
    onDateSet: (date: Date) -> Unit,
    onCancelListener: (dialog: DatePickerDialog) -> Unit = {}
) {
    val calendar: Calendar = Calendar.getInstance().apply {
        time = date ?: Date()
    }

    val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        onDateSet(calendar.time)
    }

    val dialog = DatePickerDialog(
        requireContext(),
        dateSetListener,
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    dialog.window

    if (minDate != null) {
        dialog.datePicker.minDate = minDate
    }

    if (maxDate != null) {
        val calendarMax: Calendar = Calendar.getInstance().apply {
            time = maxDate
            this.set(Calendar.HOUR_OF_DAY, 23)
            this.set(Calendar.MINUTE, 59)
            this.set(Calendar.SECOND, 59)
        }

        dialog.datePicker.maxDate = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            calendarMax.timeInMillis
        } else {
            maxDate.time
        }
    }

    dialog.show()
    dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
        .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
    dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        .setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
    onCancelListener(dialog)
}

fun Fragment.openUrl(url: String) {
    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
    )
}

fun Fragment.openPDFUrl(url: String) {
    startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://docs.google.com/gview?embedded=true&url=$url")
        )
    )
}

fun Fragment.sharePdfFromBase64(fileName: String, base64String: String) {

    context?.let { context ->

        val imageFilePackage = "images"

        try {

            val cachePath = File(
                context.cacheDir,
                imageFilePackage
            ).also {
                it.mkdirs()
            }

            FileOutputStream(
                "$cachePath/$fileName"
            ).apply {
                write(Base64.decode(base64String, Base64.DEFAULT))
                flush()
                close()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        val contentUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            File(
                File(context.cacheDir, imageFilePackage),
                fileName
            )
        ) ?: return

        startActivity(
            Intent.createChooser(
                Intent().apply {
                    action = Intent.ACTION_SEND
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    setDataAndType(
                        contentUri,
                        context.contentResolver.getType(contentUri)
                    )
                    putExtra(Intent.EXTRA_STREAM, contentUri)
                },
                getString(R.string.share_with)
            )
        )

    }

}

fun <T> Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(entry: Int? = null, result: T, key: String = "result") {
    entry?.let {
        findNavController().getBackStackEntry(it).savedStateHandle.set<T>(key, result)
    } ?: run {
        findNavController().previousBackStackEntry?.savedStateHandle?.set<T>(key, result)
    }
}

fun <T> Fragment.clearNavigationResult(key: String = "result") {
    findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(key)
}

fun Fragment.getNavigatorGraphId() = findNavController().currentDestination?.parent?.id ?: 0

fun Fragment.getResourceName(id: Int): String = resources.getResourceEntryName(id)

fun Fragment.getIdByName(id: Int) =
    resources.getIdentifier(getResourceName(id), "id", context?.packageName)