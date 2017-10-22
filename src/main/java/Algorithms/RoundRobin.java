package Algorithms;

import Machine.CPU;
import Model.SchedulingProcess;

import java.util.List;
/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A1
 * PriorityRoundRobin.java is one of the scheduling algorithms used for the Machine.CPU
 *  This is a variant of the standard Round Robin (RR) algorithm.
 * Processes are divided into two priority classes Higher Priority Class (HPC):  processes with priority 0, 1 or 2
 * and
 * Lower Priority Class (LPC): processes with priority 3, 4 or 5.
 * PRR algorithm is exactly same as the standard RR algorithm except each HPC process receives a
 * time quantum of 4 units and each LPC process receives a time quantum of 2 units.
 */
public class RoundRobin implements ISchedulingAlgorithm
{


    /**
     * public void runProcess(Model.SchedulingProcess process, Machine.CPU cpu)
     * Runs the dispatcher first to ready the process. Model.SchedulingProcess is run for a period of time depending on the priority.
     * Stringbuilder appends the time the process starts relevant to the specification
     * @param process - Model.SchedulingProcess to run on the cpu
     * @param cpu - Machine.CPU instance
     */
    @Override
    public void runProcess(SchedulingProcess process, CPU cpu) {


        if(process.isNew())
            process.admit(cpu.getCurrentTime());

        process.dispatch(cpu.getCurrentTime());

        cpu.performProcessing(process,timeRequiredToRunNextProcess(process));

        if(!process.hasReachedEndOfPageList())
            process.interrupt(cpu.getCurrentTime());
        else
            process.exit(cpu.getCurrentTime());
    }

    /**
     * public Model.SchedulingProcess nextProcessToRun(List<Model.SchedulingProcess> processList)
     * Decides which process is to run on the cpu next, selects the first element from the list
     * @param processList - Model.SchedulingProcess list containing all processes ready to be run on the cpu
     * @return - Model.SchedulingProcess to run next
     */
    @Override
    public SchedulingProcess nextProcessToRun(List<SchedulingProcess> processList) {
        if(processList.isEmpty())
            return null;
        else
            return processList.get(0);
    }

    /**
     * public int timeRequiredToRunNextProcess(Model.SchedulingProcess process)
     * Determines the time required to run the next process
     * @param process - Model.SchedulingProcess that will run on the cpu
     * @return - Integer determining time length
     */
    @Override
    public int timeRequiredToRunNextProcess(SchedulingProcess process) {
        if(process.getRemainingNumberOfPages() < 3)
            return process.getRemainingNumberOfPages();
        else
            return 3;
    }

    /**
     * public String toString()
     * @return - PRR data in toString format as per specification
     */
    /*@Override
    public String toString()
    {
        return sb.toString();
    }*/
}