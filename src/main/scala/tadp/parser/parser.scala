package tadp

import tadp.ast._
import tadp.combinators.{char, _}

object parser {
  def imagenParser : Parser[Imagen] = imagenVaciaParser <|> dibujableParser.map(ImagenCon)

  def imagenVaciaParser : Parser[ImagenVacia.type] = emptyParser.map(_ => ImagenVacia)

  def dibujableParser : Parser[Dibujable] = figuraParser <|> grupoParser <|> transformacionParser

  def transformacionParser : Parser[Dibujable] = colorParser <|> escalaParser <|> rotacionParser <|> traslacionParser

  def figuraParser : Parser[Dibujable] = trianguloParser <|> rectanguloParser <|> circuloParser

  def transformacion[A, B <: Transformacion](nombre : String,
                                             parametrosParser : Parser[A],
                                             crearTransformacion : (A, Dibujable) => B) = for {
    _ <- string(nombre)
    parametros <- entre("[", "]")(parametrosParser)
    dibujable <- entre("(", ")")(dibujableParser)
  } yield crearTransformacion(parametros, dibujable)

  def entre[A](inicio: String, fin: String)(parser: Parser[A]) = for {
    _ <- string(inicio)
    resultado <- whitespace ~> parser <~ whitespace
    _ <- string(fin)
  } yield resultado

  def separador(sep: String) = whitespace ~> string(sep) <~ whitespace

  def trianguloParser : Parser[Triangulo] =
    string("triangulo") ~> entre("[", "]")(for {
      p1 <- puntoParser
      _ <- separador(",")
      p2 <- puntoParser
      _ <- separador(",")
      p3 <- puntoParser
    } yield Triangulo(p1, p2, p3))

  def rectanguloParser : Parser[Rectangulo] =
    string("rectangulo") ~> entre("[", "]")(for {
      arribaIzquierda <- puntoParser
      _ <- separador(",")
      abajoDerecha <- puntoParser
    } yield Rectangulo(arribaIzquierda, abajoDerecha))

  def circuloParser : Parser[Circulo] =
    string("circulo") ~> entre("[", "]")(for {
      centro <- puntoParser
      _ <- separador(",")
      radio <- float
    } yield Circulo(centro, radio))

  def puntoParser : Parser[Punto2D] =
    for {
      x <- float
      _ <- separador("@")
      y <- float
    } yield (x, y)

  def grupoParser : Parser[Grupo] = for {
    _ <- string("grupo")
    dibujables <- entre("(", ")")(dibujableParser.optSepBy(separador(",")))
  } yield Grupo(dibujables)

  def colorParser : Parser[Transformacion] = {
    val colorRGBParser = for {
      r <- integer
      _ <- separador(",")
      g <- integer
      _ <- separador(",")
      b <- integer
    } yield (r, g, b)

    transformacion("color", colorRGBParser, Color(_, _))
  }

  def escalaParser : Parser[Transformacion] = {
    val escaladoParser = for {
      x <- float
      _ <- separador(",")
      y <- float
    } yield (x, y)

    transformacion("escala", escaladoParser, Escala(_, _))
  }

  def rotacionParser : Parser[Transformacion] = {
    val anguloParser = float

    transformacion("rotacion", anguloParser, Rotacion(_, _))
  }

  def traslacionParser : Parser[Transformacion] = {
    val trasladoParser = for {
      x <- float
      _ <- separador(",")
      y <- float
    } yield (x, y)

    transformacion("traslacion", trasladoParser, Traslacion(_, _))
  }
}
