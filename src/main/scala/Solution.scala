import rules.RuleParser
import tree.{Leaf, Tree}

import io.Source

object Solution {

  def main(args: Array[String]) {
    val lines = Source.stdin.getLines

    val ruleNumber = lines.next.toInt
    val serializedTree = lines.next
    val nTests = lines.next.toInt

    val rules = RuleParser(ruleNumber)
    val tree = Tree(serializedTree)
    val tests = lines.take(nTests).toList

    tests.foldLeft(0)((i, command) => {
      val iterationsAndDirections = command.split(" ") match {
        case Array(f1, f2) => (f1.toInt, f2)
      }
      val iterations = i + iterationsAndDirections._1
      val newTree = (1 to iterations).foldLeft(tree)((t, _) =>
        Tree.transform(t)(rules.rule)(t))

      println(
        iterationsAndDirections._2.replaceAll("\\[|\\]", "").foldLeft(newTree)((t, direction) => direction match {
          case '<' => t.getLeft.getOrElse(Leaf(false))
          case '>' => t.getRight.getOrElse(Leaf(false))
        }).isAlive)

      iterations
    })
  }
}