package net.notfab.eventti;

public class ExampleListener implements Listener {

    private Runnable onNormal;
    private Runnable onHigh;

    protected ExampleListener(Runnable onNormal, Runnable onHigh) {
        this.onNormal = onNormal;
        this.onHigh = onHigh;
    }

    @EventHandler
    public void onEventNormal(ExampleEvent exampleEvent) {
        this.onNormal.run();
    }

    @EventHandler(priority = ListenerPriority.HIGH)
    public void onEventHigh(ExampleEvent exampleEvent) {
        this.onHigh.run();
    }

}
