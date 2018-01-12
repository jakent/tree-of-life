package tree

sealed trait Tree {
  val alive: Boolean
  def getLeft: Option[Tree]
  def getRight: Option[Tree]

  def isAlive: String = if (alive) "X" else "."
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

  def population(root: Tree)(node: Tree): Int = {
    def booleanToString(a: Boolean): String = if (a) "1" else "0"

    Integer.parseInt(
      booleanToString(Tree.parentAlive(root, node).getOrElse(false)) +
        booleanToString(node.getLeft.getOrElse(Leaf(false)).alive) +
        booleanToString(node.alive) +
        booleanToString(node.getRight.getOrElse(Leaf(false)).alive)
      , 2)
  }

  def transform(t: Tree)(f: Tree => Boolean): Tree = t match {
    case x: Leaf => Leaf(f(x))
    case b@Branch(_, l, r) => Branch(f(b), transform(l)(f), transform(r)(f))
  }

  def apply(initialState: String): Tree =
    parse(all, initialState) match {
      case Success(result, inl) => result
      case _ => Leaf(false)
    }
}
