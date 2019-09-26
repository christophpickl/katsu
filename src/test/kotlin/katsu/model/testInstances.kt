@file:SuppressWarnings("MatchingDeclarationName")

package katsu.model

import katsu.persistence.NO_ID

fun ClientDbo.Companion.testInstance() = ClientDbo(
    id = NO_ID,
    firstName = "testFirstName",
    note = "some test note"
)
