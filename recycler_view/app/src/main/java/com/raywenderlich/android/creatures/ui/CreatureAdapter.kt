package com.raywenderlich.android.creatures.ui

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.Creature
import com.raywenderlich.android.creatures.model.Favorites
import kotlinx.android.synthetic.main.list_item_creature.view.*
import java.util.*

class CreatureAdapter(
    private var creatures: MutableList<Creature>,
    private val itemDragListener: ItemDragListener
) : RecyclerView.Adapter<CreatureAdapter.ViewHolder>(), ItemTouchHelperListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_creature))
    }

    override fun getItemCount(): Int {
        return creatures.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(creatures[position])
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
        Favorites.saveFavorites(creatures.map { it.id }, recyclerView.context)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(viewHolder: RecyclerView.ViewHolder, position: Int) {
        Favorites.removeFavorite(creatures[position], viewHolder.itemView.context)
        creatures.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateCreatures(creatures: List<Creature>) {
        this.creatures.clear()
        this.creatures.addAll(creatures)
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener, ItemSelectedListener {

        private lateinit var creature: Creature

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(creature: Creature) {
            this.creature = creature
            val context = itemView.context
            val imageResource = context.resources.getIdentifier(creature.uri, null, context.packageName)
            itemView.creatureImage.setImageResource(imageResource)
            itemView.fullName.text = creature.fullName
            itemView.nickname.text = creature.nickname
            animateView(itemView)
            itemView.handle.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    itemDragListener.onItemDrag(this)
                }
                false
            }
        }

        override fun onClick(v: View?) {
            val intent = CreatureActivity.newIntent(itemView.context, creature.id)
            itemView.context.startActivity(intent)
        }

        private fun animateView(viewToAnimate: View) {
            if (viewToAnimate.animation == null) {
                val animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.scale)
                viewToAnimate.animation = animation
            }
        }

        override fun onItemSelected() {
            itemView.itemCreatureContainer.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.colorSelectedItem))
        }

        override fun onItemCleared() {
            itemView.itemCreatureContainer.setBackgroundColor(0)
        }
    }
}