package client.listeners;

@FunctionalInterface
public interface ErrorListener {

    void onError(Exception e);
}
