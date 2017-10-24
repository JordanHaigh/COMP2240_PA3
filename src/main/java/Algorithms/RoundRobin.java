package Algorithms;

import Machine.CPU;
import Model.SchedulingProcess;

import java.util.List;
/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A1
 * RoundRobin.java is one of the scheduling algorithms used for the Machine.CPU
 *  Each process receives a time quantum of 3 units
 */
public class RoundRobin implements ISchedulingAlgorithm
{
    /**
     * public void runProcess(SchedulingProcess process, CPU cpu)
     * Runs the dispatcher first to ready the process. SchedulingProcess is run for a period of time
     * @param process - Model.SchedulingProcess to run on the cpu
     * @param cpu - CPU instance
     */
    @Override
    public void runProcess(SchedulingProcess process, CPU cpu) {


        if(process.isNew())
            process.admit(cpu.getCurrentTime());

        if(process.isReady())
            process.dispatch(cpu.getCurrentTime());

        cpu.performProcessing(process,timeRequiredToRunNextProcess(process));

        if(!process.hasReachedEndOfPageList() && !process.isBlocked())
            process.interrupt(cpu.getCurrentTime());
        else if(process.isBlocked())
        {} //do nothing
        else
            process.exit(cpu.getCurrentTime());
    }

    /**
     * public SchedulingProcess nextProcessToRun(List<SchedulingProcess> processList)
     * Decides which process is to run on the cpu next, selects the first element from the list
     * @param processList - SchedulingProcess list containing all processes ready to be run on the cpu
     * @return - SchedulingProcess to run next
     */
    @Override
    public SchedulingProcess nextProcessToRun(List<SchedulingProcess> processList) {
        if(processList.isEmpty())
            return null;
        else
            return processList.get(0);
    }

    /**
     * public int timeRequiredToRunNextProcess(SchedulingProcess process)
     * Determines the time required to run the next process
     * @param process - SchedulingProcess that will run on the cpu
     * @return - Integer determining time length
     */
    @Override
    public int timeRequiredToRunNextProcess(SchedulingProcess process) {
        if(process.getRemainingNumberOfPages() < 3)
            return process.getRemainingNumberOfPages();
        else
            return 3;
    }

}