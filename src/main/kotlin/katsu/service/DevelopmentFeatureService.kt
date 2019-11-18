package katsu.service

import katsu.model.Client
import katsu.model.ClientDbo
import katsu.model.Treatment
import katsu.model.TreatmentDbo
import katsu.persistence.NO_ID
import katsu.persistence.transactional
import katsu.withZeroTime
import java.time.LocalDateTime
import javax.persistence.EntityManager

class DevelopmentFeatureService(
    private val em: EntityManager
) {
    @Suppress("MagicNumber")
    fun resetDummyData() {
        em.transactional {
            createQuery("DELETE FROM ${TreatmentDbo.ENTITY_NAME}").executeUpdate()
            createQuery("DELETE FROM ${ClientDbo.ENTITY_NAME}").executeUpdate()

            val clients = mutableListOf(
                client("Anny Nym",
                    notes = "<b>just some short</b> notes for anna.",
                    treatments = listOf(
                        treatment(1, "die letzt kuerzlichste"),
                        treatment(30, "die 2te"),
                        treatment(420, "die erste, <b>lange</b> ists her")
                    )
                )
            )
            clients.addAll(1.rangeTo(10).map {
                client("Max Muster$it")
            })
            clients.forEach { em.persist(it.toClientDbo()) }
        }
    }

    fun client(
        firstName: String,
        notes: String = Client.PROTOTYPE.notes,
        treatments: List<Treatment> = emptyList()
    ) = Client.PROTOTYPE.copy(
        firstName = firstName,
        notes = notes,
        treatments = treatments
    )

    private fun treatment(
        minusDays: Int = 0,
        notes: String = "das hat ihr gut getan"
    ) = Treatment(
        id = NO_ID,
        date = LocalDateTime.now().minusDays(minusDays.toLong()).withZeroTime(),
        notes = notes
    )
}


