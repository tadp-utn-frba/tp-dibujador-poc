package tadp.drawing

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Button, Label, TextArea}
import scalafx.scene.input.{KeyCode, KeyCodeCombination, KeyCombination}
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.{Group, Scene}
import tadp.combinators.{Error, Success}
import tadp.parser

object TADPDrawingApp extends JFXApp {
  val appWidth = 1200
  val appHeight = 800
  val paneSeparation = 20
  val contentWidth = appWidth - paneSeparation * 2
  val contentHeight = appHeight - paneSeparation * 2

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

  val canvas = new Canvas(contentWidth / 2, contentHeight - 60)

  val canvasPane = new HBox()
  canvasPane.children = canvas
  canvasPane.margin = Insets(0, 0, 0, paneSeparation)
  canvasPane.maxHeight = canvas.height.value
  canvasPane.border = new Border(new BorderStroke(Color.Gray,
    BorderStrokeStyle.Solid,
    new CornerRadii(0),
    new BorderWidths(1)))


  val adapter = TADPDrawingAdapter(canvas)
  val textField = new TextArea()
  textField.prefWidth = contentWidth / 2 - paneSeparation
  textField.maxHeight = canvas.height.value

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
    scene = new Scene(appWidth, appHeight) {

      val border = new BorderPane
      border.padding = Insets(paneSeparation, paneSeparation, paneSeparation, paneSeparation)
      border.left = textField
      border.right = canvasPane
      border.bottom = new Group {
        children = Seq(drawButton, parseResultLabel)
        margin = Insets(paneSeparation, 0, 0, 0)
      }

      onKeyPressed = { keyEvent => if (ctrlEnter.`match`(keyEvent)) dibujar }

      root = border
    }
  }

  Dibujador.reset(adapter)
}
