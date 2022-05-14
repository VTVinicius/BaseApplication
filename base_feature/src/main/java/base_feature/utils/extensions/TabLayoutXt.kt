package base_feature.utils.extensions

import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayout

fun TabLayout.addTab(vararg title: String): TabLayout {
    title.forEach { addTab(newTab().setText(it)) }
    return this
}

fun TabLayout.addTab(list: List<String>, isPositionSelectable: (position: Int) -> Boolean) {
    list.forEachIndexed { position, text ->
        addTab(newTab().setText(text), isPositionSelectable(position))
    }
}

fun TabLayout.addTab(items: List<Pair<String, (TabLayout.Tab) -> Unit>>) {
    addTab(*(items.map { it.first }.toTypedArray()))
    addChangeListener { tab ->
        items.find { tab.text == it.first }?.second?.invoke(tab)
    }
}

fun TabLayout.setTextColorAt(index: Int, @ColorRes colorId: Int): TabLayout {
    getTextViewAt(index)?.setTextColor(ContextCompat.getColor(context, colorId))
    return this
}

fun TabLayout.getTextViewAt(index: Int): TextView? {
    return (((getChildAt(0) as? LinearLayout)?.getChildAt(index) as? LinearLayout)
        ?.getChildAt(1) as? TextView)
}

fun TabLayout.addChangeListener(
    onUnSelected: (TabLayout.Tab) -> Unit = {},
    onReSelected: (TabLayout.Tab) -> Unit = {},
    onSelected: (TabLayout.Tab) -> Unit
) {
    addOnTabSelectedListener(
        object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) = onReSelected(tab)
            override fun onTabUnselected(tab: TabLayout.Tab) = onUnSelected(tab)
            override fun onTabSelected(tab: TabLayout.Tab) = onSelected(tab)
        }
    )
}


fun TabLayout.changeFontWhenSelected(@FontRes normalFont: Int, @FontRes selectedFont: Int) {
    addOnTabSelectedListener(
        object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) = Unit

            override fun onTabUnselected(tab: TabLayout.Tab) {
                getTextViewAt(tab.position)?.typeface = ResourcesCompat.getFont(context, normalFont)
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                getTextViewAt(tab.position)?.typeface =
                    ResourcesCompat.getFont(context, selectedFont)
            }
        }
    )
}
