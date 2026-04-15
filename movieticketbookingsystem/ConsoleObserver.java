public class ConsoleObserver implements ObserverContract {
    public void update(String msg) {
        System.out.println(msg);
    }
}