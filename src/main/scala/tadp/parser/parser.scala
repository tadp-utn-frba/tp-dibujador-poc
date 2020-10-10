package tadp

import tadp.ast._
import tadp.combinators._

object parser {
  def imagenParser : Parser[Imagen] = imagenVaciaParser <|> dibujableParser.map(ImagenCon)

  def imagenVaciaParser : Parser[ImagenVacia.type] = emptyParser.map(_ => ImagenVacia)

  def dibujableParser : Parser[Dibujable] = figuraParser <|> grupoParser <|> transformacionParser

  def transformacionParser : Parser[Dibujable] = colorParser <|> escalaParser <|> rotacionParser

  def figuraParser : Parser[Dibujable] = trianguloParser <|> rectanguloParser <|> circuloParser

  def trianguloParser : Parser[Triangulo] = for {
    _ <- string("triangulo")
    _ <- char('[') <~ whitespace
    p1 <- puntoParser <~ whitespace
    _ <- char(',') <~ whitespace
    p2 <- puntoParser <~ whitespace
    _ <- char(',') <~ whitespace
    p3 <- puntoParser <~ whitespace
    _ <- char(']')
    } yield Triangulo(p1 = p1, p2 = p2, p3 = p3)

  def rectanguloParser : Parser[Rectangulo] = for {
    _ <- string("rectangulo")
    _ <- char('[') <~ whitespace
    arribaIzquierda <- puntoParser <~ whitespace
    _ <- char(',') <~ whitespace
    abajoDerecha <- puntoParser <~ whitespace
    _ <- char(']')
  } yield Rectangulo(arribaIzquierda = arribaIzquierda, abajoDerecha = abajoDerecha)

  def circuloParser : Parser[Circulo] = for {
    _ <- string("circulo")
    _ <- char('[') <~ whitespace
    centro <- puntoParser <~ whitespace
    _ <- char(',') <~ whitespace
    radio <- float <~ whitespace
    _ <- char(']')
  } yield Circulo(centro = centro, radio = radio)

  def puntoParser : Parser[Punto2D] =
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

  def colorParser : Parser[Color] = for {
    _ <- string("color[")
    colorRGB <- colorRGBParser
    _ <- char(']')
    _ <- char('(')
    dibujable <- whitespace ~> dibujableParser <~ whitespace
    _ <- char(')')
  } yield Color(colorRGB, dibujable)

  def escalaParser : Parser[Escala] = for {
    _ <- string("escala[")
    escaladoEnX <- whitespace ~> float <~ whitespace
    _ <- char(',')
    escaladoEnY <- whitespace ~> float
    _ <- char(']')
    _ <- char('(')
    dibujable <- whitespace ~> dibujableParser <~ whitespace
    _ <- char(')')
  } yield Escala((escaladoEnX, escaladoEnY), dibujable)

  def colorRGBParser : Parser[ColorRGB] = for {
    r <- whitespace ~> integer <~ whitespace
    _ <- char(',')
    g <- whitespace ~> integer <~ whitespace
    _ <- char(',')
    b <- whitespace ~> integer <~ whitespace
  } yield (r, g, b)

  def rotacionParser : Parser[Rotacion] = for {
    _ <- string("rotacion[")
    angulo <- whitespace ~> float <~ whitespace
    _ <- char(']')
    _ <- char('(')
    dibujable <- whitespace ~> dibujableParser <~ whitespace
    _ <- char(')')
  } yield Rotacion(angulo, dibujable)
}
