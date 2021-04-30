package com.ashish.e_comdummy.adopter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ashish.e_comdummy.Common.Constant
import com.ashish.e_comdummy.R
import com.ashish.e_comdummy.databinding.FooterSmallLayoutBinding
import com.ashish.e_comdummy.databinding.ProductItemLayoutBinding
import com.ashish.e_comdummy.model.ProductItem
import com.google.android.gms.common.util.CollectionUtils

/**
 * Created by Ashish Tiwari on 30,April,2021
 */
class ProductAdopter(listener: ProductClickListener) :
    RecyclerView.Adapter<ProductAdopter.Holder>() {
    private lateinit var context: Context
    private var listner: ProductClickListener = listener
    private var productList: MutableList<ProductItem> = mutableListOf()

    interface ProductClickListener {
        fun onProductClick(int: Int, item: ProductItem)
    }

    fun setData(list: MutableList<ProductItem>) {
        this.productList = list
        notifyDataSetChanged()
    }

    fun getData(): MutableList<ProductItem> {
        return productList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        this.context = parent.context
        return if (viewType == Constant.TYPE_ITEM) {
            val navigationView: View =
                LayoutInflater.from(context).inflate(R.layout.product_item_layout, parent, false)
            Holder(navigationView, viewType)
        } else {
            val navHeaderView: View =
                LayoutInflater.from(context).inflate(R.layout.footer_small_layout, parent, false)
            Holder(navHeaderView, viewType)
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (holder.viewTypeId == Constant.TYPE_ITEM)
            if (!CollectionUtils.isEmpty(productList)) {
                val itemModel: ProductItem = productList[position]
                holder.binding?.item = itemModel
                holder.binding?.llProduct?.setOnClickListener {
                    listner.onProductClick(position, itemModel)
                }
            }
    }

    override fun getItemCount(): Int {
        return productList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            Constant.TYPE_FOOTER
        } else Constant.TYPE_ITEM
    }

    class Holder(itemLayoutView: View?, ViewType: Int) : RecyclerView.ViewHolder(itemLayoutView!!) {
        var viewTypeId = 0
        var binding: ProductItemLayoutBinding? = null
        private var footerLayoutBinding: FooterSmallLayoutBinding? = null

        init {
            if (ViewType == Constant.TYPE_ITEM) {
                binding = DataBindingUtil.bind(itemView)
                viewTypeId = Constant.TYPE_ITEM
            } else if (ViewType == Constant.TYPE_FOOTER) {
                footerLayoutBinding = DataBindingUtil.bind(itemView)
                viewTypeId = Constant.TYPE_FOOTER
            }
        }
    }

    fun removeItem(position: Int) {
        productList.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(item: ProductItem) {
        productList.add(0, item)
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, item: ProductItem) {
        productList.set(position, item)
        notifyItemChanged(position)
    }
}