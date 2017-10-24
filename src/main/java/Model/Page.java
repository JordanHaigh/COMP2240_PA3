package Model;


/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * Page.java holds Page information including what process it belongs to and relevant information used in the page
 * replacement algorithms
 */
public class Page
{
    private int pageNumber;
    private SchedulingProcess schedulingProcess;
    private boolean loadedInMemory;

    private int timeLastUsed;
    private boolean useBit;


    /**
     * public Page(int pageNumber)
     * Overloaded constructor
     * @param pageNumber - Value of the Page
     */
    public Page(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    /**
     * public Page(Page page)
     * Copy Constructor
     * @param page - Value of the page
     */
    public Page(Page page)
    {
        this.pageNumber = page.getPageNumber();
    }


    /**
     * public void resetData()
     * Resets all data in the page
     * Time last used to zero, any booleans all false
     */
    public void resetData()
    {
        loadedInMemory = false;
        timeLastUsed = 0;
        useBit = false;
    }

    /**
     * public void linkProcessToPage(SchedulingProcess schedulingProcess)
     * Links the current page to a parent process
     * @param schedulingProcess - Parent process
     */
    public void linkProcessToPage(SchedulingProcess schedulingProcess)
    {
        this.schedulingProcess = schedulingProcess;
    }

    /**
     * public SchedulingProcess getParentProcess()
     * @return - Parent process of page
     */
    public SchedulingProcess getParentProcess() {return schedulingProcess; }

    /**
     * public int getPageNumber()
     * @return - Page number
     */
    public int getPageNumber() {return pageNumber; }

    /**
     * public boolean isLoadedInMemory()
     * @return - True or false depending if page is loade din memory
     */
    public boolean isLoadedInMemory() {return loadedInMemory;}

    /**
     * public void setLoadedInMemory(boolean value)
     * @param value - True or false to set the loadedInMemory value
     */
    public void setLoadedInMemory(boolean value) {loadedInMemory = value; }

    /**
     * public String toString()
     * @return - Page variables to String
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("P")
                .append(schedulingProcess.getId())
                .append("(")
                .append(pageNumber)
                .append(")");

        return sb.toString();
    }

    /**
     * public int getLastTimeUsed()
     * @return - Time last used
     */
    public int getTimeLastUsed() {
        return timeLastUsed;
    }

    /**
     * public void setTimeUsed(int timeLastUsed)
     * @param timeLastUsed - Time last used
     */
    public void setTimeLastUsed(int timeLastUsed) {
        this.timeLastUsed = timeLastUsed;
    }

    /**
     * public boolean useBitIsTrue()
     * @return - True or false depending if the use bit is true
     */
    public boolean useBitIsTrue() {
        return useBit;
    }

    /**
     * public void setUseBit(boolean useBit)
     * Sets use bit to parameter
     * @param useBit - True or false value
     */
    public void setUseBit(boolean useBit) {
        this.useBit = useBit;
    }
}
