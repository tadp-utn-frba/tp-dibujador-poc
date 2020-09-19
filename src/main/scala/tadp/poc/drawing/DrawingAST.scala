package tadp.poc.drawing

import scalafx.scene.paint.Color

object DrawingAST {
  case class Vector2D(x: Double, y: Double) {
    def tuple: (Double, Double) = (x, y)
  }

  trait Drawable

  case class Group(children: Seq[Drawable]) extends Drawable

  case class Triangulo(p1: Vector2D,p2: Vector2D,p3: Vector2D, color: Color)
  case class Cuadrado(upperLeft: Vector2D, lowerRight: Vector2D, color: Color)
  case class Circulo(center: Vector2D, radius: Double, color: Color)
}
