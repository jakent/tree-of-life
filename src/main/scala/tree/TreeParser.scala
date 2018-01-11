package tree

import scala.util.parsing.combinator.RegexParsers

trait TreeParser extends RegexParsers {
  val leaf: Parser[Leaf] = "X|\\.".r ^^ {
    case "." => Leaf(false)
    case "X" => Leaf(true)
  }

  val branch: Parser[Branch] = "(" ~>loop<~ ")"
  private val loop = (leaf|branch)~leaf~(leaf|branch) ^^ {
    case l~Leaf(true)~r => Branch(true, l, r)
    case l~Leaf(false)~r => Branch(false, l, r)
  }

  val all: Parser[Tree] = branch | leaf
}