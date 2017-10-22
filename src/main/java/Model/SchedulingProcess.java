package Model;

import java.util.ArrayList;
import java.util.List;

public class SchedulingProcess
{
    private List<Page> pageList = new ArrayList<>();
    private List<Page> completedPageList = new ArrayList<>();
    private static final int MAX_PAGES = 50;
    private int id;

    private int startTime;
    private int finishTime;

   // private int getRemainingNumberOfPages;
    private ProcessState processState;



    public SchedulingProcess(int id, List<Page> pageList)
    {
        this.id = id;
        this.pageList = pageList;

        startTime = 0;
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

    public boolean isBlocked() { return processState.equals(ProcessState.BLOCKED); }

    public void block(int currentTime)
    {
        //todo do i need to cater for blocked times or whatnot

        if (isRunning())
            processState = ProcessState.BLOCKED;
        else
            runTimeExceptionMessage(ProcessState.BLOCKED);
    }

    public void unblock(int currentTime) {
        if(isBlocked())
            processState = ProcessState.READY;
        else
            runTimeExceptionMessage(ProcessState.READY);
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

    public int getCurrentNumberPagesRunning()
    {
        int count = 0;
        for(Page page: pageList)
            if(page.isLoadedInMemory())
                count++;

        return count;
    }
}


