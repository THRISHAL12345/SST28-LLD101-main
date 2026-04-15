public interface PaymentStrategy {
    boolean process(double amount);
}