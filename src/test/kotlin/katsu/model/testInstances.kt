@file:SuppressWarnings("MatchingDeclarationName")

package katsu.model

fun Client.Companion.testInstance() = Client(
    id = 0L,
    firstName = "testFirstName",
    note = "some test note"
)
