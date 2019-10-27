package katsu.ui

import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.c
import tornadofx.mixin

class Styles : Stylesheet() {

    companion object {
        val white = c("#FFFFFF")
        val black = c("#000000")
        val greyVeryBright = c("#D9D9D9")
        val greyBright = c("#E8E8E8")
        val highlight = c("#5C8BD9")
        val backgroundInactive = c("#6AA1FD")
    }

    init {
        val backgroundHoverColors = mixin {
            textFill = black

            and(even) {
                backgroundColor += greyBright
            }
            and(odd) {
                backgroundColor += greyVeryBright
            }
            and(empty) {
                backgroundColor += backgroundInactive
            }
            and(hover) {
                backgroundColor += white
            }
            and(selected) {
                textFill = highlight
                backgroundColor += white
                fontWeight = FontWeight.BOLD
            }
        }

        Stylesheet.listCell {
            +backgroundHoverColors
        }
    }
}
