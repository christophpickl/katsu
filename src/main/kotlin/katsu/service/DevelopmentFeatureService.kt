package katsu.service

import katsu.model.Client
import katsu.model.ClientDbo
import katsu.model.Treatment
import katsu.model.TreatmentDbo
import katsu.persistence.ClientRepository
import katsu.persistence.NO_ID
import katsu.persistence.transactional
import java.time.LocalDateTime
import javax.persistence.EntityManager

class DevelopmentFeatureService(
    private val em: EntityManager,
    private val repo: ClientRepository
) {
    fun resetDummyData() {
        em.transactional {
            createQuery("DELETE FROM ${TreatmentDbo.ENTITY_NAME}").executeUpdate()
            createQuery("DELETE FROM ${ClientDbo.ENTITY_NAME}").executeUpdate()

            val clients = listOf(
                client("Anny Nym",
                    notes = "<b>just some short</b> notes for anna.",
                    treatments = listOf(
                        treatment(1, "die letzt kuerzlichste"),
                        treatment(2, "die 2te"),
                        treatment(3, "die erste, <b>lange</b> ists her")
                    )
                ),
                client("Max Muster")
            )
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
        date = LocalDateTime.now().minusDays(minusDays.toLong()),
        notes = notes
    )
}
