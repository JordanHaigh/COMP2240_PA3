package ObserverPattern;

/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * ObserverPattern.ObservableCPUTimeMessage.java extends from the abstract ObserverPattern.ObservableMessage class
 * This class monitors the current time when updated by the cpu.
 * This is later fed back to c3256730.java, utilising the observer pattern
 */
public class ObservableCPUTimeMessage extends ObservableMessage
{
    private int cpuTimeTick;

    public ObservableCPUTimeMessage(int cpuTimeTick)
    {
        this.cpuTimeTick = cpuTimeTick;
    }

    /**
     * public int getCpuTimeTick()
     * @return - Updated Cpu time
     */
    public int getCpuTimeTick() { return cpuTimeTick; }
}