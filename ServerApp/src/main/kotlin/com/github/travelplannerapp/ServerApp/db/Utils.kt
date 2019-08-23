package com.github.travelplannerapp.ServerApp.db

import kotlin.reflect.full.declaredMemberProperties

inline infix fun <reified T : Any> T.merge(other: T): T {
    val nameToProperty = T::class.declaredMemberProperties.associateBy { it.name }
    val secondaryConstructor = T::class.constructors.elementAt(0)
    val args = secondaryConstructor.parameters.associate { parameter ->
        val property = nameToProperty[parameter.name]!!
        parameter to (property.get(this) ?: property.get(other))
    }
    return secondaryConstructor.callBy(args)
}