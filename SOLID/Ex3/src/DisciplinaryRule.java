public class DisciplinaryRule implements EligibilityRule {

    @Override
    public RuleResult evaluate(StudentProfile s) {
        if (s.disciplinaryFlag != LegacyFlags.NONE) {
            return RuleResult.fail("disciplinary flag present");
        }
        return RuleResult.pass();
    }
}