package tadp

import tadp.ast._
import tadp.combinators._

object parser {
  def imagenParser : Parser[Imagen] = imagenVaciaParser <|> dibujableParser.map(ImagenCon)

  def imagenVaciaParser : Parser[ImagenVacia.type] = emptyParser.map(_ => ImagenVacia)

  def dibujableParser : Parser[Dibujable] = trianguloParser <|> rectanguloParser <|> grupoParser

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

  def rectanguloParser : Parser[Rectangulo] = for {
    _ <- string("rectangulo")
    _ <- char('[') <~ whitespace
    arribaIzquierda <- verticeParser <~ whitespace
    _ <- char(',') <~ whitespace
    abajoDerecha <- verticeParser <~ whitespace
    _ <- char(']')
  } yield Rectangulo(arribaIzquierda = arribaIzquierda, abajoDerecha = abajoDerecha)

  def verticeParser : Parser[Punto2D] =
    for {
      x <- float
      _ <- whitespace ~> char('@') <~ whitespace
      y <- float
    } yield (x, y)

  def grupoParser : Parser[Grupo] = for {
    _ <- string("grupo(")
    dibujables <-  (whitespace ~> dibujableParser).optSepBy(char(','))
    _ <- whitespace ~> char(')')
  } yield Grupo(dibujables)
}
