package rules

case class RuleParser(ruleEncoding: Int) {
  def binary: String = "%016d".format(ruleEncoding.toBinaryString.toLong)

  def rule(ruleNumber: Int): Boolean = binary.reverse.split("")(ruleNumber) == "1"

}