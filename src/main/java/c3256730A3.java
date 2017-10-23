import Algorithms.ClockPolicy;
import Algorithms.IPageReplacementAlgorithm;
import Algorithms.LRU;
import Machine.CPU;
import Machine.Memory;
import Model.Page;
import Model.SchedulingProcess;
import Model.ProcessFileReader;
import ObserverPattern.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class c3256730A3 implements ISubscriber
{
    private int currentTime = 0;
    private ProcessFileReader processFileReader = new ProcessFileReader();
    private List<SchedulingProcess> masterSchedulingProcessList = new ArrayList<>();
    List<IPageReplacementAlgorithm> pageReplacementAlgorithms = new ArrayList<>();



    public static void main(String[]args)
    {
        c3256730A3 intFace = new c3256730A3();
        try { intFace.run(args); } catch (IOException e) { e.printStackTrace(); }
    }


    private void run(String[]args) throws IOException {
        List<SchedulingProcess> masterSchedulingProcessList = processFileReader.run(args);

        //Start processing
        Memory memory = new Memory(masterSchedulingProcessList.size());

        IPageReplacementAlgorithm lru = new LRU(memory);
        IPageReplacementAlgorithm clockPolicy = new ClockPolicy(memory);
        pageReplacementAlgorithms.add(lru);
        pageReplacementAlgorithms.add(clockPolicy);


        for(IPageReplacementAlgorithm pageReplacementAlgorithm: pageReplacementAlgorithms)
        {
            List<SchedulingProcess> copiedSchedulingProcessList = new ArrayList<>();
            memory.clear(); //Wipe frames and current size of frames
            memory.setPageReplacementAlgorithm(pageReplacementAlgorithm);

            copiedSchedulingProcessList.clear();

            for(SchedulingProcess schedulingProcess : masterSchedulingProcessList)
            {
                copiedSchedulingProcessList.add(new SchedulingProcess(schedulingProcess));
                for(Page page: schedulingProcess.getPageList())
                    page.resetData();
            }
            //Clean data to work with, no old data



            CPU cpu = new CPU(copiedSchedulingProcessList, memory);
            cpu.addSubscriber(this);
            currentTime = 0; //Resets for each page replacement algorithm

            //While each process still has pages in its page list, keep going
            while(cpu.hasQueuedProcesses() || copiedSchedulingProcessList.size() > 0)
            {
                cpu.cycle();
            }

            //individual statistics
            runIndividualStatistics(pageReplacementAlgorithm, copiedSchedulingProcessList);
        }

    }

    private void runIndividualStatistics(IPageReplacementAlgorithm pageReplacementAlgorithm, List<SchedulingProcess> copiedSchedulingProcessList)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(pageReplacementAlgorithm.toString())
        .append("\nPID \t Turnaround Time \t\t # Faults \t Fault Times  ");

        for(SchedulingProcess process: copiedSchedulingProcessList)
        {
            sb.append(process.getId())
                    .append(" \t ")
                    .append(process.getTurnaroundTime())
                    .append(" \t\t ")
                    .append(process.getNumberOfFaults())
                    .append(" \t ")
                    .append(process.getFaultTimesToString());
        }

        System.out.println(sb.toString());
    }

    @Override
    public void handleMessage(ObservableMessage message) {
        if(message instanceof ObservableCPUTimeMessage)
        {
            currentTime = ((ObservableCPUTimeMessage) message).getCpuTimeTick();

        }
    }
}
