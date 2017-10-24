package Model;

/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * IORequest.java holds a Page and the time it will be ready at
 */
public class IORequest
{
    private Page page;
    private int pageReadyTime;

    /**
     * public IORequest(Page page, int pageReadyTime)
     * @param page - Page held inside IORequest
     * @param pageReadyTime - Time page till be ready at
     */
    public IORequest(Page page, int pageReadyTime)
    {
        this.page = page;
        this.pageReadyTime = pageReadyTime;
    }

    /**
     * public Page getPage()
     * @return - Page held inside IORequest
     */
    public Page getPage() { return page; }

    /**
     * public int getPageReadyTime()
     * @return - Time page will be ready at
     */
    public int getPageReadyTime() { return pageReadyTime; }
}