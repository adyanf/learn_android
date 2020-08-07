package com.raywenderlich.android.creatures.model

class CompositeItem {
    lateinit var creature: Creature
        private set
    lateinit var header: Header
        private set
    var isHeader = false
        private set

    companion object {
        fun withCreature(creature: Creature): CompositeItem {
            val compositeItem = CompositeItem()
            compositeItem.creature = creature
            return compositeItem
        }

        fun withHeader(header: Header): CompositeItem {
            val compositeItem = CompositeItem()
            compositeItem.header = header
            compositeItem.isHeader = true
            return compositeItem
        }
    }

    override fun toString(): String {
        return if (isHeader) header.name else creature.nickname
    }
}