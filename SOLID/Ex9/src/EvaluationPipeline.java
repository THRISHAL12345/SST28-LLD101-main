public class EvaluationPipeline {

    private final PlagiarismCheck plagiarism;
    private final CodeGrade grader;
    private final ReportWrite writer;

    public EvaluationPipeline(
            PlagiarismCheck plagiarism,
            CodeGrade grader,
            ReportWrite writer
    ) {
        this.plagiarism = plagiarism;
        this.grader = grader;
        this.writer = writer;
    }

    public void evaluate(Submission sub) {

        int plag = plagiarism.check(sub);
        System.out.println("PlagiarismScore=" + plag);

        int code = grader.grade(sub);
        System.out.println("CodeScore=" + code);

        String reportName = writer.write(sub, plag, code);
        System.out.println("Report written: " + reportName);

        int total = plag + code;
        String result = (total >= 90) ? "PASS" : "FAIL";
        System.out.println("FINAL: " + result + " (total=" + total + ")");
    }
}