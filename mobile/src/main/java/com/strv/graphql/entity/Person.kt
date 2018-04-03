package com.strv.graphql.entity

/**
 * Author:          Martin Rončka <mroncka@gmail.com>
 * Date:            12/13/17
 * Description:     Person data entity
 */
data class Person(
        val id: Long,
        val name: String,
        val birthDate: String,
        val imageUrl: String) {

    override fun equals(other: Any?): Boolean = when (other) {
        is Person -> other.id == this.id
        else -> super.equals(other)
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    fun hasSameContentAs(newItem: Person): Boolean {
        return id == newItem.id
    }
}
