public class CreditsRule implements EligibilityRule {

    private final int minCredits;

    public CreditsRule(int minCredits) {
        this.minCredits = minCredits;
    }

    @Override
    public RuleResult evaluate(StudentProfile s) {
        if (s.earnedCredits < minCredits) {
            return RuleResult.fail("credits below " + minCredits);
        }
        return RuleResult.pass();
    }
}