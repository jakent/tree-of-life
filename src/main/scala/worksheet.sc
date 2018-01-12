import scala.annotation.tailrec


trait Tree {
  def children: Seq[Tree]

  def size: Int = Tree.traverse[Int](
    { _ => 1 }, _+_, { x => false}, _ ++ _.children)(List(this), 0)

  def includes(node: Tree): Boolean = Tree.traverse[Boolean](
    { _.eq(node) }, _|_, {x: Boolean => x}, _ ++ _.children)(List(this), false)

  def parentAlive(root: Tree): Boolean = Tree.traverse[Boolean](
    {
      case _: Leaf => false
      case s: Branch if s.left.eq(this) || s.right.eq(this) => s.alive
      case _ => false
    }, _|_, {x: Boolean => x}, _ ++ _.children)(List(root), false)

  def transform(f: Tree => Boolean): Tree = Tree.traverse[Tree](
    { f(_) }, _+_, { x => false}, _ ++ _.children)(List(this), 0)
}

object Tree {

    /**
      * Tail-recursive method to traverse a tree-like object (defined as something that implements Parent).
      *
      * @param f  the map function which takes a T and produces an R for just that parent object (not its children).
      * @param g  the reduce function which accumulates the result (the accumulator, i.e. r, is passed as its first parameter).
      * @param q  the quick return function which, if q(r) yields true, an immediate return of r is made.
      * @param z  the function which builds the T list (ts) from the existing T list and the given T (parent node).
      * @param ts a list of Ts to be worked on.
      * @param r  the current value of the result, i.e. the "accumulator".
      * @tparam R the result type.
      * @return a value of R.
      */
  @tailrec
  final def traverse[R](f: Tree => R, g: (R, R) => R, q: R => Boolean, z: (List[Tree], Tree) => List[Tree])(ts: List[Tree], r: R): R =
    if (q(r))
      r
    else
      ts match {
        case Nil => r
        case h :: t => traverse(f, g, q, z)(z(t, h), g(r, f(h)))
      }
}


case class Leaf(alive: Boolean) extends Tree {
  override def children = Nil
}

case class Branch(alive: Boolean, left: Tree, right: Tree) extends Tree {
  override def children: Seq[Tree] = Seq(left, right)
}

val aLeaf = Leaf(false)
val tree = Branch(true, Branch(true, Branch(false, Leaf(true), aLeaf), Leaf(false)), Branch(true, Leaf(true), Leaf(false)))
tree.size

tree.includes(aLeaf)
tree.includes(Leaf(false))

aLeaf.parentAlive(tree)




