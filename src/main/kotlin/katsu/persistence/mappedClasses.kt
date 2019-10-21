@file:SuppressWarnings("MatchingDeclarationName")

package katsu.persistence

import katsu.model.ClientDbo
import katsu.model.TreatmentDbo

val katsuManagedClasses = listOf(
    ClientDbo::class,
    TreatmentDbo::class
)
