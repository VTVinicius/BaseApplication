package com.example.navigation.navigatiors.feature

import androidx.fragment.app.Fragment
import com.example.feature.commom.navigation.ExampleNavigation
import com.example.navigation.R
import com.example.navigation.utils.popBackStack

class ExampleNavigationImpl(
    val fragment: Fragment
) : ExampleNavigation {

    override fun goToLocation() {
        fragment.popBackStack(
            R.id.exampleFragment
        )
    }
}