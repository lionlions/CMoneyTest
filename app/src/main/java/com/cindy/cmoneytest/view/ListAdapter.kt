package com.cindy.cmoneytest.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cindy.cmoneytest.databinding.WidgetListItemBinding
import com.cindy.cmoneytest.model.DataModel
import com.cindy.cmoneytest.viewmodel.ListViewModel

class ListAdapter(private val mViewModel: ListViewModel) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private val TAG: String = javaClass.simpleName
    var isScrollIdol: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "=== onBindViewHolder ===")
        if (!mViewModel.mDataModelListLiveData.value.isNullOrEmpty()) {
            val dataModel: DataModel = mViewModel.mDataModelListLiveData.value!![position]
            holder.bind(position, mViewModel, dataModel)
        }
    }

    override fun getItemCount(): Int {
        return mViewModel.mDataModelListLiveData.value?.size ?: 0
    }

    class ViewHolder private constructor(private val mBinding: WidgetListItemBinding) :
        RecyclerView.ViewHolder(mBinding.root) {

        private val TAG: String = javaClass.simpleName

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
                val binding = WidgetListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(position: Int, viewModel: ListViewModel, dataModel: DataModel) {
            Log.v(TAG, "=== bind ===")
            Log.v(TAG, "position $position")

            mBinding.viewModel = viewModel
            mBinding.dataModel = dataModel
            mBinding.executePendingBindings()

            mBinding.backgroundImage.setImageBitmap(null)

            if(viewModel.isScrollIdol){
                if (viewModel.mBitmapList.isNotEmpty()
                    && viewModel.mBitmapList[position] != null) {
                    mBinding.backgroundImage.setImageBitmap(viewModel.mBitmapList[position])
                }else{
                    val imageUrl: String? = dataModel.url
                    if (!imageUrl.isNullOrEmpty()) {
                        viewModel.getImageFromInternet(position, imageUrl)
                    }
                }
            }

        }
    }

    fun notifyItem(index: Int) {
        Log.v(TAG, "=== notifyItem === index: $index")
        notifyItemChanged(index)
    }

}