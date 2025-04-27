import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    static int number0 = 0;
    static int number1 = 1;
    static int number2 = 2;
    static int number3 = 3;
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        if (args.length != number3) {
            stringPrint("error: please input correct value");
            System.exit(number2 - number1);
        }

        System.out.println("\nPlease select a command: create, publish, show, delete. Replace the content inside the {} with actual values and input");
        System.out.println("1. create {topic_id} {topic_name} #create a new topic");
        System.out.println("2. publish {topic_id} {message} #publish a message to an existing topic");
        System.out.println("3. show {topic_id} #show subscriber count for current publisher");
        System.out.println("4. delete {topic_id} #delete a topic");

        BrokerInterface broker = null;
        Publisher publisher = new Publisher(args[0]);

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", Integer.parseInt(args[1]));
            broker = (BrokerInterface) registry.lookup(args[2]);

            String response = broker.newPublisher(publisher.getName());
            if (response.contains("error")) {
                System.out.println(response);
                System.exit(1);
            } else {
                System.out.println(response);
            }

            while (input.hasNext()) {
                String operation = input.nextLine();
                String[] values = operation.split(" ");

                if (Objects.equals(values[number0], "quit")) {
                    broker.deletePAll(publisher.getName());
                    System.exit(0);
                }

                else if (Objects.equals(values[0], "create")) {
                    if (values.length != number3) {
                        stringPrint("Error occurred! Please input correct value");
                        continue;
                    }
                    System.out.println(broker.createTopic(values[1], values[2], publisher.getName()));
                }

                else if (Objects.equals(values[0], "publish")) {
                    if (values.length < number3) {
                        stringPrint("Error occurred! Please input correct value");
                        continue;
                    }
                    String[] message = operation.split(" ", number3);
                    if (message[number2].length() > 100*number1) {
                        stringPrint("Error occurred! Exceed 100 characters");
                    }
                    System.out.println(broker.publishEvent(values[1], publisher.getName(), message[number2]));
                }

                else if (Objects.equals(values[0], "show")) {
                    if (values.length == 1) {
                        ArrayList<String> results = broker.showCount(publisher.getName());
                        if (results.isEmpty()) {
                            stringPrint("No topics published currently");
                        }
                        for (String result : results) {
                            String[] messages = result.split(" ");
                            if (Objects.equals(messages[0], "error")) {
                                System.out.println(result);
                            } else {
                                System.out.println("success: " + messages[messages.length - 1]);
                            }
                        }
                        continue;
                    }
                    if (values.length != number2) {
                        stringPrint("Error occurred! Please input correct value");
                        continue;
                    }
                    String result = broker.showCount(values[1], publisher.getName());
                    if (result.contains("error")) {
                        System.out.println(result);
                    } else {
                        String[] messages = result.split(" ");
                        System.out.println("success: " + values[1] + " " + messages[number2] + " " + messages[1]);
                    }
                }

                else if (Objects.equals(values[0], "delete")) {
                    if (values.length != number2) {
                        stringPrint("Error occurred! Please input correct value");
                        continue;
                    }
                    System.out.println(broker.deleteTopic(values[1], publisher.getName()));
                } else {
                    stringPrint("Error occurred! Invalid input");
                }
            }
        } catch (NotBoundException e) {
            System.out.println("Error occurred! No existing broker found with the input, please choose another one");
            System.exit(1);
        } catch (RemoteException e) {
            stringPrint("Error occurred! No active registry or system crash, please try again");
            safelyRemovePublisher(broker, publisher);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            safelyRemovePublisher(broker, publisher);
        }
    }
    public static void stringPrint(String inputString){
        System.out.println(inputString);
    }

    private static void safelyRemovePublisher(BrokerInterface broker, Publisher publisher) {
        try {
            if (broker != null) {
                broker.deletePAll(publisher.getName());
            }
        } catch (RemoteException ignored) {
        }
        System.exit(number1);
    }
}
