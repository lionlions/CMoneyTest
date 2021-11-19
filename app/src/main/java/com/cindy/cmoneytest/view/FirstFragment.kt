package com.cindy.cmoneytest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.cindy.cmoneytest.R
import com.cindy.cmoneytest.databinding.FragmentFirstBinding
import com.cindy.cmoneytest.viewmodel.FirstViewModel
import com.cindy.cmoneytest.viewmodel.ViewModelFactory

class FirstFragment : Fragment() {

    companion object {
        fun newInstance() = FirstFragment()
    }

    private lateinit var mViewModel: FirstViewModel
    private lateinit var mFirstFragmentBinding: FragmentFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFirstFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container, false)

        processViewDataBinding()

        return mFirstFragmentBinding.root
    }

    fun processViewDataBinding(){
        mViewModel = ViewModelProvider(this, ViewModelFactory())[FirstViewModel::class.java]
        mViewModel.run {
            navigateToListFragment.observe(viewLifecycleOwner, Observer {
                it.getContentIfNotHandled()?.let { gotoNextPage ->
                    if(gotoNextPage){
                        val navController: NavController = findNavController()
                        navController.navigate(R.id.list_fragment)
                    }
                }
            })
        }
        mFirstFragmentBinding.run {
            viewModel = mViewModel
            lifecycleOwner = this@FirstFragment
        }
    }

}