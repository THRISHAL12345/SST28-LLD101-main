public abstract class Exporter {

    public final ExportResult export(ExportRequest req) {

        if (req == null) {
            throw new IllegalArgumentException("ExportRequest cannot be null");
        }

        String safeTitle = req.title == null ? "" : req.title;
        String safeBody = req.body == null ? "" : req.body;

        return doExport(new ExportRequest(safeTitle, safeBody));
    }
    protected abstract ExportResult doExport(ExportRequest req);
}