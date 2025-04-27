import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;

public class Network extends UnicastRemoteObject implements NetworkInterface {

    ArrayList<NetworkInterface> otherBrokers = new ArrayList<NetworkInterface>(){};

    private final Registry registry;

    LocalStore localStore;

    static int minusOne = -1;
    static int number1 = 1;

    /**
     * Constructor for Network, initializing the registry and local store.
     *
     * @param localStore   The local store containing topics and subscribers.
     * @param registryPort The port of the registry.
     * @throws RemoteException If a remote communication error occurs.
     */
    public Network(LocalStore localStore, int registryPort) throws RemoteException{

        super();
        this.localStore = localStore;
        this.registry = LocateRegistry.getRegistry(registryPort);
    }

    /**
     * Adds a new broker to the network by name.
     *
     * @param name The name of the broker to add.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void newBroker(String name) throws RemoteException{
        try{
            NetworkInterface obj = (NetworkInterface) registry.lookup(name);
            handleNewBrokerAdd(obj);
        }
        catch(Exception e){
            System.out.println("Error occurred! " + e.getMessage());
        }
    }
    /**
     * Adds the new broker interface to the list of other brokers.
     *
     * @param obj The broker interface to add.
     */
    public void handleNewBrokerAdd(NetworkInterface obj){
        otherBrokers.add(obj);
    }


    /**
     * Returns the global count of subscribers for a given topic.
     *
     * @param id The topic ID.
     * @return The number of subscribers.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public int showCountGlobal(String id) throws RemoteException{
        return localStore.count(id);
    }

    /**
     * Lists all topics globally.
     *
     * @return A list of topics.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public ArrayList<String[]> listTopicsGlobal() throws RemoteException{
        return localStore.getAllTopic();
    }

    /**
     * Removes a subscriber globally based on their ID.
     *
     * @param id The subscriber ID.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void removeSubGlobal(String id) throws RemoteException{
        localStore.removeSubscriber(id);
    }



    /**
     * Checks if a topic ID is unique globally.
     *
     * @param id The topic ID to check.
     * @return -1 if not unique, 1 if unique.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public int checkUniqueTopic(String id) throws RemoteException{
        for(Topic topic: localStore.topics){
            if(Objects.equals(topic.id, id)){
                return minusOne;
            }
        }
        return number1;
    }


    /**
     * Returns the number of publishers globally.
     *
     * @return The number of publishers.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public int checkNumPublisher() throws RemoteException{
        return localStore.checkNumberOfPublisher();
    }


    /**
     * Returns the number of subscribers globally.
     *
     * @return The number of subscribers.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public int checkNumSubscriber() throws RemoteException{
        return localStore.checkNumberofSubscriber();
    }

    /**
     * Removes all subscribers globally for a given publisher name.
     *
     * @param pName The publisher name.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void removeAllSubGlobal(String pName) throws RemoteException{
        localStore.removeAll(pName);
    }

    /**
     * Checks if a publisher exists globally.
     *
     * @param pName The name of the publisher to check.
     * @return A string message indicating the result.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public String checkPublisher(String pName) throws RemoteException{
        return localStore.checkexistPublisher(pName);
    }

    /**
     * Notifies all brokers of a new message globally.
     *
     * @param id      The topic ID.
     * @param message The message to be published.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void notifyMessageGlobal(String id, String message) throws RemoteException{
        localStore.publish(id,message);
    }
}
