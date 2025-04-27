import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Objects;

public class LocalStore {

    ArrayList<Topic> topics = new ArrayList<>();
    ArrayList<String[]> topicsFormat = new ArrayList<>();

    ArrayList<Subscriber> subscribers = new ArrayList<>();

    ArrayList<String> publishers = new ArrayList<>();
    static int number0 = 0;
    static int number1 = 1;

    public synchronized String create(String id, String name, String pName){
        for (Topic topic : topics){
            if(Objects.equals(topic.id, id)){
                return "Error occurred! The topic is already exist";
            }
        }
        Topic topic = new Topic(id, name, pName);
        topics.add(topic);

        String[] topicList = new String[]{id, name, pName};
        topicsFormat.add(topicList);

        return "success";
    }

    public synchronized void publish(String id, String message) {
        int subscribersSize = subscribers.size();
        try{
            int i = number0;
            while(i < subscribersSize){
                for(Topic list: subscribers.get(i).getSubscribeTopics()){
                    if(Objects.equals(list.id, id)){
                        systemPrint("local");
                        if(subscribers.get(i).obj.receiveMessage(list.id, list.name, message).contains("error")){
                            subscribers.remove(i);
                            i--;
                        }
                    }
                }
                i++;
            }
        }
        catch (RemoteException ignored){
        }
    }
    public void systemPrint(String inputString){
        System.out.println(inputString);
    }


    public String checkAccess(String tId, String pName){
        for(Topic topic: topics){
            if(Objects.equals(topic.id, tId)){
                if(Objects.equals(topic.pName, pName)){
                    return "success: " + topic.name;
                }
                else{
                    return returnString("Error occurred! Do not have access to this topic");
                }
            }
        }
        return returnString("Error occurred! There is no topic exist");
    }
    public String returnString(String stringInput){
        return stringInput;
    }


    public synchronized int count(String tId){
        int countS = number0;

        for(Subscriber subscriber: subscribers){
            for(Topic list: subscriber.getSubscribeTopics()){
                if(Objects.equals(list.id, tId)){
                    countS = countS + number1;
                }
            }
        }
        return countS;
    }


    public synchronized void removeSubscriber(String tId){
        try{
            int i = number0;
            while(i < subscribers.size()){
                ArrayList<Topic> topicsList = subscribers.get(i).getSubscribeTopics();

                int j = number0;
                while(j < topicsList.size()){
                    Topic list = topicsList.get(j);

                    if(Objects.equals(list.id, tId)){
                        handleRemoveAllSubscriber(i, j);

                        if(subscribers.get(i).obj.receiveMessage(list.id, list.name, "The publisher closed this topic").contains("error")){
                            handleRemoveSubscriber(i);
                            i--;
                        }
                    }
                    else{
                        j++;
                    }
                }
                i++;
            }
        }
        catch(RemoteException e){
            System.out.println("Error occurred!" + e.getMessage());
        }
    }

    public void handleRemoveSubscriber(int i){
        subscribers.remove(i);
    }
    public void handleRemoveAllSubscriber(int i, int j){
        subscribers.get(i).subscribeRemoveAll(j);
    }



    public synchronized void delete(String id){
        for(int i = number0; i < topics.size(); i++){
            if(Objects.equals(topics.get(i).id, id)){
                handleDeleteRemove(i);
                handleDeleteFormatRemove(i);
                break;
            }
        }
    }
    public void handleDeleteRemove(int i){
        topics.remove(i);
    }
    public void handleDeleteFormatRemove(int i){
        topicsFormat.remove(i);
    }

    //
    public synchronized void removeAll(String pName){
        try{
            int i = number0;
            while(subscribers.size() != number0 && i < subscribers.size()){
                ArrayList<Topic> topicsList = handleSubscriberTopicsList(i);

                int j = number0;
                while(topicsList.size() != number0 && j < topicsList.size()){
                    Topic list = topicsList.get(j);

                    if(Objects.equals(list.pName, pName)){
                        subscribers.get(i).subscribeRemoveAll(j);
                        if(subscribers.get(i).obj.receiveMessage(list.id, list.name, "the publisher closed this topic").contains("error")){
                            handleSubscriberRemove(i);
                            i--;
                        }
                    }
                    else{
                        j++;
                    }
                }
                i++;
            }
        }
        catch (RemoteException e){
            System.out.println("Error occurred! " + e.getMessage());
        }
        catch (Exception exception){
            System.out.println("Error occurred: " + exception.getMessage());
        }
    }
    public void handleSubscriberRemove(int i){
        subscribers.remove(i);
    }
    public ArrayList<Topic> handleSubscriberTopicsList(int i){
        return subscribers.get(i).getSubscribeTopics();
    }

    public synchronized void deleteAll(String pName){
        int i = number0;
        while (i < topics.size()){
            if(Objects.equals(topics.get(i).pName, pName)){
                topics.remove(i);
                topicsFormat.remove(i);
            }
            else{
                i++;
            }
        }
    }

    public synchronized ArrayList<String[]> getAllTopic(){ return topicsFormat; }

    public synchronized int checkNumberOfPublisher(){
        int publishersSize = publishers.size();
        return publishersSize;
    }

    public synchronized String checkexistPublisher(String pName){
        for(String publisher: publishers){
            if(Objects.equals(publisher, pName)){
                return "Error occurred! Duplicated name";
            }
        }
        return "Success: New publisher have joined the system successfully";
    }

    public synchronized int checkNumberofSubscriber(){
        int subscribersSize = subscribers.size();
        return subscribersSize;
    }


}
