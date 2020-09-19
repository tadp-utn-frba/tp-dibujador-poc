package tadp.poc.drawing

import scalafx.scene.paint.Paint

trait Drawable

case class Drawing(root: Drawable)

case class Vector2D(x: Double, y: Double) {
  def tuple: (Double, Double) = (x, y)
}

case class Group(position: Vector2D, children: Seq[Drawable]) extends Drawable

case class Stroke(width: Double, paint: Paint)

case class ShapeProperties(fill: Option[Paint], stroke: Option[Stroke])

case class Rectangle(position: Vector2D, width: Double, height: Double, properties: ShapeProperties) extends Drawable

case class Circle(center: Vector2D, radius: Double, properties: ShapeProperties) extends Drawable

case class Triangle(p1: Vector2D, p2: Vector2D, p3: Vector2D, properties: ShapeProperties) extends Drawable



