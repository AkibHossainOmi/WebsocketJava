package telcobright.app.callService;

public interface PhoneCall {
    public void startSession();
    public void updateSession();
    public void disconnect();
    public void onRing();
    public void onAnswer();
}
