package tadp

import org.scalatest.matchers.should.Matchers
import org.scalatest.freespec._
import tadp.combinators._


class ParsersTest extends AnyFreeSpec with Matchers {
  "char parser" - {
    val parserOfA = char('a')
    "when fed a text with the expected character, provides a match with that character and consumes the input" in {
      parserOfA("a") shouldBe(Success(value = 'a', leftToParse = ""))
    }

    "when fed a text beginning with the expected character, provides a match with that character and consumes the input" in {
      parserOfA("arbol") shouldBe(Success(value = 'a', leftToParse = "rbol"))
    }

    "when fed a text that does not begin with the char, fails and does not consume any input" in {
      parserOfA("b") shouldBe(Error(leftToParse = "b"))
    }
  }

  "letter parser" - {
    "when fed a text that begins with a letter, provides a match with it and consumes the input" in {
      letter("hola") shouldBe(Success(value = 'h', leftToParse = "ola"))
    }

    "when fed a text that does not begin with a letter, fails and does not consume any input" in {
      letter("123") shouldBe(Error(leftToParse = "123"))
    }
  }

  "digit parser" - {
    "when fed a text that begins with a digit, provides a match with it and consumes the input" in {
      digit("123") shouldBe(Success(value = '1', leftToParse = "23"))
    }

    "when fed a text that does not begin with a digit, fails and does not consume any input" in {
      digit("hola") shouldBe(Error(leftToParse = "hola"))
    }
  }

  "alphaNum parser" - {
    "when fed a text that begins with a digit, provides a match with it and consumes the input" in {
      alphaNum("123") shouldBe(Success(value = '1', leftToParse = "23"))
    }

    "when fed a text that begins with a letter, provides a match with it and consumes the input" in {
      alphaNum("hola") shouldBe(Success(value = 'h', leftToParse = "ola"))
    }

    "when fed any character that is not a digit or a letter, fails and does not consume any input" in {
      alphaNum("(*5)") shouldBe(Error(leftToParse = "(*5)"))
    }
  }

  "<|> combinator" - {
    "when passed 2 combinators" - {
      "if the first matches it works like that parser" in {
        (letter <|> digit)("a") shouldBe(letter("a"))
      }

      "if the first does not match and the second does, it works like the second parser" in {
        (digit <|> letter)("a") shouldBe(letter("a"))
      }

      "if none matches it fails and does not consume any input" in {
        (char('b') <|> digit)("a") shouldBe(Error(leftToParse = "a"))
      }
    }
  }

  "string parser" - {
    val parserOfHola = string("hola")

    "when fed a text with the expected string, provides a match with that string and consumes the input" in {
      parserOfHola("hola") shouldBe(Success(value = "hola", leftToParse = ""))
    }

    "when fed a text that starts with the expected string, provides a match with that string and consumes the input" in {
      parserOfHola("hola mundo") shouldBe(Success(value = "hola", leftToParse = " mundo"))
    }

    "when fed a text that does not start with the expected string, fails and does not consume any input" in {
      parserOfHola("chau") shouldBe(Error("chau"))
    }
  }

  "void parser" - {
    "when fed an empty test, it fails" in {
      void("") shouldBe(Error(leftToParse = ""))
    }

    "when fed any text, it returns Unit and consumes 1 character" in {
      void("hola") shouldBe(Success(value = (), leftToParse = "ola"))
    }
  }

  "opt combinator" - {
    "when passed a combinator" - {
      "if the combinator matches, it acts as that combinator returning the value as an option" in {
        (letter.opt)("hola") shouldBe(Success(value = Some('h'), leftToParse = "ola"))
      }

      "if the combinator does not match, it does not consume any input and returns none as the value" in {
        (digit.opt)("hola") shouldBe(Success(value = None, leftToParse = "hola"))
      }
    }
  }

  "<> combinator" - {
    "when passed 2 parsers" - {
      "if the first parser does not match, it fails without consuming any input" in {
        (letter <> digit)("11") shouldBe(Error(leftToParse = "11"))
      }

      "when the first parser matches it feeds the text left to parse to the second" - {
        "if the second parser does not match the text left to parse it fails without consuming any input" in {
          (letter <> digit)("aa") shouldBe(Error(leftToParse = "aa"))
        }

        "if the second parser matches the text left to parse," +
          "returns a tuple of the values matched consuming the input of both parsers" in {
          (letter <> digit)("a1") shouldBe(Success(value = ('a', '1'), leftToParse = ""))
        }
      }
    }
  }

  "* combinator" - {
    "when passed a parser" - {
      "if the parser does not match, it returns an empty list without consuming any input" in {
        (string("10").*)("hola") shouldBe(Success(value = Nil, leftToParse = "hola"))
      }

      "if the parser matches, it tries to match it as many times as it can, returning a list of the values that" +
        "where matched consecutively, consuming all the matched input" in {
        (string("10").*)("10101020") shouldBe(Success(value = List("10", "10", "10"), leftToParse = "20"))
      }
    }
  }

  "+ combinator" - {
    "when passed a parser" - {
      "if the parser does not match, it fails without consuming any input" in {
        (string("10").+)("hola") shouldBe(Error(leftToParse = "hola"))
      }

      "if the parser matches, it tries to match it as many times as it can, returning a list of the values that" +
        "where matched consecutively, consuming all the matched input" in {
        (string("10").+)("10101020") shouldBe(Success(value = List("10", "10", "10"), leftToParse = "20"))
      }
    }
  }

  "any char parser" - {
    "when fed an empty text, fails and does not consume any input" in {
      anyChar("") shouldBe(Error(leftToParse = ""))
    }

    "when fed any text with at least one character, it returns the first character consuming it" in {
      anyChar("hola") shouldBe(Success(value = 'h', leftToParse = "ola"))
    }
  }

  "int parser" - {
    "when fed a text that does not start with an int, fails and does not consume any input" in {
      integer("hola") shouldBe(Error(leftToParse = "hola"))
    }

    "when fed a text that starts with an int, returns the number (not a char or string) consuming the characters of the number" in {
      integer("1234hola") shouldBe(Success(value = 1234, leftToParse = "hola"))
    }
  }

  "double parser" - {
    "when fed a text that does not start with a numbers, fails and does not consume any input" in {
      float("hola") shouldBe(Error(leftToParse = "hola"))
    }

    "when fed a text that starts with integer numbers (without floating point), it returns the double consuming the characters of the number" in {
      float("1234hola") shouldBe(Success(value = 1234 : Double, leftToParse = "hola"))
    }

    "when fed a text that starts with integer numbers and followed by a floating point, it returns the double consuming the characters of the" +
      "number plus the point" in {
      float("1234.hola") shouldBe(Success(value = 1234 : Double, leftToParse = "hola"))
    }

    "when fed a text that starts with integer numbers, followed by a floating point, followed by more integer numbers," +
      "it returns the double consuming the characters of all those numbers and the point" in {
      float("1234.56hola") shouldBe(Success(value = 1234.56 : Double, leftToParse = "hola"))
    }

    "when fed a text that starts with a number that has 0 as the decimal part" +
      "it returns that number consuming all the characters of the number" in {
      float("1234.0hola") shouldBe(Success(value = 1234 : Double, leftToParse = "hola"))
    }

    "when fed a text that starts with floating point and is followed by integers, it returns the double" +
      "consuming the characters of the numbers and the point" in {
      float(".56hola") match {
        case Success(value, leftToParse) ⇒ {
          value shouldBe 0.56 +- 0.01
          leftToParse shouldBe "hola"
        }
        case _ ⇒ fail
      }
    }
  }

  "noop parser" - {
    "when passed an empty string it returns unit without consuming any input" in {
      noop("") shouldBe(Success(value = (), leftToParse = ""))
    }

    "when passed any string, it returns unit without consuming any input" in {
      noop("hola") shouldBe(Success(value = (), leftToParse = "hola"))
    }
  }

  "~> combinator" - {
    "when passed two parsers" - {
      "it requires both but only returns the result of the second one" in {
        (letter ~> digit)("a1") shouldBe(Success(value = '1', leftToParse = ""))
      }

      "fails if any of them fails" in {
        (letter ~> digit)("1a") shouldBe(Error(leftToParse = "1a"))
        (letter ~> digit)("aa") shouldBe(Error(leftToParse = "aa"))
      }
    }
  }

  "<~ combinator" - {
    "when passed two parsers" - {
      "it requires both but only returns the results of the first one" in {
        (letter <~ digit)("a1") shouldBe(Success(value = 'a', leftToParse = ""))
      }

      "fails if any of them fails" in {
        (letter <~ digit)("1a") shouldBe(Error(leftToParse = "1a"))
        (letter <~ digit)("aa") shouldBe(Error(leftToParse = "aa"))
      }
    }
  }
}
