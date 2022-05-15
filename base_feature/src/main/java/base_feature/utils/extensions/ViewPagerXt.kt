package base_feature.utils.extensions

import androidx.viewpager.widget.ViewPager

fun ViewPager.onPageSelected(pageSelected: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) = Unit

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) = Unit

        override fun onPageSelected(position: Int) {
            pageSelected(position)
        }
    })
}