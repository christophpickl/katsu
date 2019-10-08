@file:SuppressWarnings("MatchingDeclarationName")

package katsu.model

import katsu.persistence.NO_ID

fun Client.Companion.testInstance() = Client(
    id = NO_ID,
    firstName = "testFirstName",
    notes = "some test notes"
)

fun ClientDbo.Companion.testInstance() = ClientDbo(
    id = NO_ID,
    firstName = "testFirstName",
    notes = "some test notes"
)
