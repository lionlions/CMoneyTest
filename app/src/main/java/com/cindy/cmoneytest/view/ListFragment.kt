package com.cindy.cmoneytest.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cindy.cmoneytest.R
import com.cindy.cmoneytest.api.ApiRepository
import com.cindy.cmoneytest.databinding.FragmentListBinding
import com.cindy.cmoneytest.viewmodel.ListViewModel
import com.cindy.cmoneytest.viewmodel.ViewModelFactory

class ListFragment : Fragment() {

    private val TAG: String = javaClass.simpleName

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var mViewModel: ListViewModel
    private lateinit var mListFragmentBinding: FragmentListBinding
    private var mListAdapter: ListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mListFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)

        processViewDataBinding()
        processRecyclerView()
        processViewModel()

        return mListFragmentBinding.root
    }

    private fun processViewDataBinding(){
        mViewModel = ViewModelProvider(this, ViewModelFactory(ApiRepository(context)))[ListViewModel::class.java]
        mListFragmentBinding.run {
            viewModel = mViewModel
            lifecycleOwner = this@ListFragment
        }
    }

    private fun processRecyclerView(){
        mListAdapter = ListAdapter(mViewModel)
        mListFragmentBinding.list.adapter = mListAdapter
        mListFragmentBinding.list.addOnScrollListener(mListScrollListener)
    }

    private val mListScrollListener: RecyclerView.OnScrollListener = object:
        RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when(newState){
                RecyclerView.SCROLL_STATE_IDLE -> {
                    Log.w(TAG, "SCROLL_STATE_IDLE")
                    mViewModel.isScrollIdol = true
                    if(mListAdapter!=null){
                        val layoutManager: GridLayoutManager = mListFragmentBinding.list.layoutManager as GridLayoutManager
                        mListAdapter!!.notifyDataSetChanged()
                    }
                }
                RecyclerView.SCROLL_STATE_DRAGGING -> {
                    Log.w(TAG, "SCROLL_STATE_DRAGGING")
                    mViewModel.isScrollIdol = false
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    private fun processViewModel(){
        mViewModel.run {
            mDataModelListLiveData.observe(viewLifecycleOwner, Observer {
                Log.v(TAG, "=== mDataModelListLiveData observe ===")
                if(mListAdapter!=null){
                    mListAdapter!!.notifyDataSetChanged()
                }
            })
            mBitmapLiveData.observe(viewLifecycleOwner, Observer { index ->
                if(mListAdapter!=null){
                    mListAdapter!!.notifyItem(index)
                }
            })
            isLoading.observe(viewLifecycleOwner, Observer { visible ->
                mListFragmentBinding.progressGroup.visibility = if(visible){
                    mListFragmentBinding.errorMessage.visibility = View.GONE
                    View.VISIBLE
                }else{
                    View.GONE
                }
            })
            mErrorMessage.observe(viewLifecycleOwner, Observer { stringResource ->
                mListFragmentBinding.errorMessage.visibility = View.VISIBLE
                mListFragmentBinding.errorMessage.text = getString(stringResource)
            })
            navigateToDetailFragment.observe(viewLifecycleOwner, Observer {
                it.getContentIfNotHandled()?.let {
                    val navController: NavController = findNavController()
                    val bundle: Bundle = bundleOf(
                        "title" to it.title,
                        "detail" to it
                    )
                    navController.navigate(R.id.detail_fragment, bundle)
                }
            })
        }
    }

}