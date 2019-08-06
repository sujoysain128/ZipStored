package com.zipstored.com

import androidx.fragment.app.Fragment
import com.zipstored.com.fragment.HomeFragment
import com.zipstored.com.fragment.ProviderListing
import com.zipstored.com.fragment.SignUpOneFragment


class mFragmentManager {

    var arrStack: MutableList<Int> = ArrayList()

    companion object {
        val SIGN_UP_ONE_FRAGMENT = 1
        val HOME_FRAGMENT = 2
        val PROVIDER_LISTING_FRAGMENT = 3



    }

    var signUpOneFragment: SignUpOneFragment? = null
    var homeFragment : HomeFragment? = null
    var providerListing : ProviderListing? = null




    fun clearFragmentStack() {
        arrStack.clear()
        signUpOneFragment = null
        homeFragment = null
        providerListing = null

    }

    fun clearThisFragment(tag: Int) {
        when (tag) {
            SIGN_UP_ONE_FRAGMENT -> signUpOneFragment = null
            HOME_FRAGMENT -> homeFragment = null
            PROVIDER_LISTING_FRAGMENT -> providerListing = null

        }
    }

    fun removeThisFragment(tag: Int) {
        when (tag) {
            SIGN_UP_ONE_FRAGMENT -> arrStack.remove(SIGN_UP_ONE_FRAGMENT)
            HOME_FRAGMENT -> arrStack.remove(HOME_FRAGMENT)
            PROVIDER_LISTING_FRAGMENT -> arrStack.remove(PROVIDER_LISTING_FRAGMENT)

        }
    }

    private fun generateFragment(tag: Int, fragment: androidx.fragment.app.Fragment?, isNew: Boolean): androidx.fragment.app.Fragment {

        if (fragment == null || isNew) {
            println("-------Generating New Fragment Instance------$isNew")
            when (tag) {
                SIGN_UP_ONE_FRAGMENT -> return SignUpOneFragment().also { signUpOneFragment = it }
                HOME_FRAGMENT -> return HomeFragment().also { homeFragment = it }
                PROVIDER_LISTING_FRAGMENT -> return ProviderListing().also { providerListing = it }

            }
        }
        println("-------Return Old Fragment Instance------")
        return fragment
    }

    fun openFragment(tag: Int, isNew: Boolean = false): androidx.fragment.app.Fragment {
        when (tag) {
            SIGN_UP_ONE_FRAGMENT -> return generateFragment(tag, signUpOneFragment, isNew)
            HOME_FRAGMENT -> return generateFragment(tag, homeFragment, isNew)
            PROVIDER_LISTING_FRAGMENT -> return generateFragment(tag, providerListing, isNew)

        }
        return androidx.fragment.app.Fragment()
    }


    fun addStackFragment(tag: Int) {
        if (arrStack.size > 0) {
            if (arrStack[arrStack.size - 1] != tag) {
                arrStack.add(tag)
            }
        } else {
            arrStack.add(tag)
        }

    }

    fun hasFragmentInStack(): Boolean {
        return arrStack.size > 1
    }


    fun openPreviousFragment(isNew: Boolean = false): androidx.fragment.app.Fragment {
        arrStack.removeAt(arrStack.size - 1)
        return openFragment(arrStack[arrStack.size - 1], isNew)

    }

}
