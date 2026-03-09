package com.example.reports;

public class ReportProxy implements Report {

    private final String reportId;
    private final String title;
    private final String classification;

    private final AccessControl accessControl = new AccessControl();

    private RealReport realReport;

    public ReportProxy(String reportId, String title, String classification) {
        this.reportId = reportId;
        this.title = title;
        this.classification = classification;
    }

    @Override
    public void display(User user) {

        System.out.println("[proxy] request for report " + reportId);

        if (!accessControl.canAccess(user, classification)) {
            System.out.println("[proxy] ACCESS DENIED for " + user.getName());
            return;
        }

        if (realReport == null) {
            System.out.println("[proxy] lazy loading real report...");
            realReport = new RealReport(reportId, title, classification);
        }

        realReport.display(user);
    }
}