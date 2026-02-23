import java.util.*;

public class RuleResult {
    public final boolean passed;
    public final List<String> reasons;

    public RuleResult(boolean passed, List<String> reasons) {
        this.passed = passed;
        this.reasons = reasons;
    }

    public static RuleResult pass() {
        return new RuleResult(true, Collections.emptyList());
    }

    public static RuleResult fail(String reason) {
        return new RuleResult(false, List.of(reason));
    }
}