package tree

import org.scalatest._

class TreeParserTest extends FlatSpec with Matchers {

  "TreeParser" should "construct a single alive leaf" in {
    Tree("X") shouldEqual Leaf(true)
  }

  it should "construct a single dead leaf" in {
    Tree(".") shouldEqual Leaf(false)
  }

  it should "construct a dead branch holding left alive leaf and a right dead leaf" in {
    Tree("(X . .)") shouldEqual Branch(false, Leaf(true), Leaf(false))
  }

  it should "construct a dead branch holding left alive leaf and a right branch same as previous test" in {
    Tree("(X . (X . .))") shouldEqual Branch(false, Leaf(true), Branch(false, Leaf(true), Leaf(false)))
  }

  it should "construct a dead branch holding two symmetric branches" in {
    Tree("((X . .) . (X . .))") shouldEqual Branch(false, Branch(false, Leaf(true), Leaf(false)), Branch(false, Leaf(true), Leaf(false)))
  }

}