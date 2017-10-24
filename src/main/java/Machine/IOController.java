package Machine;

import ObserverPattern.*;
import Model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * IOController.java is used to handle IO Requests from the CPU
 * Pages are formed into IO Requests and held in an ArrayList
 */
public class IOController implements ISubscriber, IObservable
{
    public static final int PAGE_SWAP = 6;
    private List<ISubscriber> subscriberList = new ArrayList<>();
    private List<IORequest> ioRequests = new ArrayList<>();

    /**
     * public void handlePageFault(Page page, int currentTime)
     * Forms Page into an IO request and adds to the IO Requests list
     * @param page - Page that must wait till allowed to go in Memory
     * @param currentTime - Current time method occurs
     */
    public void handlePageFault(Page page, int currentTime)
    {
        IORequest ioRequest = new IORequest(page, currentTime + PAGE_SWAP);

        //Not in IO Request List In that case, add this to the list
        if(!pageAlreadyRequested(page))
            ioRequests.add(ioRequest);
    }

    /**
     * private boolean pageAlreadyRequested(Page page)
     * @param page - Page to check if already requested
     * @return - True or false depending if page is already in the IO request list
     */
    private boolean pageAlreadyRequested(Page page)
    {
        for(IORequest ioRequest: ioRequests)
            if(ioRequest.getPage() == page)
                return true;

        return false;
    }

    /**
     * public IORequest getNextIORequest()
     * @return - Next IO Request in the List
     */
    public IORequest getNextIORequest()  { return ioRequests.get(0);  }

    /**
     * public void checkIOListForReadiedRequests
     * Looks through the list of IO Requests to determine IORequests that are ready to go into memory
     * @param cpuTime
     */
    public void checkIOListForReadiedRequests(int cpuTime)
    {
        List<IORequest> requestsMarkedForRemoval = new ArrayList<>();

        for(IORequest ioRequest: ioRequests)
        {
            if(ioRequest.getPageReadyTime() <= cpuTime)
                requestsMarkedForRemoval.add(ioRequest);
        }

        for(IORequest ioRequest: requestsMarkedForRemoval)
        {
            ioRequests.remove(ioRequest);
            ObservablePageReadyMessage message = new ObservablePageReadyMessage(ioRequest.getPage(), cpuTime);
            //Sent off to add to memory
            notifySubscribers(message);
        }

    }



    /************************IOBSERVABLE***********************************/
    /**
     * public void addSubscriber(ISubscriber subscriber)
     * Adds a new subscriber to the subscribers list.
     * Utilised for the observer pattern
     * @param subscriber - New subscriber
     */
    @Override
    public void addSubscriber(ISubscriber subscriber) {
        subscriberList.add(subscriber);
    }

    /**
     * public void removeSubScriber(ISubscriber subscriber)
     * Removes a subscriber from the subscribers list
     * @param subscriber - Existing subscriber in the the list
     */
    @Override
    public void removeSubscriber(ISubscriber subscriber) {
        subscriberList.remove(subscriber);
    }


    /**
     * public void notifySubscribers(ObservableMessage message)
     * Broadcast a message from the IOControllerClass class to all subscribers of this class.
     * Utilised for the observer pattern
     * @param message - Observable Message to be sent to all subscribers
     */
    @Override
    public void notifySubscribers(ObservableMessage message) {
        for(ISubscriber subscriber: subscriberList)
            subscriber.handleMessage(message);
    }



    /***********************ISUBSCRIBER************************/
    /**
     * public void handleMessage(ObservableMessage message)
     * Utilises the Observer Pattern
     * Handles message broadcasted by another class
     * @param message - ObservableMessage that contains relevant time information
     */
    @Override
    public void handleMessage(ObservableMessage message) {
        if(message instanceof ObservablePageFaultMessage)
        {
            Page page = ((ObservablePageFaultMessage) message).getPage();
            int currentTime = ((ObservablePageFaultMessage) message).getCurrentTime();
            handlePageFault(page, currentTime);
        }
        if(message instanceof ObservableCPUTimeMessage)
        {
            int cpuTime = ((ObservableCPUTimeMessage) message).getCpuTimeTick();
            checkIOListForReadiedRequests(cpuTime);
        }

    }


}
