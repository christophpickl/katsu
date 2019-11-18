package katsu.ui.view

import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.web.HTMLEditor
import katsu.datePatternParse
import katsu.formatKatsuDate
import katsu.ui.TreatmentUi
import tornadofx.View
import tornadofx.hgrow
import tornadofx.htmleditor
import tornadofx.label
import tornadofx.singleAssign
import tornadofx.textfield
import tornadofx.vbox
import tornadofx.vgrow

class TreatmentView : View() {

    private var treatmentNotesField: HTMLEditor by singleAssign()
    private var dateField: TextField by singleAssign()

    val notes get() = treatmentNotesField.htmlText
    val date get() = datePatternParse(dateField.text)

    override val root = vbox {
        label("TREATMENT ===")
        vbox {
            hgrow = Priority.ALWAYS
            label("Date")
            dateField = textfield().apply {
                hgrow = Priority.ALWAYS
            }
        }
        treatmentNotesField = htmleditor().apply {
            hgrow = Priority.ALWAYS
            vgrow = Priority.ALWAYS
        }
    }

    fun updateTreatmentFields(treatment: TreatmentUi?) {
        dateField.text = treatment?.date?.formatKatsuDate() ?: ""
        treatmentNotesField.htmlText = treatment?.notes ?: ""
    }
}
