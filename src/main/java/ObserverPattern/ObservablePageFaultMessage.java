package ObserverPattern;

import Model.Page;

/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * ObserverPattern.ObservablePageFaultMessage.java extends from the abstract ObserverPattern.ObservableMessage class
 * This class monitors when a page fault occurs
 * This is later fed to the IO Controller class, utilising the observer pattern
 */
public class ObservablePageFaultMessage extends ObservableMessage
{
    private Page page;
    private int currentTime;

    public ObservablePageFaultMessage(Page page, int currentTime)
    {
        this.page = page;
        this.currentTime = currentTime;
    }

    /**
     * public Page getPage()
     * @return - Page in the Page Fault
     */
    public Page getPage() {return page; }

    /**
     * public int getCurrentTime()
     * @return - Current Time in the Page Fault
     */
    public int getCurrentTime() { return currentTime; }
}
