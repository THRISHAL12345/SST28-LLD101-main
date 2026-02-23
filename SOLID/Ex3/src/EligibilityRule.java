public interface EligibilityRule {
    RuleResult evaluate(StudentProfile s);
}