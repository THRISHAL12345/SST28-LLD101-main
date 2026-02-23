public abstract class NotificationSender {

    protected final AuditLog audit;

    protected NotificationSender(AuditLog audit) {
        this.audit = audit;
    }

    public final void send(Notification n) {

        if (n == null) {
            throw new IllegalArgumentException("Notification cannot be null");
        }

        String subject = n.subject == null ? "" : n.subject;
        String body = n.body == null ? "" : n.body;

        Notification safe =
                new Notification(subject, body, n.email, n.phone);

        validate(safe);
        doSend(safe);
        audit.add(auditEntry());
    }

    protected abstract void validate(Notification n);

    protected abstract void doSend(Notification n);

    protected abstract String auditEntry();
}