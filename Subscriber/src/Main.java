import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    static int number0 = 0;
    static int number1 = 1;
    static int number2 = 2;
    static int number3 = 3;
    public static void handleNotBoundException(){
        System.out.println("Error occur! There is no exist broker based on you input, please choose another one");
        System.exit(1);
    }

    public static void handleAlreadyBoundException(){
        System.out.println("Error Occur! This name has already been used, please choose another one");
        System.exit(1);
    }

    public static void handleRemoteException(){
        System.out.println("error: there is no active registry or the system is crashed, please try again");
    }

    public static void systemOut(){
        System.exit(1);
    }

    public static void systemPrint(String inputString){
        System.out.println(inputString);
    }

    public static void main(String[] args) {

        if(args.length != number3){
            System.out.println("Error Occur! please input correct value");
            systemOut();
        }

        System.out.println("Please select command: list, sub, current, unsub. Replace the content inside the {} to real value, and ignore rest");
        systemPrint("1. list {all} #list all topics");
        System.out.println("2. sub {topic_id} #subscribe to a topic");
        systemPrint("3. current # show the current subscriptions of the subscriber");
        System.out.println("4. unsub {topic_id} #unsubscribe from a topic");
        systemPrint("or type quit to quit the system");

        Scanner input = new Scanner(System.in);
        BrokerInterface obj = null;

        try{

            Subscriber subscriber = new Subscriber(args[number0]);

            Registry registry = LocateRegistry.getRegistry(Integer.parseInt(args[1]));

            registry.bind(args[0], subscriber);

            obj = (BrokerInterface) registry.lookup(args[number2]);
            String check = obj.newSubscriber(args[number0]);

            if(check.contains("error")){
                System.out.println(check);
                systemOut();
            }
            else{
                System.out.println(check);
            }

            while(true){
                String operation = input.nextLine();

                String[] values = operation.split(" ");

                switch (values[0]){

                    case "quit" -> {
                        registry.unbind(subscriber.getName());
                        System.exit(number0);
                    }

                    case "list" -> {
                        ArrayList<String[]> allTopics = obj.listTopics();

                        if(allTopics == null || allTopics.size() == 0){
                            systemPrint("Error Occur! There is no topic exist right now");
                            continue;
                        }

                        for(String[] list: allTopics){
                            System.out.println(list[0] + " " + list[number1] + " " + list[number2]);
                        }
                    }

                    case "sub" -> {
                        if(values.length != number2){
                            systemPrint("Error Occur! Please input right value");
                            continue;
                        }
                        System.out.println(obj.subscribe(values[1], subscriber.getName()));
                    }

                    case "current" -> {
                        ArrayList<String[]> currentLists = obj.showCurrent(subscriber.getName());

                        if(currentLists == null || currentLists.size() == 0) {
                            systemPrint("You have not subscribe any topic yet");
                            continue;
                        }

                        for(String[] list : currentLists){
                            System.out.println(list[number0] + " " + list[1] + " " + list[number2]);
                        }
                    }

                    case "unsub" -> {
                        if(values.length != 2){
                            systemPrint("Error Occur! Please input right value");
                            continue;
                        }
                        String dsucc = obj.unsubscribe(values[1], subscriber.getName());
                        System.out.println(dsucc);
                    }
                    default -> {
                        systemPrint("Error Occur! Incorrect input");
                    }
                }
            }
        }
        catch (AlreadyBoundException alreadyBoundException){
            handleAlreadyBoundException();
        }
        catch (NotBoundException notBoundException){
            handleNotBoundException();
        }
        catch (RemoteException remoteException){
            handleRemoteException();
            try{
                assert obj != null;
                String res = obj.deleteSAll(args[number0]);
            }
            catch(RemoteException ignored){
            }
            systemOut();
        }

        catch (Exception exception){
            systemPrint("Error occur! something wrong, please try again");
            try{
                assert obj != null;
                String res = obj.deleteSAll(args[0]);
            }
            catch (RemoteException ignored){
            }
            systemOut();
        }
    }
}