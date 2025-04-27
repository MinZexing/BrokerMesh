import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface BrokerInterface extends Remote {

    /**
     * Lists all available topics in the system.
     *
     * @return A list of topics, each represented by an array containing the topic ID and name.
     * @throws RemoteException If an error occurs during remote communication.
     */
    ArrayList<String[]> listTopics() throws RemoteException;

    /**
     * Subscribes a subscriber to a topic by topic ID.
     *
     * @param id   The unique ID of the topic.
     * @param name The name of the subscriber.
     * @return A success or error message.
     * @throws RemoteException If an error occurs during remote communication.
     */
    String subscribe(String id, String name) throws RemoteException;

    /**
     * Shows the current subscriptions of a subscriber.
     *
     * @param subscriberName The name of the subscriber.
     * @return A list of currently subscribed topics, each represented by an array containing the topic ID and name.
     * @throws RemoteException If an error occurs during remote communication.
     */
    ArrayList<String[]> showCurrent(String subscriberName) throws RemoteException;

    /**
     * Unsubscribes a subscriber from a specific topic.
     *
     * @param tId           The unique ID of the topic.
     * @param subscriberName The name of the subscriber.
     * @return A success or error message.
     * @throws RemoteException If an error occurs during remote communication.
     */
    String unsubscribe(String tId, String subscriberName) throws RemoteException;

    /**
     * Removes a subscriber from all topics they are subscribed to.
     *
     * @param sName The name of the subscriber.
     * @return A success or error message.
     * @throws RemoteException If an error occurs during remote communication.
     */
    String deleteSAll(String sName) throws RemoteException;

    /**
     * Registers a new subscriber in the system.
     *
     * @param pName The name of the subscriber.
     * @return A success or error message.
     * @throws RemoteException If an error occurs during remote communication.
     */
    String newSubscriber(String pName) throws RemoteException;
}
