package ObserverPattern;

import Model.Page;

public class ObservablePageFaultMessage extends ObservableMessage
{
    private Page page;
    private int currentTime;

    public ObservablePageFaultMessage(Page page, int currentTime)
    {
        this.page = page;
        this.currentTime = currentTime;
    }

    public Page getPage() {return page; }
    public int getCurrentTime() { return currentTime; }
}
