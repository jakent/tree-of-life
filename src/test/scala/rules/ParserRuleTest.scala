package rules

import org.scalatest._

class ParserRuleTest extends FunSpec with Matchers {

  describe("Rule Parser") {
    it("find 16-bit binary representation of rule encoding") {
      RuleParser(42354).binary shouldEqual "1010010101110010"
      RuleParser(7710).binary shouldEqual "0001111000011110"
    }

    it("create a rule sheet") {
      RuleParser(42354).rule(15) shouldEqual true
      RuleParser(42354).rule(14) shouldEqual false
    }
  }

}
