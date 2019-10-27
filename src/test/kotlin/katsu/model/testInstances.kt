@file:SuppressWarnings("MatchingDeclarationName")

package katsu.model

import katsu.persistence.NO_ID
import java.time.LocalDateTime

fun Client.Companion.testInstance() = Client(
    id = NO_ID,
    firstName = "testFirstName",
    notes = "some test notes"
)

fun ClientDbo.Companion.testInstance() = ClientDbo(
    id = NO_ID,
    firstName = "testFirstName",
    notes = "some test notes",
    treatments = mutableListOf()
)

fun TreatmentDbo.Companion.testInstance() = TreatmentDbo(
    id = NO_ID,
    date = LocalDateTime.now(),
    notes = "some test notes"
)
