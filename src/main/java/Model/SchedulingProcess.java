package Model;

import java.util.ArrayList;
import java.util.List;
/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * SchedulingProcess.java contains all Process information, including a page list for each process
 */
public class SchedulingProcess implements Comparable<SchedulingProcess>
{
    private List<Page> pageList = new ArrayList<>();
    private List<Page> completedPageList = new ArrayList<>();
    private static final int MAX_PAGES = 50;
    private int id;

    private int startTime;
    private int finishTime;

    private List<Integer> faultTimes = new ArrayList<>();


   // private int getRemainingNumberOfPages;
    private ProcessState processState;

    private int nextPageIndex = 0;

    private int clockIndex = 0;
    public void incrementClockIndex(){clockIndex ++;}
    public void resetClockIndex(){clockIndex = 0;}
    public int getClockIndex(){return clockIndex; }


    /**
     * public SchedulingProcess(int id, List<Page> pageList)
     * Overloaded Constructor
     * @param id - ID of Process
     * @param pageList - List of Pages for Process
     */
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

    /**
     * public SchedulingProcess(SchedulingProcess schedulingProcess)
     * Copy Constructor
     * @param schedulingProcess - Other SchedulingProcess
     */
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

    /**
     * public int getID()
     * @return  Process ID
     */
    public int getId() { return id; }

    /**
     * public List<Page> getPageList()
     * @return - Page List
     */
    public List<Page> getPageList() {return pageList; }

    /**
     * public boolean isNew()
     * @return - True or false if in the NEW State
     */
    public boolean isNew() { return processState.equals(ProcessState.NEW); }

    /**
     * public boolean isReady()
     * @return - True or false if in the READY state
     */
    public boolean isReady() { return processState.equals(ProcessState.READY); }

    /**
     * public boolean isRunning()
     * @return - True or false if in the RUNNING state
     */
    public boolean isRunning() { return processState.equals(ProcessState.RUNNING); }

    /**
     * public boolean isBlocked()
     * @return - True or False if in the BLOCKED state
     */
    public boolean isBlocked() { return processState.equals(ProcessState.BLOCKED); }


    /**
     * public int getTurnaroundTime()
     * @return - Turnaround time of Process
     */
    public int getTurnaroundTime(){return finishTime - startTime;}

    /**
     * public page getNextPageFromList()
     * @return - Next page in page list
     */
    public Page getNextPageFromList() {return pageList.get(nextPageIndex);}

    /**
     * public boolean hasReachedEndOfPageList()
     * @return - True or false if there are no more pages in the page list
     */
    public boolean hasReachedEndOfPageList() { return getRemainingNumberOfPages() == 0; }

    /**
     * public int getRemainingNumberOfPages()
     * @return - Number of pages remaining in the page list
     */
    public int getRemainingNumberOfPages() {return  pageList.size() - nextPageIndex; }

    /**
     * public int getNumberFaultTimes()
     * @return - Size of the fault Times list
     */
    public int getNumberFaultTimes(){return faultTimes.size(); }

    /**
     * public String getFaultTimesToString()
     * @return - Fault times in string format
     */
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


    /**
     * public void block(int currentTime)
     * Updates process state to BLOCKED if in the RUNNING state
     * @param currentTime
     */
    public void block(int currentTime)
    {
        if (isRunning())
            processState = ProcessState.BLOCKED;
        else
            runTimeExceptionMessage(ProcessState.RUNNING);
    }

    /**
     * public void unblock(int currentTime)
     * Updates the Process state to READY if in the BLOCKED State
     * @param currentTime
     */
    public void unblock(int currentTime) {
        if(isBlocked())
            processState = ProcessState.READY;
        else
            runTimeExceptionMessage(ProcessState.BLOCKED);
    }

    /**
     * public void admit
     * Updates the process state to READY if it is NEW
     */
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

    /**
     * public void dispatch
     * Updates the process state to RUNNING if it is READY
     */
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

    /**
     * public void interrupt()
     * Updates the process state to READY if it is RUNNING
     */
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

    /**
     * public void exit(int currentTime)
     * Updates the process state to TERMINATED if it is RUNNING
     * Updates the finish time to the current time and calculates other statistics
     * @param currentTime - Current time on the cpu
     */
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

    /**
     * public void run()
     * Updates the next page index and sets the use bit to true
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


    /**
     * public long countDistinctProcessesRunning()
     * @return - Distinct pages that are running in memory
     */

    public long countDistinctProcessesRunning()
    {
        //https://www.leveluplunch.com/java/examples/count-boolean-true-values-in-arraylist/
        //NOTE: This section is using Lambdas, you will need to use Java 1.8 for this submission
        return pageList.stream().distinct().filter(p -> p.isLoadedInMemory() == true).count();
        //Calculates the distinct pages that are running in memory
    }


    /**
     * public void addPageFaultTimeToList(int pageFaultTime)
     * @param pageFaultTime - Fault time to be added to list
     */
    public void addPageFaultTimeToList(int pageFaultTime)
    {
        faultTimes.add(pageFaultTime);
    }

    /**
     * public String toString()
     * @return - Process information to string
     */
    @Override
    public String toString() {
        return "Process{" +
                "id=" + id + " state=" + processState+
                '}';
    }

    /**
     * public int compareTo(SchedulingProcess o)
     * @param o - Second Scheduling process to compare to
     * @return - Int comparison determining position
     */
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


