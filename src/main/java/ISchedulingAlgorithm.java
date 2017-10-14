import java.util.List;

/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * ISchedulingAlgorithm.java implements methods used by any scheduling algorithm. These methods determine runtime and selection
 */
public interface ISchedulingAlgorithm
{
    void runProcess(Process process, CPU cpu); //Run the process on the cpu
    Process nextProcessToRun(List<Process> processList); //Determine the next process to run
    int timeRequiredToRunNextProcess(Process process); //Determine the time required to run the next process
}