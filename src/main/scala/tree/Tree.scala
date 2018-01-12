package tree

import scala.annotation.tailrec

sealed trait Tree {
  val alive: Boolean
  def getLeft: Option[Tree]
  def getRight: Option[Tree]
  def children: Seq[Tree]


  def isAlive: String = if (alive) "X" else "."

  def parentAlive(root: Tree): Boolean = Tree.traverse[Boolean](
    {
      case _: Leaf => false
      case s: Branch if s.left.eq(this) || s.right.eq(this) => s.alive
      case _ => false
    }, _|_, {x: Boolean => x}, _ ++ _.children)(List(root), false)
}

case class Leaf(alive: Boolean) extends Tree {
  override def children = Nil
  def getLeft: Option[Tree] = None
  def getRight: Option[Tree] = None
}

case class Branch(alive: Boolean, left: Tree, right: Tree) extends Tree {
  override def children: Seq[Tree] = Seq(left, right)
  def getLeft: Option[Tree] = Some(left)
  def getRight: Option[Tree] = Some(right)
}

object Tree extends TreeParser {

//  def parentAlive(root: Tree, node: Tree): Option[Boolean] = {
//    root match {
//      case _: Leaf => None
//      case s if s.getLeft.isDefined && s.getLeft.get.eq(node) => Some(s.alive)
//      case s if s.getRight.isDefined && s.getRight.get.eq(node) => Some(s.alive)
//      case _ => parentAlive(root.getLeft.get, node) match {
//        case s@Some(_) => s
//        case None if root.getRight.isDefined => parentAlive(root.getRight.get, node)
//      }
//    }
//  }

  @tailrec
  final def traverse[R](f: Tree => R, g: (R, R) => R, q: R => Boolean, z: (List[Tree], Tree) => List[Tree])(ts: List[Tree], r: R): R =
    if (q(r))
      r
    else
      ts match {
        case Nil => r
        case h :: t => traverse(f, g, q, z)(z(t, h), g(r, f(h)))
      }

  def population(root: Tree)(node: Tree): Int = {
    def booleanToString(a: Boolean): String = if (a) "1" else "0"

    Integer.parseInt(
      booleanToString(node.parentAlive(root)) +
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
