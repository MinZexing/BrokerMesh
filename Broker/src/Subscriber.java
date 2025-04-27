import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Subscriber {
    String name;

    SubscriberInterface obj;

    private ArrayList<Topic> subscribeTopics = new ArrayList<Topic>();
    private ArrayList<String[]> subscribeTopicsFormat = new ArrayList<String[]>();
    static int number0 = 0;

    public Subscriber(String name, Registry registryS) throws RemoteException{
        this.name = name;
        try{
            this.obj = (SubscriberInterface) registryS.lookup(name);
            System.out.println(obj);
        }catch (Exception e){
            System.out.println("Error occurred! " + e.getMessage());
        }
    }

    public synchronized String subscribeNewTopic(Topic topic){
        for(Topic current: subscribeTopics){
            if(Objects.equals(current.id, topic.id)){
                return returnString("Error occurred! This topic has already been subscribed");
            }
        }
        subscribeTopics.add(topic);
        String[] topicFo = new String[]{topic.id, topic.name, topic.pName};
        handleSubscribeNewTopicsFormat(topicFo);
        return returnString("Subscribed the new topic successfully!");
    }
    public String returnString(String stringInput){
        return stringInput;
    }
    public void handleSubscribeNewTopicsFormat(String[] topicFo){
        subscribeTopicsFormat.add(topicFo);
    }


    public synchronized void subscribeUpdateTopic(String id){
        int subscribeTopicsSize = subscribeTopics.size();
        for (int i = number0; i < subscribeTopicsSize; i++){
            if(Objects.equals(subscribeTopics.get(i).id, id)){
                handleSubscribeUpdateTopic("topic", i);
                handleSubscribeUpdateTopic("topicFormat",i);
                break;
            }
        }
    }

    public synchronized void subscribeRemoveAll(int index){
        handleSubscribeUpdateTopic("topic",index);
        handleSubscribeUpdateTopic("topicFormat",index);
    }

    public void handleSubscribeUpdateTopic(String type, int i){
        if(type.equals("topic")) {
            subscribeTopics.remove(i);
        }else{
            subscribeTopicsFormat.remove(i);
        }
    }

    public synchronized ArrayList<String[]> getSubscribeTopicsFormat(){return subscribeTopicsFormat;}

    public synchronized ArrayList<Topic> getSubscribeTopics(){return subscribeTopics;}

}
