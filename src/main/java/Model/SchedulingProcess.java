package Model;

import java.util.ArrayList;
import java.util.List;

public class SchedulingProcess implements Comparable<SchedulingProcess>
{
    private List<Page> pageList = new ArrayList<>();
    private List<Page> completedPageList = new ArrayList<>();
    private static final int MAX_PAGES = 50;
    private int id;

    private int startTime;
    private int finishTime;
    private int turnaroundTime;

    private List<Integer> faultTimes = new ArrayList<>();


   // private int getRemainingNumberOfPages;
    private ProcessState processState;



    public SchedulingProcess(int id, List<Page> pageList)
    {
        this.id = id;
        this.pageList = pageList;

        startTime = 0;
        finishTime = 0;
        processState = ProcessState.NEW;

        for(Page page: pageList)
            page.linkProcessToPage(this);
    }

    //Copy constructor
    public SchedulingProcess(SchedulingProcess schedulingProcess)
    {
        this.id = schedulingProcess.getId();
        this.pageList = schedulingProcess.getPageList();
        startTime = 0;
        processState = ProcessState.NEW;

        for(Page page: pageList)
            page.linkProcessToPage(this);
    }

    public int getId() { return id; }
    public List<Page> getPageList() {return pageList; }
    public boolean isNew() { return processState.equals(ProcessState.NEW); }
    public boolean isReady() { return processState.equals(ProcessState.READY); }
    public boolean isRunning() { return processState.equals(ProcessState.RUNNING); }
    public int getStartTime() {return startTime; }
    public void setStartTime(int startTime) {this.startTime = startTime;}
    public int getFinishTime(){return finishTime; }
    public void setFinishTime(int finishTime){this.finishTime = finishTime; }
    public int getTurnaroundTime(){return finishTime - startTime;}
    public int getNumberFaultTimes(){return faultTimes.size(); }
    public String getFaultTimesToString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(Integer time: faultTimes)
        {
            sb.append(time);
            sb.append(time.equals(faultTimes.get(faultTimes.size()-1)) ? "}" : ", ");
        }

        return sb.toString();

    }

    public boolean isBlocked() { return processState.equals(ProcessState.BLOCKED); }

    public void block(int currentTime)
    {
        //todo do i need to cater for blocked times or whatnot

        if (isRunning())
            processState = ProcessState.BLOCKED;
        else
            runTimeExceptionMessage(ProcessState.RUNNING);
    }

    public void unblock(int currentTime) {
        if(isBlocked())
            processState = ProcessState.READY;
        else
            runTimeExceptionMessage(ProcessState.BLOCKED);
    }

    public void admit(int currentTime)
    {
        //Precondition check to determine if process is in the NEW state
        if(isNew())
        {
            //Update state
            processState = ProcessState.READY;
            //stateTransitionMessage(Model.ProcessState.READY, currentTime);

        }
        else
            runTimeExceptionMessage(ProcessState.NEW);
    }

    public void dispatch(int currentTime)
    {
        //Precondition check to determine if the process is in the READY state
        if(isReady())
        {
            //Update state
            processState = ProcessState.RUNNING;
            //stateTransitionMessage(Model.ProcessState.RUNNING, currentTime);

        }
        else
            runTimeExceptionMessage(ProcessState.READY);
    }

    public void interrupt(int currentTime)
    {
        //Precondition check to determine if the process is in the RUNNING state
        if(isRunning())
        {
            //Update state
            processState = ProcessState.READY;
            //stateTransitionMessage(Model.ProcessState.READY, currentTime);

        }
        else
            runTimeExceptionMessage(ProcessState.RUNNING);
    }

    public void exit(int currentTime)
    {
        if(isRunning())
        {
            //Update State
            processState = ProcessState.TERMINATED;
            //stateTransitionMessage(Model.ProcessState.TERMINATED, currentTime);

            //Update finishTime
            finishTime = currentTime;
        }
    }


    private int nextPageIndex = 0;

    public Page getNextPageFromList() {return pageList.get(nextPageIndex);}
    public boolean hasReachedEndOfPageList() { return getRemainingNumberOfPages() == 0; }

    public int getRemainingNumberOfPages() {return  pageList.size() - nextPageIndex; }


    /**
     * public void run()
     */
    public void run(int currentTime)
    {
        Page nextPage = getNextPageFromList();
        nextPage.setTimeLastUsed(currentTime);
        nextPageIndex++;
        nextPage.setUseBit(true);

    }

    /**
     * private void runtimeExceptionMessage(Model.ProcessState requiredState)
     * Throws Runtime Exception Message
     * @param requiredState - State the process is meant to be in
     */
    private void runTimeExceptionMessage(ProcessState requiredState)
    {
        throw new RuntimeException("Model.SchedulingProcess is not in the " + requiredState + "state for correct transition. Actual State: " + processState);
    }

   /* public int getCurrentNumberPagesRunning()
    {
        int count = 0;
        for(Page page: pageList)
            if(page.isLoadedInMemory())
                count++;

        return count;
    }*/


    //https://www.leveluplunch.com/java/examples/count-boolean-true-values-in-arraylist/
    //NOTE: This section is using Lambdas, you will need to use Java 1.8 for this submission
    public long countDistinctProcessesRunning()
    {
        return pageList.stream().distinct().filter(p -> p.isLoadedInMemory() == true).count();
        //Calculates the distinct pages that are running in memory
    }


    public void addPageFaultTimeToList(int pageFaultTime)
    {
        faultTimes.add(pageFaultTime);
    }

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id + " state=" + processState+
                '}';
    }

    @Override
    public int compareTo(SchedulingProcess o) {
        if(this.getId() < o.getId())
            return -1;
        else if(this.getId() > o.getId())
            return 1;
        else
            return 0;
    }
}


