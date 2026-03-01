public class SecretaryTool implements MinutesOps {

    private final MinutesBook minutes;

    public SecretaryTool(MinutesBook minutes) {
        this.minutes = minutes;
    }

    @Override
    public void addMinutes(String text) {
        minutes.add(text);
    }
}