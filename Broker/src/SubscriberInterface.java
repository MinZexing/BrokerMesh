import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SubscriberInterface extends Remote {
    public String receiveMessage(String tId, String tName, String message) throws RemoteException;
}
