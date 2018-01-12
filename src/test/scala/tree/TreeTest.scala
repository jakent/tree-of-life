package tree

import org.scalatest._
import rules.RuleParser

class TreeTest extends FunSpec with Matchers {

  describe("Tree") {

    describe("isAlive") {
      it("say root node is dead") {
        Tree(".").isAlive shouldEqual "."
      }

      it("say left branch is dead") {
        Tree("((X . X) . .)").getLeft.get.isAlive shouldEqual "."
      }

      it("say left branch right leaf is alive") {
        Tree("((X . X) . .)").getLeft.get.getRight.get.isAlive shouldEqual "X" //for comprehension
      }
    }

    describe("parentAlive") {
      it("root should have no parent") {
        val tree = Tree(".")
        Tree.parentAlive(tree, tree) shouldEqual None
      }

      it("node should find its parent alive") {
        val tree = Tree("(. X .)")
        Tree.parentAlive(tree, tree.getLeft.get) shouldEqual Some(true)
      }

      it("node should find its parent dead") {
        val tree = Tree("(. . .)")
        Tree.parentAlive(tree, tree.getLeft.get) shouldEqual Some(false)
      }

      it("deep node should find its parent alive") {
        val tree = Tree("(. . (. X (. . .)))")
        val node = tree.getRight.get.getRight.get
        node shouldEqual Branch(false, Leaf(false), Leaf(false))
        Tree.parentAlive(tree, node) shouldEqual Some(true)
      }
    }

    describe("population") {
      /**
        * Tree populations are counted with the following 4-bit representation:
        *
        * (1)
        * |
        * (3)
        * /  \
        * (2)  (4)
        *
        * If all nodes are alive, population is 15.
        * If all nodes are dead, population is 0.
        */
      it("say dead root node with no leaves has a population of 0") {
        val tree = Tree(".")
        Tree.population(tree)(tree) shouldEqual 0
      }

      it("say alive root node with no leaves has a population of 2") {
        val tree = Tree("X")
        Tree.population(tree)(tree) shouldEqual 2
      }

      it("say alive branch with one alive leave has a population of 6") {
        val tree = Tree("(X X .)")
        Tree.population(tree)(tree) shouldEqual 6
      }

      it("say alive branch with alive leaves has a population of 7") {
        val tree = Tree("(X X X)")
        Tree.population(tree)(tree) shouldEqual 7
      }

      it("say alive branch with alive leaves and parent has a population of 15") {
        val tree = Tree("(. X (X X X))")
        Tree.population(tree)(tree.getRight.get) shouldEqual 15
      }
    }

    describe("transform") {
      it("transform one") {
        val tree = Tree("(. . .)")
        Tree.transform(tree)(_ => true) shouldEqual Tree("(X X X)")
      }

      it("transform two") {
        val tree = Tree("(. . .)")
        Tree.transform(tree)(_ => false) shouldEqual Tree("(. . .)")
      }

      it("transform three") {
        val tree = Tree("(. . (X X .))")
        val f: Tree => Boolean = x => RuleParser(42354).rule(Tree.population(tree)(x))
        Tree.transform(tree)(f) shouldEqual Tree("(. X (X X X))")
      }

      it("transform whole tree based on rules") {
        val tree = Tree("((. X (. . .)) . (X . (. X X)))")
        val f: Tree => Boolean = x => RuleParser(42354).rule(Tree.population(tree)(x))
        Tree.transform(tree)(f) shouldEqual Tree("((X . (. X .)) X (. X (X . X)))")
      }
    }

  }
}