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

package com.raywenderlich.android.w00tze.ui.profile

import androidx.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.raywenderlich.android.w00tze.R
import com.raywenderlich.android.w00tze.model.ApiError
import com.raywenderlich.android.w00tze.model.Either
import com.raywenderlich.android.w00tze.model.Status
import com.raywenderlich.android.w00tze.model.User
import com.raywenderlich.android.w00tze.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

  private lateinit var profileViewModel: ProfileViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.fragment_profile, container, false)

    profileViewModel = defaultViewModelProviderFactory.create(ProfileViewModel::class.java)

    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    profileViewModel.getUser().observe(viewLifecycleOwner, userObserver)

    val onClickListener = View.OnClickListener {
      showCompanyDialog(company.text.toString()) { newCompany ->
        profileViewModel.updateUser(newCompany).observe(viewLifecycleOwner, userObserver)
      }
    }

    login.setOnClickListener(onClickListener)
    company.setOnClickListener(onClickListener)
  }

  private val userObserver = Observer<Either<User>> { either ->
    if (either?.status == Status.SUCCESS && either.data != null) {
      val user = either.data
      login.text = String.format(getString(R.string.screen_name_format), user.login)
      repoName.text = user.name
      company.text = user.company
      Picasso.get().load(user.avatarUrl).into(avatar)
    } else if (either?.status == Status.ERROR && either.error == ApiError.USER) {
      Snackbar.make(view!!, getString(R.string.error_retrieving_user), Snackbar.LENGTH_SHORT).show()
    }
  }
}