package tadp.drawing

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.event.ActionEvent
import scalafx.scene.{Group, Scene}
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Button, Label, ListView, TextArea, TextField}
import scalafx.scene.input.{KeyCode, KeyCodeCombination, KeyCombination}
import scalafx.scene.layout.BorderPane
import scalafx.scene.paint.Color
import tadp.combinators.{Error, Success}
import tadp.parser

object TADPDrawingApp extends JFXApp {
  def dibujar: Unit = {
    val programa = textField.text.getValue
    parser.imagenParser(programa) match {
      case Success(imagen, _) => {
        parseResultLabel.text = ""
        Dibujador.resetAndDraw(adapter, imagen)
      }
      case Error(_) => parseResultLabel.text = "Problema al parsear"
    }
  }

  val canvas = new Canvas(1000, 750)
  val adapter = TADPDrawingAdapter(canvas)
  val textField = new TextArea()
  val drawButton = new Button("Dibujar")
  val ctrlEnter = new KeyCodeCombination(KeyCode.Enter, KeyCombination.ControlDown)
  val parseResultLabel = new Label("") {
    layoutX = 70
    layoutY = 5
    textFill = Color.Red
  }

  drawButton.onAction = { event => dibujar }
  stage = new PrimaryStage {
    title = "TADP Draw"
    scene = new Scene(1200, 800) {
      val border = new BorderPane
      border.left = textField
      border.right = canvas
      border.bottom = new Group {
        children = Seq(drawButton, parseResultLabel)
      }

      onKeyPressed = { keyEvent => if (ctrlEnter.`match`(keyEvent)) dibujar }

      root = border
    }
  }

  Dibujador.reset(adapter)
}
