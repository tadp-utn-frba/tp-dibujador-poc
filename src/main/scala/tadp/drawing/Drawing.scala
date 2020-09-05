package tadp.drawing

import scalafx.scene.paint.Paint

trait Drawable

case class Drawing(root: Drawable)

case class Vector2D(x: Double, y: Double)

case class Group(position: Vector2D, children: Seq[Drawable]) extends Drawable

case class Stroke(width: Double, paint: Paint)
case class ShapeProperties(fill: Option[Paint], stroke: Option[Stroke])

case class Rectangle(position: Vector2D, width: Double, height: Double, properties: ShapeProperties) extends Drawable
case class Circle(center: Vector2D, radius: Double, properties: ShapeProperties) extends Drawable



