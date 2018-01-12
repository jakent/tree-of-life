import rules.RuleParser
import tree.{Leaf, Tree}

import io.Source
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object Solution {

  import scala.concurrent.ExecutionContext.Implicits.global

  def main(args: Array[String]) {
    val lines = Source.stdin.getLines

    val ruleNumber = lines.next.toInt
    val serializedTree = lines.next
    val nTests = lines.next.toInt

    val rules = RuleParser(ruleNumber)
    val tree = Tree(serializedTree)
    val tests = lines.take(nTests).toList

    val futures = tests.foldLeft((0, Nil: List[Future[String]]))((i, command) => {
      val iterationsAndDirections = command.split(" ") match {
        case Array(f1, f2) => (f1.toInt, f2)
      }
      val iterations = i._1 + iterationsAndDirections._1

      val future = createFuture(iterations, tree, rules, iterationsAndDirections._2)

      (iterations, i._2 :+ future)
    })._2

    val eventualStrings = Future.sequence(futures)
    println(Await.result(eventualStrings, 10000 seconds).mkString("\n"))
  }

  def createFuture(iterations: Int, tree: Tree, rules: RuleParser, directions: String): Future[String] = Future {
    val newTree = (1 to iterations).foldLeft(tree)((t, _) =>
      Tree.transform(t)(rules.rule)(t))

    directions.replaceAll("\\[|\\]", "").foldLeft(newTree)((t, direction) => direction match {
      case '<' => t.getLeft.getOrElse(Leaf(false))
      case '>' => t.getRight.getOrElse(Leaf(false))
    }).isAlive
  }
}