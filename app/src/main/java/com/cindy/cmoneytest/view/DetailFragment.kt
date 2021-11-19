package com.cindy.cmoneytest.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cindy.cmoneytest.R
import com.cindy.cmoneytest.databinding.FragmentDetailBinding
import com.cindy.cmoneytest.model.DataModel
import com.cindy.cmoneytest.viewmodel.DetailViewModel
import com.cindy.cmoneytest.viewmodel.ViewModelFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {

    private val TAG: String = javaClass.simpleName

    companion object {
        fun newInstance() = DetailFragment()
    }

    private lateinit var mViewModel: DetailViewModel
    private lateinit var mDetailFragmentBinding: FragmentDetailBinding
    private var mDataModel: DataModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDetailFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        processArguments()
        processViewDataBinding()
        processContent()

        return mDetailFragmentBinding.root
    }

    private fun processArguments(){
        mDataModel = requireArguments().getParcelable("detail")
    }

    private fun processViewDataBinding(){
        mViewModel = ViewModelProvider(this, ViewModelFactory())[DetailViewModel::class.java]

        mViewModel.run {
            mBitmapLiveData.observe(viewLifecycleOwner, Observer {
                if(it!=null){
                    mDetailFragmentBinding.image.setImageBitmap(it)
                }
            })
        }

        mDetailFragmentBinding.run {
            dataModel = mDataModel
            lifecycleOwner = this@DetailFragment
        }
    }

    private fun processContent(){
        mDataModel?.run{
            if(!hdurl.isNullOrEmpty()){
                mViewModel.getImageFromInternet(hdurl!!)
            }
            if(!copyright.isNullOrEmpty()){
                mDetailFragmentBinding.copyright.text = String.format(getString(R.string.detail_page_copyright), copyright)
            }
            if(!date.isNullOrEmpty()){
                val inputDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputDate = SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault())
                var newDate: Date? = null
                try{
                    newDate = inputDate.parse(date)
                }catch(e: ParseException){
                    e.printStackTrace()
                }

                mDetailFragmentBinding.dateTime.text = if(newDate==null){
                    date
                }else{
                    outputDate.format(newDate)
                }

            }
        }
    }


}