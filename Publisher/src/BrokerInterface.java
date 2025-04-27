import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface BrokerInterface extends Remote {
    /**
     * Creates a new topic in the broker.
     *
     * @param id The unique identifier of the topic.
     * @param name The name of the topic.
     * @param pName The name of the publisher creating the topic.
     * @return A success or error message.
     * @throws RemoteException If a remote error occurs.
     */
    String createTopic(String id, String name, String pName) throws RemoteException;

    /**
     * Publishes a message to a specific topic.
     *
     * @param tId The unique topic ID.
     * @param pName The name of the publisher.
     * @param message The message to be published.
     * @return A success or error message.
     * @throws RemoteException If a remote error occurs.
     */
    String publishEvent(String tId, String pName, String message) throws RemoteException;

    /**
     * Shows the subscriber count for a specific topic.
     *
     * @param tId The topic ID.
     * @param pName The publisher name.
     * @return The subscriber count or an error message.
     * @throws RemoteException If a remote error occurs.
     */
    String showCount(String tId, String pName) throws RemoteException;

    /**
     * Shows the subscriber count for all topics of the publisher.
     *
     * @param pName The publisher name.
     * @return A list of subscriber counts for each topic.
     * @throws RemoteException If a remote error occurs.
     */
    ArrayList<String> showCount(String pName) throws RemoteException;

    /**
     * Deletes a specific topic and unsubscribes all its subscribers.
     *
     * @param tId The topic ID.
     * @param pName The publisher name.
     * @return A success or error message.
     * @throws RemoteException If a remote error occurs.
     */
    String deleteTopic(String tId, String pName) throws RemoteException;

    /**
     * Deletes all topics associated with a publisher.
     *
     * @param pName The publisher name.
     * @return A success or error message.
     * @throws RemoteException If a remote error occurs.
     */
    String deletePAll(String pName) throws RemoteException;

    /**
     * Registers a new publisher in the broker.
     *
     * @param pName The publisher name.
     * @return A success or error message.
     * @throws RemoteException If a remote error occurs.
     */
    String newPublisher(String pName) throws RemoteException;
}
