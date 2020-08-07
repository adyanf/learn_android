package com.raywenderlich.android.foodmart.ui.categories

import androidx.viewpager.widget.ViewPager
import android.view.View
import kotlin.math.abs

class DepthPageTransformer : androidx.viewpager.widget.ViewPager.PageTransformer {

    companion object {
        private const val MIN_SCALE = 0.75f
    }

    override fun transformPage(view: View, position: Float) {
        val pageWidth: Int = view.width

        when {
            position < -1 -> { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.alpha = 0f
            }
            position <= 0 -> { // [-1,0]
                // Fade the page out.
                view.alpha = 1f + position
                // Counteract the default slide transition
                view.translationX = pageWidth * -position
                // Scale the page down (between MIN_SCALE and 1)
                val scaleFactor = (MIN_SCALE + (1 - MIN_SCALE) * (1 - abs(position)))
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
            }
            position <= 1 -> { // (0,1]
                // Use the default slide transition when moving to the left page
                view.alpha = 1f
                view.translationX = 0f
                view.scaleX = 1f
                view.scaleY = 1f
            }
            else -> { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.alpha = 0f
            }
        }
    }
}