package net.notfab.eventti;

public enum ListenerPriority {

    LOWEST(0), LOW(1), NORMAL(2), HIGH(3), HIGHEST(4);

    private int weight;

    ListenerPriority(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

}