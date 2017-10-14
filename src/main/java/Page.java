public class Page
{
    private int pageNumber;
    private static Instruction instruction = new Instruction();

    private Process process;
    private boolean loadedInMemory;
    private int timeLastUsed;


    public Page(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public void linkProcessToPage(Process process)
    {
        this.process = process;
    }

    public Process getProcess() {return process; }

    public boolean isLoadedInMemory() {return loadedInMemory;}

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
}
