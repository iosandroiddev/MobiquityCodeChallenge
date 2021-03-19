package com.manohar.mobiquitycodechallenge.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.TooltipCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.manohar.mobiquitycodechallenge.R
import com.manohar.mobiquitycodechallenge.model.BookmarkLocationModel
import com.manohar.mobiquitycodechallenge.ui.adapter.BookMarksAdapter
import com.manohar.mobiquitycodechallenge.utils.ConstantKeys
import com.manohar.mobiquitycodechallenge.utils.ConstantKeys.pinnedLocations
import com.manohar.mobiquitycodechallenge.utils.hasLocationPermission
import com.manohar.mobiquitycodechallenge.utils.hide
import com.manohar.mobiquitycodechallenge.utils.show
import getCustomArrayListPreferenceAsynchronous
import kotlinx.android.synthetic.main.app_bar_home.*
import setCustomArrayListPreferenceAsynchronous

class MainActivity : AppCompatActivity(), BookMarksAdapter.IBookMarkClicked {

    private var _actionMode: ActionMode? = null

    private var _arrayOfPinnedLocations = ArrayList<BookmarkLocationModel>()
    private lateinit var _bookMarksAdapter: BookMarksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)
        setUpRecyclerView()
        bindClicks()
        addToolTips()
    }

    private fun addToolTips() {
        TooltipCompat.setTooltipText(fabAddBookMark, "Tap here to add a Bookmark")
    }

    override fun onStart() {
        super.onStart()
        bindData()
    }

    private fun bindClicks() {
        fabAddBookMark.setOnClickListener {
            if (hasLocationPermission()) {
                startActivity(Intent(this, MapsActivity::class.java))
            } else {
                requestLocationPermission()
            }
        }
    }

    private fun setUpRecyclerView() {
        val mLayoutManager = LinearLayoutManager(this)
        rvBookMarks.layoutManager = mLayoutManager
        rvBookMarks.itemAnimator = DefaultItemAnimator()
        _bookMarksAdapter =
            BookMarksAdapter(this, this)
        rvBookMarks.adapter = _bookMarksAdapter
    }

    private fun bindData() {
        getCustomArrayListPreferenceAsynchronous(pinnedLocations)?.let {
            _arrayOfPinnedLocations = ArrayList(it)
        }
        setAdapter()
    }

    private fun setAdapter() {
        if (_arrayOfPinnedLocations.size > 0) {
            tvNoBookMarkedLocations.hide()
            _bookMarksAdapter.addItems(_arrayOfPinnedLocations)
        } else {
            tvNoBookMarkedLocations.show()
        }
    }

    override fun onBookMarkClicked(pos: Int, bookMarkLocationModel: BookmarkLocationModel) {
        if (_bookMarksAdapter.selectedItemCount() > 0) {
            toggleActionBar(pos)
        } else {
            val bundle = Bundle()
            bundle.putSerializable("locationData", bookMarkLocationModel)
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
        _actionMode?.invalidate()
    }

    override fun onBookMarkLongLicked(pos: Int, bookMarkLocationModel: BookmarkLocationModel) {
        toggleActionBar(pos)
        _actionMode?.invalidate()
    }

    private fun requestLocationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), ConstantKeys.locationPermissionCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            when (requestCode) {
                ConstantKeys.locationPermissionCode -> {
                    fabAddBookMark.performClick()
                }
            }
        }
    }


    private fun toggleActionBar(position: Int) {
        if (_actionMode == null) {
            _actionMode = startSupportActionMode(_actionModeCallback)
        }
        toggleSelection(position)
    }

    private fun toggleSelection(position: Int) {
        _bookMarksAdapter.toggleSelection(position)
        val count: Int = _bookMarksAdapter.selectedItemCount()
        if (count == 0) {
            _actionMode?.finish()
        } else {
            _actionMode?.title = count.toString()
            _actionMode?.invalidate()
        }
    }

    private var _actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.menu_delete, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            item?.let {
                when (it.itemId) {
                    R.id.action_delete -> {
                        val arraySelectedItems = getBookMarks()
                        _actionMode?.finish()
                        _bookMarksAdapter.removeAllSelections()
                        removeAllItems(arraySelectedItems)
                    }
                    else -> {

                    }
                }
            }
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            _bookMarksAdapter.clearSelection()
            _actionMode = null
        }
    }

    private fun removeAllItems(arraySelectedItems: java.util.ArrayList<String>) {
        val uniqueArrayItems = ArrayList<BookmarkLocationModel>()
        _arrayOfPinnedLocations.forEach { bookMarkModel ->
            var modelFound = false
            arraySelectedItems.forEach original@{
                if (bookMarkModel.locationLatitude == it) {
                    modelFound = true
                    return@forEach
                }
            }
            if (!modelFound) {
                uniqueArrayItems.add(bookMarkModel)
            }
        }
        _arrayOfPinnedLocations.clear()
        _arrayOfPinnedLocations = uniqueArrayItems
        setCustomArrayListPreferenceAsynchronous(pinnedLocations, uniqueArrayItems)
        setAdapter()
    }

    private fun getBookMarks(): ArrayList<String> {
        val arrayCategoryIds = ArrayList<String>()
        _bookMarksAdapter.getSelectedItems().forEach {
            arrayCategoryIds.add(_bookMarksAdapter.getItem(it).locationLatitude)
        }
        return arrayCategoryIds
    }

}