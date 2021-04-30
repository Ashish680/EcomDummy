package com.ashish.e_comdummy.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ashish.e_comdummy.Common.ACTION
import com.ashish.e_comdummy.Common.CheckInternet
import com.ashish.e_comdummy.Common.Constant.PRODUCT
import com.ashish.e_comdummy.R
import com.ashish.e_comdummy.databinding.AddProductLayoutBinding
import com.ashish.e_comdummy.model.ProductItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


/**
 * Created by Ashish Tiwari on 30,April,2021
 */
class AddUpdateProductFragment(
    private val action: ACTION,
    private val position: Int,
    private val product: ProductItem?
) :
    Fragment(),
    View.OnClickListener {
    private lateinit var binding: AddProductLayoutBinding

    // request code
    private val PICK_IMAGE_REQUEST = 22

    // instance for firebase storage and StorageReference
    private var storage: FirebaseStorage? = null

    //our database reference object
    private var databaseArtists: DatabaseReference? = null

    // Uri indicates, where the image will be picked from
    private var filePath: Uri? = null
    private var imageUrl: String = ""

    private lateinit var back: InterUpdate

    interface InterUpdate {
        fun onBackFromUpdate(position: Int, product: ProductItem)
    }

    fun setInterface(i: InterUpdate) {
        this.back = i
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance()
        //getting the reference of product node
        databaseArtists = FirebaseDatabase.getInstance().getReference(PRODUCT)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_product_layout, container, false)
        initializeView()
        return binding.root
    }

    private fun initializeView() {
        binding.btnSelectImage.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.topBar.imgBack.setOnClickListener(this)
        if (action == ACTION.ADD)
            binding.topBar.txtTitle.text = getString(R.string.add_product)
        else {
            binding.topBar.txtTitle.text = getString(R.string.edit_product)
            binding.item = product
            imageUrl = product?.image.toString()
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnSelectImage -> selectImage()
            R.id.btnSubmit -> {
                if (CheckInternet.isNetworkAvailable(activity)) {

                    val name = binding.etName.text.toString()
                    val detail = binding.etDesc.text.toString()
                    val price = binding.etPrice.text.toString()
                    if (validation(name, price, detail)) {
                        binding.loadingPB.visibility = View.VISIBLE
                        binding.btnSubmit.visibility = View.GONE
                        if (action == ACTION.ADD)
                            filePath?.let { uploadImageToFirebase(it, name, price, detail) }
                        else if (action == ACTION.UPDATE) {
                            if (filePath == null)
                                submitData(name, price, detail, imageUrl)
                            else
                                filePath?.let { uploadImageToFirebase(it, name, price, detail) }
                        }
                    }
                } else
                    Toast.makeText(activity, getString(R.string.connection_msg), Toast.LENGTH_LONG)
                        .show()
            }
            R.id.imgBack -> activity?.onBackPressed()
        }
    }

    private fun validation(name: String, price: String, detail: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                Toast.makeText(activity, getString(R.string.enter_name), Toast.LENGTH_LONG).show()
                false
            }
            TextUtils.isEmpty(price) -> {
                Toast.makeText(activity, getString(R.string.enter_price), Toast.LENGTH_LONG).show()
                false
            }
            TextUtils.isEmpty(detail) -> {
                Toast.makeText(activity, getString(R.string.enter_detail), Toast.LENGTH_LONG).show()
                false
            }
            filePath == null && action == ACTION.ADD -> {
                Toast.makeText(activity, getString(R.string.select_image), Toast.LENGTH_LONG).show()
                false
            }
            else -> true
        }
    }

    private fun submitData(name: String, price: String, detail: String, imageUrl: String) {
        if (action == ACTION.ADD) {
            val id: String? = databaseArtists?.push()?.key
            val product = id?.let { ProductItem(it, name, price, detail, imageUrl) }

            if (id != null) {
                databaseArtists?.child(id)?.setValue(product)
            }
        } else {
            val dR =
                FirebaseDatabase.getInstance().getReference(PRODUCT).child(product?.id.toString())
            val product = ProductItem(product?.id.toString(), name, price, detail, imageUrl)
            dR.setValue(product)
            back.onBackFromUpdate(position, product)
        }
        activity?.onBackPressed()
    }

    private fun uploadImageToFirebase(fileUri: Uri, name: String, price: String, detail: String) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refStorage = storage?.reference?.child("images/$fileName")

        refStorage?.putFile(fileUri)?.addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                imageUrl = it.toString()
                submitData(name, price, detail, imageUrl)
            }
        }?.addOnFailureListener { e ->
            print(e.message)
        }
    }

    private fun selectImage() {
        // Defining Implicit Intent to mobile gallery
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Get the Uri of data
            filePath = data?.data
            binding.imgProduct.setImageURI(filePath)
        }
    }
}