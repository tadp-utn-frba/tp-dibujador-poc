package tadp.drawing

import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.embed.swing.SwingFXUtils
import scalafx.geometry.Insets
import scalafx.scene.{Group, Scene}
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.{Button, Label, TextArea}
import scalafx.scene.image.WritableImage
import scalafx.scene.input.{KeyCode, KeyCodeCombination, KeyCombination}
import scalafx.scene.layout.{Border, BorderPane, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, HBox}
import scalafx.scene.paint.Color

import java.io.FileOutputStream
import java.time.LocalDateTime
import javax.imageio.ImageIO

class TADPInteractiveDrawingScreen(dibujador: (String, TADPDrawingAdapter) => Any) extends JFXApp3 {
  val appWidth = 1200
  val appHeight = 800
  val paneSeparation = 20
  val contentWidth = appWidth -  paneSeparation * 2
  val contentHeight = appHeight - paneSeparation * 2
  var canvas: Canvas = null
  var adapter: TADPDrawingAdapter = null
  var canvasPane: HBox = null
  var textField: TextArea = null
  var drawButton: Button = null
  var saveButton: Button = null
  var ctrlEnter: KeyCodeCombination = null
  var parseResultLabel: Label = null

  def start () = {
    canvas = new Canvas(contentWidth / 2, contentHeight - 60)
    adapter = new TADPDrawingAdapter(canvas)
    canvasPane = new HBox()
    textField = new TextArea() {
      prefWidth = contentWidth / 2 - paneSeparation
      maxHeight = canvas.height.value
    }
    drawButton = new Button("Dibujar") {
      layoutX = 0
      onAction = { _ => dibujar() }
    }
    saveButton = new Button("Guardar") {
      layoutX = 70
      onAction = { _ => guardar() }
    }
    ctrlEnter = new KeyCodeCombination(KeyCode.Enter, KeyCombination.ControlDown)
    parseResultLabel = new Label("") {
      layoutX = 150
      layoutY = 5
    }
    canvasPane.children = canvas
    canvasPane.margin = Insets(0, 0, 0, paneSeparation)
    canvasPane.maxHeight = canvas.height.value
    canvasPane.border = new Border(new BorderStroke(Color.Gray,
      BorderStrokeStyle.Solid,
      new CornerRadii(0),
      new BorderWidths(1))
    )

    stage = new PrimaryStage {
      title = "TADP Draw"
      scene = new Scene(appWidth, appHeight) {

        val border = new BorderPane
        border.padding = Insets(paneSeparation, paneSeparation, paneSeparation, paneSeparation)
        border.left = textField
        border.right = canvasPane
        border.bottom = new Group {
          children = Seq(drawButton, saveButton, parseResultLabel)
          margin = Insets(paneSeparation, 0, 0, 0)
        }

        onKeyPressed = { keyEvent => if (ctrlEnter.`match`(keyEvent)) dibujar() }

        root = border
      }
    }
  }

  def info(mensaje: String): Unit = {
    parseResultLabel.text = mensaje
    parseResultLabel.textFill = Color.Black
  }

  def error(mensaje: String): Unit = {
    parseResultLabel.text = mensaje
    parseResultLabel.textFill = Color.Red
  }

  def limpiarMensaje(): Unit = {
    parseResultLabel.text = ""
  }

  def dibujar(callback: () => Unit = () => ()): Unit = {
    setupCanvas()
    limpiarMensaje()
    val descripcionDeImagen = textField.text.getValue

    try {
      dibujador(descripcionDeImagen, adapter)
      callback()
    } catch {
      case e => error(e.getMessage)
    }
  }

  private def setupCanvas(): Unit = {
    canvas = new Canvas(contentWidth /
      2, contentHeight -
      60)
    adapter = new TADPDrawingAdapter(canvas)
    canvasPane.children = canvas
    canvasPane.margin = Insets(0, 0, 0, paneSeparation)
    canvasPane.maxHeight = canvas.height.value
    canvasPane.border = new Border(new BorderStroke(Color.Gray,
      BorderStrokeStyle.Solid,
      new CornerRadii(0),
      new BorderWidths(1)))
  }

  def guardar(): Unit = {
    dibujar { () =>
      val image = new WritableImage(canvas.width.intValue(), canvas.height.intValue())
      canvas.snapshot(null, image)

      val nombreDeImagen = s"dibujo-${LocalDateTime.now()}.png"
      val nombreDeCarpeta = "out/"

      ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new FileOutputStream(nombreDeCarpeta + nombreDeImagen))
      info(s"Imagen ${nombreDeImagen} guardada en ${nombreDeCarpeta}")
    }
  }
}
