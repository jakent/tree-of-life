import org.scalatest.{FlatSpec, Matchers}
import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}
import scala.io.Source

class SolutionTest extends FlatSpec with Matchers {

  "Tree of Life" should "work" in {
    val testFile = Source.fromURL(getClass.getResource("/basic/test"))
    val test = testFile.getLines().mkString("\n")

    val expectedResultFile = Source.fromURL(getClass.getResource("/basic/expected"))
    val expectedResult = expectedResultFile.getLines().mkString("\n")

    val myOut = new ByteArrayOutputStream
    Console.withOut(new PrintStream(myOut)) {
      System.setIn(new ByteArrayInputStream(test.getBytes()))

      Solution.main(Array.empty)
    }

    myOut.toString shouldEqual expectedResult
  }

}
