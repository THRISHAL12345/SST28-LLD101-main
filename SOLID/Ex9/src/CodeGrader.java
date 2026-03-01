public class CodeGrader implements CodeGrade {

    private final Rubric rubric;

    public CodeGrader(Rubric rubric) {
        this.rubric = rubric;
    }

    @Override
    public int grade(Submission s) {
        int base = Math.min(80, 50 + s.code.length() % 40);
        return base + rubric.bonus;
    }
}