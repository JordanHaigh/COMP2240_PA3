package Algorithms;

import Machine.CPU;
import Model.SchedulingProcess;

import java.util.List;

/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * Algorithms.ISchedulingAlgorithm.java implements methods used by any scheduling algorithm. These methods determine runtime and selection
 */
public interface ISchedulingAlgorithm
{
    void runProcess(SchedulingProcess process, CPU cpu); //Run the process on the cpu
    SchedulingProcess nextProcessToRun(List<SchedulingProcess> processList); //Determine the next process to run
    int timeRequiredToRunNextProcess(SchedulingProcess process); //Determine the time required to run the next process
}