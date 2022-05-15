package com.example.feature.nomedatela

import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import base_feature.core.BaseFragment
import base_feature.utils.delegateproperties.navDirections
import com.example.feature.commom.navigation.ExampleNavigation
import com.example.feature.databinding.FragmentExampleBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExampleFragment : BaseFragment<FragmentExampleBinding>() {

    private val viewModel: ExampleViewModel by viewModel()
    private val navigation: ExampleNavigation by navDirections()


    override fun onCreateViewBinding(inflater: LayoutInflater): FragmentExampleBinding =
        FragmentExampleBinding.inflate(inflater)

    override fun setupView() {
        super.setupView()

    }

    override fun addObservers(owner: LifecycleOwner) {
        super.addObservers(owner)

//        exampleObserver(owner)
    }

//    private fun exampleObserver(owner: LifecycleOwner) {
//        viewModel.exampleViewState.onPostValue(owner,
//            onSuccess = {
//            },
//            onError = {
//
//            }
//        )
//    }

}
