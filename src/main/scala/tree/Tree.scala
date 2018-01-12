package tree

sealed trait Tree {
  val alive: Boolean
  def getLeft: Option[Tree]
  def getRight: Option[Tree]

  def isAlive: String = if (alive) "X" else "."

  def population(root: Tree): Int =
    Integer.parseInt(
      booleanToString(Tree.parentAlive(root, this).getOrElse(false)) +
        booleanToString(getLeft.getOrElse(Leaf(false)).alive) +
        booleanToString(alive) +
        booleanToString(getRight.getOrElse(Leaf(false)).alive)
      , 2)

  private def booleanToString(a: Boolean): String = if (a) "1" else "0"
}

case class Leaf(alive: Boolean) extends Tree {
  def getLeft: Option[Tree] = None
  def getRight: Option[Tree] = None
}

case class Branch(alive: Boolean, left: Tree, right: Tree) extends Tree {
  def getLeft: Option[Tree] = Some(left)
  def getRight: Option[Tree] = Some(right)
}

object Tree extends TreeParser {
  def parentAlive(root: Tree, node: Tree): Option[Boolean] = {
    root match {
      case _: Leaf => None
      case s if s.getLeft.isDefined && s.getLeft.get.eq(node) => Some(s.alive)
      case s if s.getRight.isDefined && s.getRight.get.eq(node) => Some(s.alive)
      case _ => parentAlive(root.getLeft.get, node) match {
        case s@Some(_) => s
        case None if root.getRight.isDefined => parentAlive(root.getRight.get, node)
      }
    }
  }

  def transform(t: Tree)(f: Int => Boolean)(root: Tree): Tree = t match {// move root to f as a curried param
    case x: Leaf => Leaf(f(x.population(root)))
    case b@Branch(_, l, r) => Branch(f(b.population(root)), transform(l)(f)(root), transform(r)(f)(root))
  }

  def apply(initialState: String): Tree =
    parse(all, initialState) match {
      case Success(result, inl) => result
      case _ => Leaf(false)
    }
}
