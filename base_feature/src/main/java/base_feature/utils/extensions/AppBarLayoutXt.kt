package base_feature.utils.extensions

import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs


/**
 * Add an action which will be invoked when AppBar is scrolled
 *
 * @param event has a Float param that represents the scroll percentage. "1" represents 100% and 0 represents 0%.
 */
fun AppBarLayout.onAppBarScroll(event: (Float) -> Unit) {
    addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { layout, offset ->
        event((offset / layout.totalScrollRange.toFloat()) + 1)
    })
}


/**
 * Add an action which will be invoked when AppBar is expanded (offset == 0)
 *
 */
fun AppBarLayout.onAppBarExpand(event: () -> Unit) {
    addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
        if (offset == 0) event()
    })
}

/**
 * Add an action which will be invoked when AppBar is collapse (offset == max height)
 *
 */
fun AppBarLayout.onAppBarCollapse(event: () -> Unit) {
    addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
        if (abs(offset) >= totalScrollRange) event()
    })
}

/**
 * Add an action which will be invoked when AppBar is idle (offset is between 0 and max height)
 *
 * @param event has a Float param that represents the scroll percentage. "1" represents 100% and 0 represents 0%.
 */
fun AppBarLayout.onAppBarIdle(event: (Float) -> Unit) {
    addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { layout, offset ->
        if (offset != 0 && abs(offset) < totalScrollRange) {
            event((offset / layout.totalScrollRange.toFloat()) + 1)
        }
    })
}
