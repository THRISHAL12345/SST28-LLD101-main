public class EmailSender extends NotificationSender {

    public EmailSender(AuditLog audit) {
        super(audit);
    }

    @Override
    protected void validate(Notification n) {
        if (n.email == null || n.email.isBlank()) {
            throw new IllegalArgumentException("email required");
        }
    }

    @Override
    protected void doSend(Notification n) {
        System.out.println("EMAIL -> to=" + n.email +
                " subject=" + n.subject +
                " body=" + n.body);
    }

    @Override
    protected String auditEntry() {
        return "email sent";
    }
}