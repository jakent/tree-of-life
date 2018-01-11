package tree

import org.scalatest._
import rules.RuleParser

class TreeTest extends FlatSpec with Matchers {

  "Tree" should "say root node is dead" in {
    Tree(".").isAlive shouldEqual "."
  }

  it should "say left branch is dead" in {
    Tree("((X . X) . .)").getLeft.get.isAlive shouldEqual "."
  }

  it should "say left branch right leaf is alive" in {
    Tree("((X . X) . .)").getLeft.get.getRight.get.isAlive shouldEqual "X" //for comprehension
  }

  it should "root should have no parent" in {
    val tree = Tree(".")
    Tree.parentAlive(tree, tree) shouldEqual None
  }

  it should "node should find its parent alive" in {
    val tree = Tree("(. X .)")
    Tree.parentAlive(tree, tree.getLeft.get) shouldEqual Some(true)
  }

  it should "node should find its parent dead" in {
    val tree = Tree("(. . .)")
    Tree.parentAlive(tree, tree.getLeft.get) shouldEqual Some(false)
  }

  it should "deep node should find its parent alive" in {
    val tree = Tree("(. . (. X (. . .)))")
    val node = tree.getRight.get.getRight.get
    node shouldEqual Branch(false, Leaf(false), Leaf(false))
    Tree.parentAlive(tree, node) shouldEqual Some(true)
  }

  /**
    * Tree populations are counted with the following 4-bit representation:
    *
    *     (1)
    *      |
    *     (3)
    *    /   \
    *  (2)   (4)
    *
    *  If all nodes are alive, population is 15.
    *  If all nodes are dead, population is 0.
    */
  it should "say dead root node with no leaves has a population of 0" in {
    val tree = Tree(".")
    tree.population(tree) shouldEqual 0
  }

  it should "say alive root node with no leaves has a population of 2" in {
    val tree = Tree("X")
    tree.population(tree) shouldEqual 2
  }

  it should "say alive branch with one alive leave has a population of 6" in {
    val tree = Tree("(X X .)")
    tree.population(tree) shouldEqual 6
  }

  it should "say alive branch with alive leaves has a population of 7" in {
    val tree = Tree("(X X X)")
    tree.population(tree) shouldEqual 7
  }

  it should "say alive branch with alive leaves and parent has a population of 15" in {
    val tree = Tree("(. X (X X X))")
    tree.getRight.get.population(tree) shouldEqual 15
  }

  it should "transform one" in {
    val tree = Tree("(. . .)")
    Tree.transform(tree)(_ => true)(tree) shouldEqual Tree("(X X X)")
  }

  it should "transform two" in {
    val tree = Tree("(. . .)")
    Tree.transform(tree)(_ => false)(tree) shouldEqual Tree("(. . .)")
  }

  it should "transform three" in {
    val tree = Tree("(. . (X X .))")
    Tree.transform(tree)(RuleParser(42354).rule)(tree) shouldEqual Tree("(. X (X X X))")
  }

  it should "transform whole tree based on rules" in {
    val tree = Tree("((. X (. . .)) . (X . (. X X)))")
    Tree.transform(tree)(RuleParser(42354).rule)(tree) shouldEqual Tree("((X . (. X .)) X (. X (X . X)))")
  }
}