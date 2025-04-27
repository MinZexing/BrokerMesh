import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    static int number0 = 0;
    static int number1 = 1;
    static int number2 = 2;
    static int number3 = 3;

    public static void systemPrint(String inputString){
        System.out.println(inputString);
    }

    public static void handleRemoteException(){
        System.exit(number1);
    }

    public static void ifHandleLocalRegistry(int argsInput){
        try{
            LocateRegistry.createRegistry(argsInput);
        }catch (RemoteException exception){
            System.out.println(exception.getMessage());
            handleRemoteException();
        }
    }

    public static void handleGeneralException(Exception e){
        System.out.println("Error Occur! " + e.getMessage());
        System.exit(1);
    }

    public static void handleBindRegistry(String[] args, Broker obj, Network network, Registry registry){
        try{
            registry.bind(args[args.length - 1], obj);

            registry.bind(args[args.length - 1] + "N", network);

            try{
                if(args.length > number2){
                    for(int i = 1; i < args.length - 1; i++){
                        NetworkInterface obj0 = (NetworkInterface) registry.lookup(args[i]+"N");
                        network.newBroker(args[i]+"N");
                        obj0.newBroker(args[args.length - 1]+"N");
                    }
                }
            }
            catch (Exception e){
                handleGeneralException(e);
            }
            systemPrint("Broker bind to registry successfully!");
        }
        catch (Exception e){
            handleGeneralException(e);
        }
    }


    public static void main(String[] args) throws RemoteException{
        if(args.length == number2){
            ifHandleLocalRegistry(Integer.parseInt(args[0]));
        }

        Registry registry = LocateRegistry.getRegistry(Integer.parseInt(args[0]));

        LocalStore localStore = new LocalStore();

        Network network = new Network(localStore, Integer.parseInt(args[number0]));

        Broker obj = new Broker(args[args.length - 1], localStore, network, Integer.parseInt(args[0]));

        handleBindRegistry(args, obj, network, registry);
    }
}