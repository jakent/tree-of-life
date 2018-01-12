package tree

import org.scalatest._

class TreeParserTest extends FunSpec with Matchers {

  describe("TreeParser") {
    it("construct a single alive leaf") {
      Tree("X") shouldEqual Leaf(true)
    }

    it("construct a single dead leaf") {
      Tree(".") shouldEqual Leaf(false)
    }

    it("construct a dead branch holding left alive leaf and a right dead leaf") {
      Tree("(X . .)") shouldEqual Branch(false, Leaf(true), Leaf(false))
    }

    it("construct a dead branch holding left alive leaf and a right branch same as previous test") {
      Tree("(X . (X . .))") shouldEqual Branch(false, Leaf(true), Branch(false, Leaf(true), Leaf(false)))
    }

    it("construct a dead branch holding two symmetric branches") {
      Tree("((X . .) . (X . .))") shouldEqual Branch(false, Branch(false, Leaf(true), Leaf(false)), Branch(false, Leaf(true), Leaf(false)))
    }
  }

}