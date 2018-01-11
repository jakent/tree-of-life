import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import scala.concurrent.duration._


val f1 = Future{
  Thread.sleep(2000); "X"
}
val f2 = Future{
  "."
}
val f3 = Future{
  "X"
}

val p = "q"

val list = List(f1, f2, f3)

val eventualStrings: Future[List[String]] = Future.sequence(list)
//eventualStrings onComplete {
//  case Success(value) => println(value)
//  case Failure(e) => e.printStackTrace()
//}


Await.result(eventualStrings, 10000 seconds).mkString("\n")
