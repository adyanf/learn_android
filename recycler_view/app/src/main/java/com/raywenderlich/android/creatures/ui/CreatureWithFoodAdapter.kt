package com.raywenderlich.android.creatures.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.Creature
import com.raywenderlich.android.creatures.model.CreatureStore
import kotlinx.android.synthetic.main.list_item_creature_with_food.view.*

class CreatureWithFoodAdapter(
    private var creatures: MutableList<Creature>
) : RecyclerView.Adapter<CreatureWithFoodAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(parent.inflate(R.layout.list_item_creature_with_food))
        holder.itemView.rvFoods.setRecycledViewPool(viewPool)
        LinearSnapHelper().attachToRecyclerView(holder.itemView.rvFoods)
        return holder
    }

    override fun getItemCount(): Int {
        return creatures.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(creatures[position])
    }

    fun updateCreatures(creatures: List<Creature>) {
        this.creatures.clear()
        this.creatures.addAll(creatures)
        notifyDataSetChanged()
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private val adapter = FoodAdapter(mutableListOf())
        private lateinit var creature: Creature

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(creature: Creature) {
            this.creature = creature
            val context = itemView.context
            val imageResource = context.resources.getIdentifier(creature.uri, null, context.packageName)
            itemView.creatureImage.setImageResource(imageResource)
            setupFoods()
        }

        override fun onClick(v: View?) {
            val intent = CreatureActivity.newIntent(itemView.context, creature.id)
            itemView.context.startActivity(intent)
        }

        private fun setupFoods() {
            itemView.rvFoods.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            itemView.rvFoods.adapter = adapter

            adapter.updateFoods(CreatureStore.getCreatureFoods(creature))
        }
    }
}