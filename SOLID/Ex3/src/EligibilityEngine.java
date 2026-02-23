import java.util.*;

public class EligibilityEngine {

    private final FakeEligibilityStore store;
    private final List<EligibilityRule> rules;

    public EligibilityEngine(FakeEligibilityStore store) {
        this.store = store;
        this.rules = List.of(
                new DisciplinaryRule(),
                new CgrRule(8.0),
                new AttendanceRule(75),
                new CreditsRule(20)
        );
    }

    public void runAndPrint(StudentProfile s) {
        ReportPrinter p = new ReportPrinter();
        EligibilityEngineResult r = evaluate(s);
        p.print(s, r);
        store.save(s.rollNo, r.status);
    }

    public EligibilityEngineResult evaluate(StudentProfile s) {

        for (EligibilityRule rule : rules) {
            RuleResult result = rule.evaluate(s);
            if (!result.passed) {
                return new EligibilityEngineResult(
                        "NOT_ELIGIBLE",
                        result.reasons
                );
            }
        }

        return new EligibilityEngineResult("ELIGIBLE", List.of());
    }
}