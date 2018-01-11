package tree

sealed trait Tree {
  val alive: Boolean
  def getLeft: Option[Tree]
  def getRight: Option[Tree]

  def isAlive: String = if (alive) "X" else "."

  private def booleanToString(a: Boolean): String = if (a) "1" else "0"

  def population(root: Tree): Int =
    Integer.parseInt(
      booleanToString(Tree.parentAlive(root, this).getOrElse(false)) +
        booleanToString(getLeft.getOrElse(Leaf(false)).alive) +
        booleanToString(alive) +
        booleanToString(getRight.getOrElse(Leaf(false)).alive)
      , 2)
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
    if (root.eq(node)) //check if leaf
      None
    else if (root.getLeft.isDefined && root.getLeft.get.eq(node))
      Some(root.alive)
    else if (root.getRight.isDefined && root.getRight.get.eq(node))
      Some(root.alive)
    else {

      if (root.getLeft.isDefined) { // this is overkill, just check if this is a leaf
        parentAlive(root.getLeft.get, node) match {
          case s @ Some(_) => return s
          case None if root.getRight.isDefined => return parentAlive(root.getRight.get, node)
          case _ =>  return None
        }
      } else if (root.getRight.isDefined) {
        parentAlive(root.getRight.get, node) match {
          case s @ Some(_) => return s
          case _ =>  return None
        }
      }
      None
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
