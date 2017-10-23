package Model;

public class IORequest
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