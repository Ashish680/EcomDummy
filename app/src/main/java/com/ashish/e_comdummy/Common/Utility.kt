package com.ashish.e_comdummy.Common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Created by Ashish Tiwari on 30,April,2021
 */
class Utility {
    companion object {
        fun callFragmentMethod(
            activity: FragmentActivity?,
            BaseFragment: Fragment?,
            TAG: String?,
            container: Int
        ) {
            val fm = activity!!.supportFragmentManager
            val fragmentTransaction = fm.beginTransaction()
            fragmentTransaction.setCustomAnimations(0, 0, 0, 0)
            fragmentTransaction.add(container, BaseFragment!!)
            fragmentTransaction.addToBackStack(TAG)
            fragmentTransaction.commit()
        }
    }
}