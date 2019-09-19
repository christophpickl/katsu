@file:SuppressWarnings("MatchingDeclarationName")

package katsu.persistence

import javax.persistence.EntityManager

const val NO_ID = 0L

fun EntityManager.transactional(function: EntityManager.() -> Unit) {
    transaction.begin()
    try {
        function(this)
        transaction.commit()
    } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
        transaction.rollback()
        throw e
    }
}
