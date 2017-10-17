public class Page
{
    private int pageNumber;
    private static Instruction instruction = new Instruction();

    private Process process;
    private boolean loadedInMemory;

    private int timeLastUsed;
    private boolean useBit;

    private int startTime;
    private int finishTime;



    public Page(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public void linkProcessToPage(Process process)
    {
        this.process = process;
    }

    public Process getParentProcess() {return process; }

    public int getPageNumber() {return pageNumber; }

    public boolean isLoadedInMemory() {return loadedInMemory;}
    public void setLoadedInMemory(boolean value) {loadedInMemory = value; }

    public int getStartTime() {return startTime; }
    public void setStartTime(int startTime) {this.startTime = startTime;}
    public int getFinishTime(){return finishTime; }
    public void setFinishTime(int finishTime){this.finishTime = finishTime; }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("P")
                .append(process.getId())
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
