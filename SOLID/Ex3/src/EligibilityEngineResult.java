import java.util.List;

public class EligibilityEngineResult {

    public final String status;     // "ELIGIBLE" or "NOT_ELIGIBLE"
    public final List<String> reasons;

    public EligibilityEngineResult(String status, List<String> reasons) {
        this.status = status;
        this.reasons = reasons;
    }
}