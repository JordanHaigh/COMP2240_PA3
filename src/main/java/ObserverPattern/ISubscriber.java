package ObserverPattern;

/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * ObserverPattern.ISubscriber.java implements the Observer Pattern Interface for subscribers
 */
public interface ISubscriber
{
    void handleMessage(ObservableMessage message); //Determines the type of the message and associates variables where necessary
}