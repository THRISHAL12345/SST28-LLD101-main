public class PaymentStrategyFactory {

    public static PaymentStrategy get(PaymentChannel mode) {
        if (mode == PaymentChannel.UPI) return new UpiHandler();
        if (mode == PaymentChannel.CARD) return new CardHandler();
        return new WalletHandler();
    }
}