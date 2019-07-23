package com.asapp.asMessagin.challenge.injection

import com.asapp.asMessagin.challenge.persistence.Persistence
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
/**
 * Spark class used for injection of dependencies
 */
class PersistenceModule : AbstractModule() {
    override fun configure() {}

    @Provides
    @Singleton
    fun persistenceEngine(
        mapper: KotlinModule
    ): Persistence = Persistence(
        ObjectMapper().registerModule(mapper)
    )
}