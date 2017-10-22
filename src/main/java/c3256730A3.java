import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class c3256730A3 implements ISubscriber
{
    private int currentTime = 0;

    public static void main(String[]args)
    {
        c3256730A3 intFace = new c3256730A3();
        try { intFace.run(args); } catch (IOException e) { e.printStackTrace(); }
    }


    private void run(String[]args) throws IOException {
        if(args.length == 0)
            throw new IllegalArgumentException("Error. Missing program arguments");

        List<Process> processList = new ArrayList<>();
        ProcessFileReader processFileReader = new ProcessFileReader();

        for(String filePath: args)
        {
            //Read each process file
            Process process = processFileReader.readProcess(filePath);
            processList.add(process);
        }


        //Start processing
        Memory memory = new Memory(processList.size());

        IPageReplacementAlgorithm lru = new LRU(memory);
        IPageReplacementAlgorithm clockPolicy = new ClockPolicy(memory);
        List<IPageReplacementAlgorithm> pageReplacementAlgorithms = new ArrayList<>();
        pageReplacementAlgorithms.add(lru);
        pageReplacementAlgorithms.add(clockPolicy);

        for(IPageReplacementAlgorithm pageReplacementAlgorithm: pageReplacementAlgorithms)
        {
            //todo clear memory
            //todo reset process list
            //todo reset pages

            memory.setPageReplacementAlgorithm(pageReplacementAlgorithm);
            CPU cpu = new CPU(processList, memory);

            //While each process still has pages in its page list, keep going
            while(cpu.hasQueuedProcesses())
            {
                cpu.cycle();
            }
        }
    }

    @Override
    public void handleMessage(ObservableMessage message) {
        if(message instanceof ObservableCPUTimeMessage)
        {
            currentTime = ((ObservableCPUTimeMessage) message).getCpuTimeTick();

        }
    }
}
