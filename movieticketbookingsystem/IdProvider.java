import java.util.UUID;

public class IdProvider {
    public static String generate() {
        return UUID.randomUUID().toString();
    }
}