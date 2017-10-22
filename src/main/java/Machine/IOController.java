package Machine;

import ObserverPattern.*;
import Model.*;

import java.util.ArrayList;
import java.util.List;

public class IOController implements ISubscriber, IObservable
{
    public static final int PAGE_SWAP = 6;
    private List<ISubscriber> subscriberList = new ArrayList<>();
    private List<IORequest> ioRequests = new ArrayList<>();

    public void handlePageFault(Page page, int currentTime)
    {
        IORequest ioRequest = new IORequest(page, currentTime + PAGE_SWAP);

        //Not in IO Request List In that case, add this to the list
        if(!pageAlreadyRequested(page))
            ioRequests.add(ioRequest);
    }

    private boolean pageAlreadyRequested(Page page)
    {
        for(IORequest ioRequest: ioRequests)
            if(ioRequest.getPage() == page)
                return true;

        return false;
    }

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
            notifySubscribers(message);
        }

    }



    /************************IOBSERVABLE***********************************/
    @Override
    public void addSubscriber(ISubscriber subscriber) {
        subscriberList.add(subscriber);
    }

    @Override
    public void removeSubscriber(ISubscriber subscriber) {
        subscriberList.remove(subscriber);
    }

    @Override
    public void notifySubscribers(ObservableMessage message) {
        for(ISubscriber subscriber: subscriberList)
            subscriber.handleMessage(message);
    }



    /***********************ISUBSCRIBER************************/
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

    private class IORequest
    {
        private Page page;
        private int pageReadyTime;

        public IORequest(Page page, int pageReadyTime)
        {
            this.page = page;
            this.pageReadyTime = pageReadyTime;
        }

        public Page getPage() { return page; }

        public int getPageReadyTime() { return pageReadyTime; }
    }
}
