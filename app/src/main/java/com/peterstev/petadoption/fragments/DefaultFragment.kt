package com.peterstev.petadoption.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import com.peterstev.petadoption.R
import com.peterstev.petadoption.contracts.FragmentContracts
import com.peterstev.petadoption.models.adoption.Page
import com.peterstev.petadoption.utils.PAGE_KEY
import com.peterstev.petadoption.view_models.MainViewModel

class DefaultFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var parentLayout: LinearLayoutCompat
    private lateinit var viewModel: MainViewModel
    private lateinit var contract: FragmentContracts

    companion object {
        fun newInstance(pageItems: Page): DefaultFragment {
            val bundle = Bundle()
            bundle.putSerializable(PAGE_KEY, pageItems)
            val fragment = DefaultFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            contract = context as FragmentContracts
        } catch (e: ClassCastException) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show();
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mContext = context!!
        return inflater.inflate(R.layout.activity_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentLayout = view.findViewById(R.id.fragment_parent)
        viewModel = contract.getMainViewModel()

        val page = arguments!!.getSerializable(PAGE_KEY) as Page
        val formViews = viewModel.getPetAdoptionFormPage(page, mContext)
        formViews.forEach { views ->
            parentLayout.addView(views.value)
        }
    }
}