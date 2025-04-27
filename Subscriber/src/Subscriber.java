import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Subscriber extends UnicastRemoteObject implements SubscriberInterface {

    private String name;

    /**
     * Constructor to initialize the Subscriber with a name.
     *
     * @param name The name of the subscriber.
     * @throws RemoteException if an RMI error occurs during object export.
     */
    public Subscriber(String name) throws RemoteException{
        super();
        this.name = name;
    }

    /**
     * Retrieves the name of the subscriber.
     *
     * @return The name of the subscriber.
     */
    public String getName() { return name; }

    /**
     * Receives a message from the broker for a subscribed topic and displays it with a timestamp.
     *
     * @param tId   The unique identifier of the topic.
     * @param tName The name of the topic.
     * @param message   The message to be displayed.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public String receiveMessage(String tId, String tName, String message) throws RemoteException{
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM HH:mm:ss")) + " " + tId + " " + tName + " " + message);
        return "success";
    }
}
