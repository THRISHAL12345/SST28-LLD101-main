public class PaymentGateway implements PaymentProcess {

    @Override
    public String charge(String studentId, double amount) {
        return "TXN-9001";
    }
}