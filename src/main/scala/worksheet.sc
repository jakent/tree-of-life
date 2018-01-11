import tree.Tree


Tree("(. . .)").eq(Tree("(. . .)"))

Tree("(. . .)") == Tree("(. . .)")

"[asdf]".replaceAll("\\[|\\]", "")



(1 to 0).foreach(println("hello", _))


Array("hello", "goodbye") match {
  case Array(f1,f2) => (f1,f2)
  case _ => ("womp", "womp")
}