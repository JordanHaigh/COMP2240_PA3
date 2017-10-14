import java.util.ArrayList;
import java.util.List;

public class CPU implements IObservable
{
    private ISchedulingAlgorithm schedulingAlgorithm = new RoundRobin();
    private int currentTime;

    private List<ISubscriber> subscriberList = new ArrayList<>();

    private List<Process> processList = new ArrayList<>();
    private List<Process> completedProcessList = new ArrayList<>();
    private Process currentProcess;

    public CPU(List<Process> processList)
    {
        this.processList = processList;
        currentTime = 0;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    /**
     * public void performProcessing(Process process, int numberOfCycles)
     * Run the process on the cpu for a specified period of time
     * Once completed, it will be added to a 'Completed Process List' for data statistics
     * If the process does not complete processing within the allocated number of cycles, it is sent to the back
     * of the process list.
     * @param process - Current process that will run on the CPU
     * @param numberOfCycles - Length of time the process runs for
     */
    public void performProcessing(Process process, int numberOfCycles)
    {
        this.currentProcess = process; //Used in determining if CPU is running if we need to preempt

        for(int i = 0; i < numberOfCycles; i++)
        {
            process.run();
            updateTimeTick(1);
        }

        if(process.getRemainingServiceTime() == 0)
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
        Process process = schedulingAlgorithm.nextProcessToRun(processList);

        if(process != null)
            schedulingAlgorithm.runProcess(process, this);
        /*else
        {
            //No processes to run. Currently idling
            updateTimeTick(1);
            // System.out.println("Forced time increment. Current time now: " + currentTime);

        }*/
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

