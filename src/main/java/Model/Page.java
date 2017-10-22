package Model;

public class Page
{
    private int pageNumber;
    private SchedulingProcess schedulingProcess;
    private boolean loadedInMemory;

    private int timeLastUsed;
    private boolean useBit;


    public Page(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public Page(Page page)
    {
        this.pageNumber = page.getPageNumber();
    }


    public void resetData()
    {
        loadedInMemory = false;
        timeLastUsed = 0;
        useBit = false;
    }

    public void linkProcessToPage(SchedulingProcess schedulingProcess)
    {
        this.schedulingProcess = schedulingProcess;
    }

    public SchedulingProcess getParentProcess() {return schedulingProcess; }

    public int getPageNumber() {return pageNumber; }

    public boolean isLoadedInMemory() {return loadedInMemory;}
    public void setLoadedInMemory(boolean value) {loadedInMemory = value; }

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

    public int getTimeLastUsed() {
        return timeLastUsed;
    }

    public void setTimeLastUsed(int timeLastUsed) {
        this.timeLastUsed = timeLastUsed;
    }

    public boolean useBitIsTrue() {
        return useBit;
    }

    public void setUseBit(boolean useBit) {
        this.useBit = useBit;
    }
}
