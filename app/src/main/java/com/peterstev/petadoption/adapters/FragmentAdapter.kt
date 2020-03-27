package com.peterstev.petadoption.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.peterstev.petadoption.fragments.DefaultFragment
import com.peterstev.petadoption.models.adoption.Page

class FragmentAdapter(
    private val fragmentManager: FragmentManager,
    private val pageList: List<Page>
) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return DefaultFragment.newInstance(pageList[position])
    }

    override fun getCount(): Int {
        return pageList.size
    }
}