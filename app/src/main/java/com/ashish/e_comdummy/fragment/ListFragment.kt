package com.ashish.e_comdummy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ashish.e_comdummy.Common.ACTION
import com.ashish.e_comdummy.Common.CheckInternet
import com.ashish.e_comdummy.Common.Constant
import com.ashish.e_comdummy.Common.Utility
import com.ashish.e_comdummy.R
import com.ashish.e_comdummy.adopter.ProductAdopter
import com.ashish.e_comdummy.databinding.ProductListLayoutBinding
import com.ashish.e_comdummy.model.ProductItem
import com.ashish.e_comdummy.roomDB.AppDatabase
import com.google.android.gms.common.util.CollectionUtils
import com.google.firebase.database.*


/**
 * Created by Ashish Tiwari on 30,April,2021
 */
class ListFragment : Fragment(), AddUpdateProductFragment.InterUpdate {
    private lateinit var binding: ProductListLayoutBinding

    //our database reference object
    private var databaseArtists: DatabaseReference? = null
    private var mMessageListener: ChildEventListener? = null
    private var productAdapter: ProductAdopter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //getting the reference of product node
        databaseArtists = FirebaseDatabase.getInstance().getReference(Constant.PRODUCT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.product_list_layout, container, false)
        initializeView()
        listData()

        //---------Check Internet connection-----------//
        if (!CheckInternet.isNetworkAvailable(activity))
            fromDB()

        return binding.root
    }

    private fun initializeView() {
        binding.topBar.imgBack.visibility = View.GONE
        binding.floating.setOnClickListener {
            if (CheckInternet.isNetworkAvailable(activity)) {
                val addFrg = AddUpdateProductFragment(ACTION.ADD, -1, null)
                addFrg.setInterface(this)
                Utility.callFragmentMethod(
                    activity,
                    addFrg,
                    Constant.FRG_ADD,
                    R.id.container
                )
            } else
                Toast.makeText(activity, getString(R.string.connection_msg), Toast.LENGTH_LONG)
                    .show()
        }

        //creating adapter
        productAdapter = ProductAdopter(object : ProductAdopter.ProductClickListener {
            override fun onProductClick(int: Int, item: ProductItem) {
                val addFrg = AddUpdateProductFragment(ACTION.UPDATE, int, item)
                addFrg.setInterface(this@ListFragment)
                Utility.callFragmentMethod(
                    activity,
                    addFrg,
                    Constant.FRG_ADD,
                    R.id.container
                )
            }
        })
        binding.recyclerView.adapter = productAdapter
    }

    //-------------------- Get list of products from Firebase ---------------//
    private fun listData() {
        val childEventListener: ChildEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // A new product has been added
                // onChildAdded() will be called for each node at the first time
                binding.loadingPB.visibility = View.VISIBLE
                val product: ProductItem? = dataSnapshot.getValue(ProductItem::class.java)
                if (product != null) {
                    productAdapter?.addItem(product)
                    methodAddOrUpdateToBD(product)
                }
                binding.loadingPB.visibility = View.GONE

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val product: ProductItem? = dataSnapshot.getValue(ProductItem::class.java)
                if (product != null) {
                    methodAddOrUpdateToBD(product)
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(activity, "Failed to load Product.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        databaseArtists?.addChildEventListener(childEventListener)
        mMessageListener = childEventListener

    }

    //-------------------------Get List when Offline-----------------------//
    private fun fromDB() {
        val db = activity?.let { AppDatabase(it) }
        Thread {
            val list = db?.todoDao()?.getAll()
            if (!CollectionUtils.isEmpty(list)) {
                productAdapter?.setData(list?.reversed() as MutableList<ProductItem>)
            }
            binding.loadingPB.visibility = View.GONE
        }.start()
    }

    //-------------------------Add and Update the date in Database---------------//
    private fun methodAddOrUpdateToBD(product: ProductItem) {
        val db = activity?.let { AppDatabase(it) }
        Thread {
            if (CollectionUtils.isEmpty(db?.todoDao()?.findById(product.id))) {
                db?.todoDao()?.insert(product)
            } else
                db?.todoDao()?.updateTodo(product)

        }.start()
    }

    //-------------------------Callback from Update product--------------------//
    override fun onBackFromUpdate(position: Int, product: ProductItem) {
        productAdapter?.updateItem(position, product)
    }
}