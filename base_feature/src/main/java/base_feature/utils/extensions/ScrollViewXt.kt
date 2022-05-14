package base_feature.utils.extensions

import androidx.core.widget.NestedScrollView

fun NestedScrollView.moveOnTop(){
    this.post {
        this.fling(0)
        this.smoothScrollTo(0, 0)
    }
}

fun NestedScrollView.moveOnBottom(height: Int){
    this.post {
        this.fling(0)
        this.smoothScrollTo(0, height)
    }
}