public class AttendanceRule implements EligibilityRule {

    private final int minAttendance;

    public AttendanceRule(int minAttendance) {
        this.minAttendance = minAttendance;
    }

    @Override
    public RuleResult evaluate(StudentProfile s) {
        if (s.attendancePct < minAttendance) {
            return RuleResult.fail("attendance below " + minAttendance);
        }
        return RuleResult.pass();
    }
}