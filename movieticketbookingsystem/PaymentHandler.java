public class PaymentHandler {

    public boolean pay(double amount, PaymentChannel mode) {
        PaymentStrategy strategy = PaymentStrategyFactory.get(mode);
        return strategy.process(amount);
    }
}