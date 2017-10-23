package Machine;

import Algorithms.ISchedulingAlgorithm;
import Algorithms.RoundRobin;
import Model.Instruction;
import Model.Page;
import Model.SchedulingProcess;
import ObserverPattern.*;

import java.util.ArrayList;
import java.util.List;

public class CPU implements IObservable
{
    private ISchedulingAlgorithm schedulingAlgorithm = new RoundRobin();
    private int currentTime;

    private List<ISubscriber> subscriberList = new ArrayList<>();

    private List<SchedulingProcess> processList = new ArrayList<>();
    private List<SchedulingProcess> completedProcessList = new ArrayList<>();
    private SchedulingProcess currentProcess;

    private Memory memory;
    private IOController ioController;

    public CPU(List<SchedulingProcess> processList, Memory memory)
    {
        this.processList = processList;
        this.memory = memory;

        currentTime = 0;

        ioController = new IOController();
        addSubscriber(ioController);


        ioController.addSubscriber(memory);

    }

    public int getCurrentTime() {
        return currentTime;
    }
    public boolean hasQueuedProcesses() {return processList.size() > 0; }

    /**
     * public void performProcessing(Model.SchedulingProcess process, int numberOfCycles)
     * Run the process on the cpu for a specified period of time
     * Once completed, it will be added to a 'Completed Model.SchedulingProcess List' for data statistics
     * If the process does not complete processing within the allocated number of cycles, it is sent to the back
     * of the process list.
     * @param process - Current process that will run on the Machine.CPU
     * @param numberOfCycles - Length of time the process runs for
     */
    public void performProcessing(SchedulingProcess process, int numberOfCycles)
    {
        this.currentProcess = process; //Used in determining if Machine.CPU is running if we need to preempt

        for(int i = 0; i < numberOfCycles; i++)
        {
            Page nextPageFromProcess = process.getNextPageFromList();

            if (!nextPageFromProcess.isLoadedInMemory())
            {
                issuePageFault(nextPageFromProcess);
                System.out.println("Time  " + currentTime + ": " + nextPageFromProcess.getParentProcess().toString() + ": PAGE("+ nextPageFromProcess.getPageNumber()+") FAULT");
                break; //todo double check
            }
            else {
                process.run(currentTime);
                System.out.println("Time  " + currentTime + ": " + nextPageFromProcess.getParentProcess().toString() + ": PAGE("+ nextPageFromProcess.getPageNumber()+") RUNNING");
                updateTimeTick(Instruction.INSTRUCTION_TIME);

            }
        }

        if(process.hasReachedEndOfPageList())
        {
            processList.remove(process);
            completedProcessList.add(process);
        }

        else //In the event of round robin
        {
            processList.remove(process);
            processList.add(process); //Add to back of the process list
        }

    }

    /**
     * public void updateTimeTick(int timeIncrement)
     * Updates the current time by the specified increment
     * @param timeIncrement - Time increment to update current time
     */
    private void updateTimeTick(int timeIncrement)
    {
        currentTime += timeIncrement;

        ObservableMessage message = new ObservableCPUTimeMessage(currentTime);
        notifySubscribers(message);
    }

    public void cycle()
    {
        checkAllProcessesBlocked();

        SchedulingProcess nextProcessToRun = schedulingAlgorithm.nextProcessToRun(processList);

        if(nextProcessToRun != null && !nextProcessToRun.isBlocked())
            schedulingAlgorithm.runProcess(nextProcessToRun, this);
        else if (nextProcessToRun.isBlocked())
        {
            processList.remove(nextProcessToRun);
            processList.add(nextProcessToRun);
            //updateTimeTick(1);
            //process is waiting for pages to be loaded into memory.
            //Cycle for RR cycles and see if it ends up being loaded during this time quantum
        }
        else
        {
            //No processes to run. Currently idling
            //updateTimeTick(1);
             System.out.println("No processes to run: " + currentTime);

        }
    }

    private void issuePageFault(Page page)
    {
        SchedulingProcess parentProcess = page.getParentProcess();
        parentProcess.block(currentTime);
        ObservablePageFaultMessage pageFaultMessage = new ObservablePageFaultMessage(page, currentTime);
        notifySubscribers(pageFaultMessage);
    }

    private void checkAllProcessesBlocked()
    {
        boolean allProcessesBlocked = true;
        for(SchedulingProcess process: processList)
        {
            allProcessesBlocked = process.isBlocked();
            if(!allProcessesBlocked)
                break;
        }

        //check if all processes blocked
        if(allProcessesBlocked)
        {
            //if all blocked, update to minimum time that pages is ready
            //Find minimum time to update to
            int n = getNextTimeReadyFromIOController();

            int delta = n-currentTime;
            updateTimeTick(delta);
        }
    }

    private int getNextTimeReadyFromIOController()
    {
        return ioController.getNextIORequest().getPageReadyTime();
    }

    @Override
    public void addSubscriber(ISubscriber subscriber) {
        subscriberList.add(subscriber);
    }

    @Override
    public void removeSubscriber(ISubscriber subscriber) {
        subscriberList.remove(subscriber);
    }

    @Override
    public void notifySubscribers(ObservableMessage message) {
        for(ISubscriber subscriber: subscriberList)
            subscriber.handleMessage(message);
    }


}

