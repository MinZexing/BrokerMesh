import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface BrokerInterface extends Remote {

    //for publisher
    /**
     * Publishes a message to a specific topic.
     *
     * @param tId     The unique topic ID.
     * @param pName   The name of the publisher.
     * @param message The message to be published.
     * @return A success or error message.
     * @throws RemoteException If there is a remote communication issue.
     */
    String  publishEvent(String tId, String pName, String message) throws RemoteException;

    /**
     * Shows the subscriber count for a specific topic.
     *
     * @param tId   The unique topic ID.
     * @param pName The name of the publisher.
     * @return The subscriber count or an error message.
     * @throws RemoteException If there is a remote communication issue.
     */
    String showCount(String tId, String pName) throws RemoteException;

    /**
     * Shows the subscriber count for all topics associated with a specific publisher.
     *
     * @param pName The name of the publisher.
     * @return A list of subscriber counts for each topic.
     * @throws RemoteException If there is a remote communication issue.
     */
    ArrayList<String> showCount(String pName) throws RemoteException;

    /**
     * Creates a new topic.
     *
     * @param id    The unique topic ID.
     * @param name  The name of the topic.
     * @param pName The name of the publisher.
     * @return A success or error message.
     * @throws RemoteException If there is a remote communication issue.
     */
    String createTopic(String id, String name, String pName) throws RemoteException;

    /**
     * Deletes a specific topic and unsubscribes all subscribers.
     *
     * @param tId   The unique topic ID.
     * @param pName The name of the publisher.
     * @return A success or error message.
     * @throws RemoteException If there is a remote communication issue.
     */
    String deleteTopic(String tId, String pName) throws RemoteException;

    /**
     * Deletes all topics associated with a specific publisher and unsubscribes all subscribers.
     *
     * @param pName The name of the publisher.
     * @return A success or error message.
     * @throws RemoteException If there is a remote communication issue.
     */
    String deletePAll(String pName) throws RemoteException;

    /**
     * Registers a new publisher.
     *
     * @param pName The name of the publisher.
     * @return A success or error message.
     * @throws RemoteException If there is a remote communication issue.
     */
    String newPublisher(String pName) throws RemoteException;


    //For subscriber
    /**
     * Lists all available topics in the system.
     *
     * @return A list of topics, each represented by an array containing the topic ID, name, and publisher name.
     * @throws RemoteException If there is a remote communication issue.
     */
    ArrayList<String[]> listTopics() throws RemoteException;
    /**
     * Unsubscribes a subscriber from a specific topic.
     *
     * @param tId           The unique topic ID.
     * @param subscriberName The name of the subscriber.
     * @return A success or error message.
     * @throws RemoteException If there is a remote communication issue.
     */
    String unsubscribe(String tId, String subscriberName) throws RemoteException;

    /**
     * Subscribes a subscriber to a specific topic by topic ID.
     *
     * @param id   The unique topic ID.
     * @param name The name of the subscriber.
     * @return A success or error message.
     * @throws RemoteException If there is a remote communication issue.
     */
    String subscribe(String id, String name) throws RemoteException;


    /**
     * Registers a new subscriber.
     *
     * @param pName The name of the subscriber.
     * @return A success or error message.
     * @throws RemoteException If there is a remote communication issue.
     */
    String newSubscriber(String pName) throws RemoteException;

    /**
     * Shows the current subscriptions of a subscriber.
     *
     * @param subscriberName The name of the subscriber.
     * @return A list of currently subscribed topics.
     * @throws RemoteException If there is a remote communication issue.
     */
    ArrayList<String[]> showCurrent(String subscriberName) throws RemoteException;

    /**
     * Deletes all subscriptions associated with a specific subscriber.
     *
     * @param sName The name of the subscriber.
     * @return A success or error message.
     * @throws RemoteException If there is a remote communication issue.
     */
    String deleteSAll(String sName) throws RemoteException;
}
