package net.notfab.eventti;

public interface Cancellable {

    void setCancelled(boolean cancelled);

    boolean isCancelled();

}