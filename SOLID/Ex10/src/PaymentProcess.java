public interface PaymentProcess {
    String charge(String studentId, double amount);
}