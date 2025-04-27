import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface NetworkInterface extends Remote {

    /**
     * Notifies other brokers globally about a new message.
     *
     * @param id      The topic ID.
     * @param message The message to be sent globally.
     * @throws RemoteException If a remote communication error occurs.
     */
    void notifyMessageGlobal(String id, String message) throws RemoteException;

    /**
     * Shows the global count of subscribers for a specific topic.
     *
     * @param id The topic ID.
     * @return The number of subscribers for the topic.
     * @throws RemoteException If a remote communication error occurs.
     */
    int showCountGlobal(String id) throws RemoteException;
    /**
     * Checks if a topic is unique globally.
     *
     * @param id The topic ID to check.
     * @return -1 if the topic already exists, 1 if it is unique.
     * @throws RemoteException If a remote communication error occurs.
     */
    int checkUniqueTopic(String id) throws RemoteException;

    /**
     * Removes all subscribers for a given publisher globally.
     *
     * @param pName The name of the publisher.
     * @throws RemoteException If a remote communication error occurs.
     */
    void removeAllSubGlobal(String pName) throws RemoteException;

    /**
     * Removes a subscriber globally based on the subscriber ID.
     *
     * @param id The subscriber ID to remove.
     * @throws RemoteException If a remote communication error occurs.
     */
    void removeSubGlobal(String id) throws RemoteException;


    /**
     * Checks if a publisher exists globally.
     *
     * @param pName The name of the publisher to check.
     * @return A success or error message indicating the result.
     * @throws RemoteException If a remote communication error occurs.
     */
    String checkPublisher(String pName) throws RemoteException;
    /**
     * Lists all topics globally across brokers.
     *
     * @return A list of topics, each represented by an array of topic ID, topic name, and publisher name.
     * @throws RemoteException If a remote communication error occurs.
     */
    ArrayList<String[]> listTopicsGlobal() throws RemoteException;
    /**
     * Adds a new broker to the network.
     *
     * @param name The name of the new broker.
     * @throws RemoteException If a remote communication error occurs.
     */
    void newBroker(String name) throws RemoteException;

    /**
     * Returns the global number of publisher.
     *
     * @return The total number of publisher.
     * @throws RemoteException If a remote communication error occurs.
     */
    int checkNumPublisher() throws RemoteException;

    /**
     * Returns the global number of subscribers.
     *
     * @return The total number of subscribers.
     * @throws RemoteException If a remote communication error occurs.
     */
    int checkNumSubscriber() throws  RemoteException;

}
