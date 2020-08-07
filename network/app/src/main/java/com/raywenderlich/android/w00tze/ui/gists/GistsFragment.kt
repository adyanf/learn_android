/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package com.raywenderlich.android.w00tze.ui.gists

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.raywenderlich.android.w00tze.R
import com.raywenderlich.android.w00tze.model.*
import com.raywenderlich.android.w00tze.viewmodel.GistsViewModel
import kotlinx.android.synthetic.main.fragment_gists.*


class GistsFragment : Fragment(), GistAdapter.GistAdapterListener {

  private lateinit var gistsViewModel: GistsViewModel

  private val adapter = GistAdapter(mutableListOf(), this)

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_gists, container, false)

    gistsViewModel = defaultViewModelProviderFactory.create(GistsViewModel::class.java)

    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    gistsRecyclerView.layoutManager = LinearLayoutManager(context)
    gistsRecyclerView.adapter = adapter

    val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
    itemTouchHelper.attachToRecyclerView(gistsRecyclerView)

    gistsViewModel.getGists().observe(viewLifecycleOwner, Observer<Either<List<Gist>>?> { either ->
      if (either?.status == Status.SUCCESS && either.data != null) {
        val gists = either.data
        adapter.updateGists(gists)
      } else if (either?.status == Status.ERROR && either.error == ApiError.GISTS) {
        Snackbar.make(view, getString(R.string.error_retrieving_gists), Snackbar.LENGTH_SHORT).show()
      }
    })

    fab.setOnClickListener {
      showGistDialog()
    }
  }

  override fun deleteGist(gist: Gist) {
    gistsViewModel.deleteGist(gist).observe(viewLifecycleOwner, Observer<Either<EmptyResponse>?> { either ->
      if (either?.status == Status.SUCCESS && either.data != null) {
        adapter.deleteGist(gist)
      } else if (either?.status == Status.ERROR && either.error == ApiError.DELETE_GIST) {
        Snackbar.make(view!!, getString(R.string.error_deleting_gist), Snackbar.LENGTH_SHORT).show()
      }
    })
  }

  internal fun sendGist(description: String, filename: String, content: String) {
    gistsViewModel.sendGist(description, filename, content).observe(viewLifecycleOwner, Observer<Either<Gist>?> { either ->
      if (either?.status == Status.SUCCESS && either.data != null) {
        val gist = either.data
        adapter.addGist(gist)
      } else if (either?.status == Status.ERROR && either.error == ApiError.POST_GIST) {
        Snackbar.make(view!!, getString(R.string.error_posting_gist), Snackbar.LENGTH_SHORT).show()
      }
    })
  }
}