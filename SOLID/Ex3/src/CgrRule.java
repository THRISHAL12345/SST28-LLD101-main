public class CgrRule implements EligibilityRule {

    private final double minCgr;

    public CgrRule(double minCgr) {
        this.minCgr = minCgr;
    }

    @Override
    public RuleResult evaluate(StudentProfile s) {
        if (s.cgr < minCgr) {
            return RuleResult.fail("CGR below " + String.format("%.1f", minCgr));
        }
        return RuleResult.pass();
    }
}