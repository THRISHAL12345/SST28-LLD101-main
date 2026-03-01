public class Main {
    public static void main(String[] args) {

        System.out.println("=== Evaluation Pipeline ===");

        Submission sub = new Submission("23BCS1007", "public class A{}", "A.java");

        PlagiarismCheck pc = new PlagiarismChecker();
        CodeGrade grader = new CodeGrader(new Rubric());
        ReportWrite writer = new ReportWriter();

        EvaluationPipeline pipeline =
                new EvaluationPipeline(pc, grader, writer);

        pipeline.evaluate(sub);
    }
}