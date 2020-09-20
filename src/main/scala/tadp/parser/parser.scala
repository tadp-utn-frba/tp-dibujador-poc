package tadp

import tadp.ast._
import tadp.combinators._

object parser {
  def imagenParser : Parser[Imagen] = imagenVaciaParser <|> trianguloParser.map(ImagenCon)

  def imagenVaciaParser : Parser[ImagenVacia.type] = emptyParser.map(_ => ImagenVacia)

  def trianguloParser : Parser[Triangulo] = for {
    _ <- string("triangulo")
    _ <- char('[') <~ whitespace
    p1 <- verticeParser <~ whitespace
    _ <- char(',') <~ whitespace
    p2 <- verticeParser <~ whitespace
    _ <- char(',') <~ whitespace
    p3 <- verticeParser <~ whitespace
    _ <- char(']')
    } yield Triangulo(p1 = p1, p2 = p2, p3 = p3)

  def verticeParser : Parser[Punto2D] =
    for {
      x <- float
      _ <- whitespace ~> char('@') <~ whitespace
      y <- float
    } yield (x, y)
}
