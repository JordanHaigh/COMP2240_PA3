package ObserverPattern;

import Model.Page;

/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * ObserverPattern.ObservablePageReadyMessage.java extends from the abstract ObserverPattern.ObservableMessage class
 * This class monitors when a page is ready to be added to memory
 * This is later fed to the Memory class, utilising the observer pattern
 */
public class ObservablePageReadyMessage extends ObservableMessage
{
    private Page page;
    private int currentTime;

    public ObservablePageReadyMessage(Page page, int currentTime)
    {
        this.page = page;
        this.currentTime = currentTime;
    }

    /**
     * public Page getPage()
     * @return - Page that is ready
     */
    public Page getPage(){return page; }

    /**
     * public int getCurrentTime()
     * @return - Current time page is ready
     */
    public int getCurrentTime() {return currentTime; }

}
