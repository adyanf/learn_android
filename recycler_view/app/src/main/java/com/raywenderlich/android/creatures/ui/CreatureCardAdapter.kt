package com.raywenderlich.android.creatures.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.Constants
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.Creature
import kotlinx.android.synthetic.main.list_item_creature_card.view.creatureCard
import kotlinx.android.synthetic.main.list_item_creature_card.view.creatureCardImage
import kotlinx.android.synthetic.main.list_item_creature_card.view.fullName
import kotlinx.android.synthetic.main.list_item_creature_card.view.nameHolder
import kotlinx.android.synthetic.main.list_item_creature_card_jupiter.view.*
import java.lang.IllegalStateException
import java.util.*

class CreatureCardAdapter(
    private val creatures: MutableList<Creature>
) : RecyclerView.Adapter<CreatureCardAdapter.ViewHolder>(), ItemTouchHelperListener {

    var scrollDirection = ScrollDirection.DOWN
    var jupiterSpanSize = 2

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private lateinit var creature: Creature

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(creature: Creature) {
            this.creature = creature
            val context = itemView.context
            val imageResource = context.resources.getIdentifier(creature.thumbnail, null, context.packageName)
            itemView.creatureCardImage.setImageResource(imageResource)
            itemView.fullName.text = creature.fullName
            setBackgroundColor(context, imageResource)
            animateView(itemView)
        }

        override fun onClick(view: View) {
            val context = view.context
            val intent = CreatureActivity.newIntent(context, creature.id)
            context.startActivity(intent)
        }

        private fun setBackgroundColor(context: Context, imageResource: Int) {
            val image = BitmapFactory.decodeResource(context.resources, imageResource)
            Palette.from(image).generate { palette ->
                val defaultBackgroundColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
                val backgroundColor = palette?.getDominantColor(defaultBackgroundColor) ?: defaultBackgroundColor
                itemView.creatureCard.setBackgroundColor(backgroundColor)
                itemView.nameHolder.setBackgroundColor(backgroundColor)
                val textColor = if (isColorDark(backgroundColor)) Color.WHITE else Color.BLACK
                itemView.fullName.setTextColor(textColor)
                if (itemView.slogan != null) {
                    itemView.slogan.setTextColor(textColor)
                }
            }
        }

        private fun isColorDark(color: Int): Boolean {
            val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
            return darkness >= 0.5
        }

        private fun animateView(viewToAnimate: View) {
            val animId = if (scrollDirection == ScrollDirection.DOWN) {
                R.anim.slide_from_bottom
            } else R.anim.slide_from_top
            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, animId)
            viewToAnimate.animation = animation
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ViewType.JUPITER.ordinal -> ViewHolder(parent.inflate(R.layout.list_item_creature_card_jupiter))
            ViewType.MARS.ordinal -> ViewHolder(parent.inflate(R.layout.list_item_creature_card_mars))
            ViewType.OTHER.ordinal -> ViewHolder(parent.inflate(R.layout.list_item_creature_card))
            else -> throw IllegalStateException()
        }
    }

    override fun getItemCount(): Int {
        return creatures.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(creature = creatures[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (creatures[position].planet) {
            Constants.JUPITER -> ViewType.JUPITER.ordinal
            Constants.MARS -> ViewType.MARS.ordinal
            else -> ViewType.OTHER.ordinal
        }
    }

    override fun onItemMove(
        recyclerView: RecyclerView,
        fromPosition: Int,
        toPosition: Int
    ): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(creatures, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(creatures, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(viewHolder: RecyclerView.ViewHolder, position: Int) {
        // do nothing
    }

    fun spanSizeAtPosition(position: Int): Int {
        return if (creatures[position].planet == Constants.JUPITER) {
            jupiterSpanSize
        } else 1
    }

    enum class ScrollDirection {
        UP, DOWN
    }

    enum class ViewType {
        JUPITER, MARS, OTHER
    }
}