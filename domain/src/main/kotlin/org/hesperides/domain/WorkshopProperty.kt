package org.hesperides.domain

import org.axonframework.commandhandling.TargetAggregateIdentifier
import org.hesperides.domain.security.User
import org.hesperides.domain.security.UserEvent
import org.hesperides.domain.workshopproperties.entities.WorkshopProperty

// Command
data class CreateWorkshopPropertyCommand(val definition: WorkshopProperty, val user: User)
data class UpdateWorkshopPropertyCommand(@TargetAggregateIdentifier val key: String, val newDefinition: WorkshopProperty, val user: User)

// Event
data class WorkshopPropertyCreatedEvent(val definition: WorkshopProperty, override val user: User) : UserEvent(user)
data class WorkshopPropertyUpdatedEvent(val key: String, val newDefinition: WorkshopProperty, override val user: User) : UserEvent(user)

// Query
data class WorkshopPropertyByKeyQuery(val key: String)
