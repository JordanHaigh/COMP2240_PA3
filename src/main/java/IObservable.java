/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * IObservable.java implements the Observer Pattern Interface
 */
public interface IObservable
{
    void addSubscriber(ISubscriber subscriber); //Add Subscriber parameter to a subscriber list
    void removeSubscriber(ISubscriber subscriber); //Remove existing Subscriber parameter from subscriber list
    void notifySubscribers(ObservableMessage message); //Notify all subscribers of message parameter

}