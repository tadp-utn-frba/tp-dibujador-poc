package tadp

object combinators {
  trait ParseResult[+Value]
  case class Success[Value](value: Value, leftToParse: String) extends ParseResult[Value]
  case class Error[Value](leftToParse: String) extends ParseResult[Value]

  abstract class Parser[+T] extends (String => ParseResult[T]) {
    def apply(text: String): ParseResult[T]

    def <|>[U >: T, V >: U](otroParser: Parser[U]): Parser[V] = (text) =>
      this(text) match {
        case success@Success(_, _) => success
        case Error(_) => otroParser(text)
      }

    def opt: Parser[Option[T]] = this.map(Some(_): Option[T]) <|> noop.map(_ => None)

    def <>[U](otroParser: Parser[U]): Parser[(T, U)] = for { parsedValue <- this
                                                             anotherParsedValue <- otroParser }
      yield (parsedValue, anotherParsedValue)

    def * : Parser[List[T]] = this.+ <|> noop.map(_ => Nil)

    def + : Parser[List[T]] = for { parsedValue <- this
                                    parsedValues <- this.* }
      yield parsedValue :: parsedValues

    def sepBy[U](separatorParser: Parser[U]) =
      (for { firstValue <- this
             values <- (separatorParser ~> this ).* } yield firstValue :: values)

    def satisfies(predicate: T => Boolean): Parser[T] = (text) =>
      this(text) match {
        case success@Success(value, _) if predicate(value) => success
        case _ => Error(text)
      }

    def withFilter: (T => Boolean) => Parser[T] = satisfies

    def map[U](f: T => U): Parser[U] = flatMap(value => Success(f(value), _))

    def flatMap[U](f: T => Parser[U]): Parser[U] = text =>
      this(text) match {
        case Success(value, leftToParse) => f(value)(leftToParse) match {
          case success@Success(_, _) => success
          case Error(_) => Error(text)
        }
        case Error(_) => Error(text)
      }

    def ~>[U](otherParser: Parser[U]): Parser[U] = for {
      _ <- this
      value <- otherParser
    } yield value

    def <~[U](otherParser: Parser[U]): Parser[T] = for {
      value <- this
      _ <- otherParser
    } yield value
  }

  case object AnyChar extends Parser[Char] {
    def apply(text: String) = {
      text.headOption match {
        case Some(firstCharacter) => Success(value = firstCharacter, leftToParse = text.drop(1))
        case None => Error(leftToParse = text)
      }
    }
  }

  case object NoopParser extends Parser[Unit] {
    def apply(text: String) = Success(value = (), leftToParse = text)
  }

  case object EmptyParser extends Parser[Unit] {
    def apply(text: String) =
      if(text.isEmpty) Success(value = (), leftToParse = text) else Error(leftToParse = text)
  }

  def emptyParser: Parser[Unit] = EmptyParser
  def anyChar: Parser[Char] = AnyChar
  def char: Char => Parser[Char] = aChar => anyChar satisfies (_ == aChar)
  def whitespace: Parser[Unit] = (char(' ').*).map(_ => ())
  def letter: Parser[Char] = anyChar satisfies (_.isLetter)
  def digit: Parser[Char] = anyChar satisfies (_.isDigit)
  def alphaNum: Parser[Char] = letter <|> digit
  def string: String => Parser[String] = expectedString => expectedString.map(char).foldLeft(noop.map(_ => "")) {
    case (acumParser, charParser) => (acumParser <> charParser) map { case (acumResult, aChar) => acumResult :+ aChar }
  }
  def void: Parser[Unit] = anyChar map { _ => () }
  def integer: Parser[Int] = (digit.+) map (_.mkString.toInt)
  def float: Parser[Double] = {
    def toDecimalPart : Double => Double = number => if (number < 1) number else toDecimalPart(number / 10)

    val floatingPoint: Parser[Char] = char('.')

    val decimals: Parser[Double] = for {
      _ <- floatingPoint
      decimalPart <- integer
    } yield toDecimalPart(decimalPart)

    val floatWithIntegerPart: Parser[Double] = for {
      integerPart <- integer
      decimalPart <- (decimals <|> floatingPoint.map(_ => 0: Double)).opt
    } yield integerPart + decimalPart.getOrElse(0: Double)

    floatWithIntegerPart <|> decimals
  }
  def noop: Parser[Unit] = NoopParser
}
