package com.peterstev.petadoption.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.peterstev.petadoption.R
import com.peterstev.petadoption.adapters.FragmentAdapter
import com.peterstev.petadoption.contracts.FragmentContracts
import com.peterstev.petadoption.databinding.ActivityMainBinding
import com.peterstev.petadoption.forms.PetFormOptions
import com.peterstev.petadoption.models.AdoptedPet
import com.peterstev.petadoption.view_models.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity(), View.OnClickListener, ViewPager.OnPageChangeListener,
    FragmentContracts {

    private lateinit var pager: ViewPager
    private lateinit var nextFab: FloatingActionButton
    private lateinit var prevFab: FloatingActionButton
    private lateinit var submitFab: ExtendedFloatingActionButton
    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var viewModel: MainViewModel
    private lateinit var subcription: Disposable
    private lateinit var adapter: FragmentAdapter

    private var pethistory: MutableList<AdoptedPet> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as PetApplication).appComponent.injectActivity(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupViews()

        adapter = FragmentAdapter(supportFragmentManager, viewModel.getPetAdoptionScope().pages)
        pager.adapter = adapter
        toggleVisibility(0)

        subcription = viewModel.fetchData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                pethistory = it
            }
    }

    private fun setupViews() {

        pager = binding.viewPager
        nextFab = binding.mainFabNext
        prevFab = binding.mainFabBack
        submitFab = binding.mainFabDone

        nextFab.setOnClickListener(this)
        prevFab.setOnClickListener(this)
        submitFab.setOnClickListener(this)
        pager.addOnPageChangeListener(this)
        val limit = viewModel.getPetAdoptionScope().pages.size
        pager.offscreenPageLimit = if (limit > 1) limit else 1
    }

    private fun toggleVisibility(position: Int) {
        if (adapter.count == 1) {
            nextFab.visibility = View.GONE
            prevFab.visibility = View.GONE
            submitFab.visibility = View.VISIBLE
        } else if (adapter.count > 1) {
            if (position == 0) {
                nextFab.visibility = View.VISIBLE
                prevFab.visibility = View.GONE
                submitFab.visibility = View.GONE
            } else if (position > 0 && position < pager.adapter!!.count.minus(1)) {
                nextFab.visibility = View.VISIBLE
                prevFab.visibility = View.VISIBLE
                submitFab.visibility = View.GONE
            } else if (position == pager.adapter!!.count.minus(1)) {
                nextFab.visibility = View.GONE
                prevFab.visibility = View.VISIBLE
                submitFab.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_history) {
            val history = viewModel.getPetHistoryHeaders()
            showHistory(history)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        subcription.dispose()
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when (v) {
            nextFab -> {
                nextPage()
            }
            prevFab -> {
                prevPage()
            }
            submitFab -> {
                //completed
                val error = viewModel.validateForm()
                //use this to display a bottom sheet that tells the user
                //The error fields if the list is greater than 0
                if (error.size > 0) {
                    val stringBuilder = StringBuilder()
                    error.forEach {
                        stringBuilder.append(
                            "\n\n ${it.label.toString()}   on page ${it.page!!.plus(1)}"
                        )
                    }
                    showPrompt(getString(R.string.error_fields), stringBuilder.toString())
                } else if (error.size == 0) {
                    //All Clear, Good to go
                    val viewList = viewModel.getValidViews()
                    val adoptedPet = AdoptedPet()
                    val stringBuilder = StringBuilder()
                    viewList.forEach { data ->
                        if (data.label.contains(PetFormOptions.TextInputType.EMAIL.name, true)) {
                            adoptedPet.label = data.value
                        }
                        stringBuilder.append("${data.label}: ${data.value} \n\n")
                    }
                    adoptedPet.value = stringBuilder.toString()
                    //show user the entered value and insert into db
                    showPrompt(getString(R.string.save_to_db), adoptedPet.value, adoptedPet)
                }
            }
        }
    }

    private fun showHistory(list: MutableList<String>) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.dialog_show_title))
            .setCancelable(true)
            .setAdapter(
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
            ) { dialog, which ->
                showPrompt(pethistory.get(which).label, pethistory.get(which).value)
            }
            .setPositiveButton(getString(R.string.close)) { dialog, _ ->

                dialog.dismiss()
            }.show()
    }

    private fun showPrompt(title: String, message: String, adoptedPet: AdoptedPet? = null) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(true)
            .setPositiveButton(getString(R.string.okay)) { dialog, _ ->
                if (adoptedPet != null) {
                    //this is a call to save
                    viewModel.insertData(adoptedPet)
                    Toast.makeText(this, getString(R.string.saved_data), Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun nextPage() {
        pager.setCurrentItem(pager.currentItem + 1, true)
    }

    private fun prevPage() {
        pager.setCurrentItem(pager.currentItem - 1, true)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        toggleVisibility(position)
    }

    override fun getMainViewModel(): MainViewModel {
        return viewModel
    }

}