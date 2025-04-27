import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;

public class Broker extends UnicastRemoteObject implements BrokerInterface {

    public String name;
    private LocalStore localStore;
    private Network network;

    Registry registry;

    static int number0 = 0;
    static int number1 = 1;
    static int number2 = 2;
    static int number5 = 5;
    static boolean falseValue = false;
    static boolean trueValue = true;


    public Broker(String name, LocalStore localStore, Network network, int registryPort) throws RemoteException{
        super();
        this.name = name;
        this.localStore = localStore;
        this.network = network;
        this.registry = LocateRegistry.getRegistry(registryPort);
    }

    public void systemPrint(String stringInput){
        System.out.println(stringInput);
    }

    @Override
    public String createTopic(String id, String name, String pName) throws RemoteException{
        for (NetworkInterface broker: network.otherBrokers){
            try{
                int result = broker.checkUniqueTopic(id);
                if(result == -1){
                    return createTopicReturn("Error occurred! The topic is already exist");
                }
            }
            catch(RemoteException remoteException){
                return createTopicReturn("Error occurred! Add failed");
            }
        }
        return localStore.create(id, name, pName);
    }
    public String createTopicReturn(String stringInput){
        return stringInput;
    }


    @Override
    public String publishEvent(String tId, String pName, String message) throws RemoteException{

        String result = localStore.checkAccess(tId, pName);

        if(result.contains("error")){
            return result;
        }
        return handlePublishEvent(tId, message);
    }
    public String handlePublishEvent(String tId, String message){
        try{
            localStore.publish(tId, message);
            systemPrint("local success");

            for(NetworkInterface broker: network.otherBrokers){
                broker.notifyMessageGlobal(tId, message);
            }
            return "success";
        }
        catch(RemoteException exception){
            return "Error occurred! Publish process failed";
        }
    }


    @Override
    public String showCount(String tId, String pName) throws RemoteException{

        try{
            String result = handleShowCount(tId, pName);

            if(result.contains("error")){
                return result;
            }

            String[] results = result.split(":");
            int value = handleShowCountValue(tId);

            for(NetworkInterface broker: network.otherBrokers){
                int others = broker.showCountGlobal(tId);
                value = value + others;
            }

            return "success " + value + " " + results[number1];
        }
        catch(RemoteException remoteException){
            return handleShowCountException();
        }
    }
    public String handleShowCount(String tId, String pName){
        return localStore.checkAccess(tId, pName);
    }
    public int handleShowCountValue(String tId){
        return localStore.count(tId);
    }
    public String handleShowCountException(){
        return "Error occurred! Get count process failed";
    }

    @Override
    public ArrayList<String> showCount(String pName) throws RemoteException{
        ArrayList<Topic> topics = handleShowCountTopics();
        ArrayList<String> results = new ArrayList<>();
        for(Topic topic: topics){
            if(Objects.equals(topic.pName, pName)){
                String result = showCount(topic.id, pName);
                if(result.contains("success")){
                    result = result + " " + topic.id;
                }else{
                    result = result + " for topic " + topic.id;
                }
                results.add(result);
            }
        }
        return results;
    }
    public ArrayList<Topic> handleShowCountTopics(){
        return localStore.topics;
    }


    @Override
    public String deleteTopic(String tId, String pName) throws RemoteException{

        try{
            String result = handleDeleteTopicResult(tId, pName);

            if(result.contains("error")){
                return result;
            }

            handleDeleteTopicRemove(tId);

            for(NetworkInterface broker : network.otherBrokers){
                broker.removeSubGlobal(tId);
            }

            handleDeleteTopicDelete(tId);

            return "success";
        }
        catch(RemoteException remoteException){
            return handleDeleteTopicError();
        }
    }
    public String handleDeleteTopicResult(String tId, String pName){
        return localStore.checkAccess(tId, pName);
    }
    public void handleDeleteTopicRemove(String tId){
        localStore.removeSubscriber(tId);
    }
    public void handleDeleteTopicDelete(String tId){
        localStore.delete(tId);
    }
    public String handleDeleteTopicError(){
        return "Error Occurred! Delete process failed";
    }



    @Override
    public String deletePAll(String pName) throws RemoteException{

        try{
            handleDeletePAllRemoveAll(pName);

            handleDeletePAllDeleteAll(pName);

            for(NetworkInterface broker : network.otherBrokers){
                broker.removeAllSubGlobal(pName);
            }

           handleDeletePAllPubRemove(pName);

            return "success";
        }
        catch(RemoteException remoteException){
            return handleDeletePAllError();
        }
    }
    public void handleDeletePAllRemoveAll(String pName){
        localStore.removeAll(pName);
    }
    public void handleDeletePAllDeleteAll(String pName){
        localStore.deleteAll(pName);
    }
    public void handleDeletePAllPubRemove(String pName){
        localStore.publishers.remove(pName);
    }
    public String handleDeletePAllError(){
        return "Error occurred! Delete process failed";
    }


    @Override
    public String newPublisher(String pName) throws RemoteException{
        int numberP = handleNewPublisherNumber();
        String result = handleNewPublisherResult(pName);

        if(result.contains("error")){
            return result;
        }

        for(NetworkInterface broker : network.otherBrokers){
            numberP = numberP + broker.checkNumPublisher();
            String oResult = broker.checkPublisher(pName);
            if(oResult.contains("error")){
                return oResult;
            }
        }

        if(numberP < number5){
            handleNewPublisherAddName(pName);
            return "Success! New publisher have joined the system successfully";
        }

        return "Error occurred! Max number achieved";
    }
    public int handleNewPublisherNumber(){
        return localStore.checkNumberOfPublisher();
    }
    public String handleNewPublisherResult(String pName){
        return localStore.checkexistPublisher(pName);
    }
    public void handleNewPublisherAddName(String pName){
        localStore.publishers.add(pName);
    }


    //Below are methods for subscriber
    public String returnString(String stringInput){
        return stringInput;
    }

    @Override
    public ArrayList<String[]> listTopics(){
        try{
            ArrayList<String[]> topicsList = new ArrayList<>();
            topicsList.addAll(localStore.getAllTopic());
            for(NetworkInterface broker : network.otherBrokers){
                topicsList.addAll(broker.listTopicsGlobal());
            }
            return topicsList;
        }
        catch(RemoteException remoteException){
            return new ArrayList<>();
        }
    }

    public String subscribe(String tId, String sName) throws RemoteException{

        boolean exist = falseValue;
        Topic sTopic = null;

        ArrayList<String[]> topics = listTopics();
        for(String[] topicList: topics){
            if(Objects.equals(topicList[number0], tId)){
                exist = trueValue;
                sTopic = new Topic(topicList[number0], topicList[number1], topicList[number2]);
            }
        }

        if(!exist){
            return returnString("Error occurred! There is no topic exist");
        }

        for(int i = number0; i < localStore.subscribers.size(); i++){
            if(Objects.equals(localStore.subscribers.get(i).name, sName)){
                return localStore.subscribers.get(i).subscribeNewTopic(sTopic);
            }
        }
        return returnString("Error occurred! Subscribe process failed");
    }

    @Override
    public ArrayList<String[]> showCurrent(String subscriberName) throws RemoteException{

        for(Subscriber subscriber: localStore.subscribers){
            if(Objects.equals(subscriber.name, subscriberName)){
                return subscriber.getSubscribeTopicsFormat();
            }
        }
        return null;
    }

    @Override
    public String unsubscribe(String tId, String subscriberName) throws RemoteException{
        boolean exist = falseValue;

        ArrayList<String[]> topics = listTopics();
        for(String[] topicList : topics){
            if(Objects.equals(topicList[number0], tId)){
                exist = trueValue;
            }
        }

        if(!exist){
            return returnString("Error Occurred! There is no topic exist");
        }

        for(Subscriber subscriber: localStore.subscribers){
            if(Objects.equals(subscriber.name, subscriberName)){
                for(int i = number0; i < subscriber.getSubscribeTopics().size(); i++){
                    if(Objects.equals(subscriber.getSubscribeTopics().get(i).id, tId)){
                        handleUnsubscribe(subscriber, tId);
                        return returnString("Success");
                    }
                }
            }
        }
        return returnString("Error Occurred! You did not subscribe this topic");
    }
    public void handleUnsubscribe(Subscriber subscriber, String tId){
        subscriber.subscribeUpdateTopic(tId);
    }


    @Override
    public String deleteSAll(String sName) throws RemoteException{
        int subscriberSize = localStore.subscribers.size();
        for(int i = number0; i < subscriberSize; i++){
            if(Objects.equals(localStore.subscribers.get(i).name, sName)){
                localStore.subscribers.remove(i);
                return "Success";
            }
        }
        return returnString("Error occurred! Delete process failed");
    }

    @Override
    public String newSubscriber(String sName) throws RemoteException{
        int numberS = handleNewSubscriberNumber();

        for(NetworkInterface broker : network.otherBrokers){
            numberS = numberS + broker.checkNumSubscriber();
        }

        if(numberS < number2*number5){
            Subscriber subscriber = new Subscriber(sName, registry);
            handleNewSubscriberAdd(subscriber);

            return returnString("Success: success join the system");
        }

        return returnString("Error occurred! Max number achieved");
    }
    public int handleNewSubscriberNumber(){
        return localStore.checkNumberofSubscriber();
    }
    public void handleNewSubscriberAdd(Subscriber subscriber){
        localStore.subscribers.add(subscriber);
    }

}
