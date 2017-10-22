public class ObservablePageReadyMessage extends ObservableMessage
{
    private Page page;
    private int currentTime;

    public ObservablePageReadyMessage(Page page, int currentTime)
    {
        this.page = page;
        this.currentTime = currentTime;
    }

    public Page getPage(){return page; }
    public int getCurrentTime() {return currentTime; }

}
