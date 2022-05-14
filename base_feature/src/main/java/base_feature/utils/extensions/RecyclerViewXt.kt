package base_feature.utils.extensions

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.enableSnap(){

    PagerSnapHelper().attachToRecyclerView(this)
}
