import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class c3256730A3 implements ISubscriber
{
    private int currentTime = 0;
    private ProcessFileReader processFileReader = new ProcessFileReader();
    private List<Process> masterProcessList = new ArrayList<>();
    List<IPageReplacementAlgorithm> pageReplacementAlgorithms = new ArrayList<>();



    public static void main(String[]args)
    {
        c3256730A3 intFace = new c3256730A3();
        try { intFace.run(args); } catch (IOException e) { e.printStackTrace(); }
    }


    private void run(String[]args) throws IOException {
        List<Process> masterProcessList = processFileReader.run(args);

        //Start processing
        Memory memory = new Memory(masterProcessList.size());

        IPageReplacementAlgorithm lru = new LRU(memory);
        IPageReplacementAlgorithm clockPolicy = new ClockPolicy(memory);
        pageReplacementAlgorithms.add(lru);
        pageReplacementAlgorithms.add(clockPolicy);


        for(IPageReplacementAlgorithm pageReplacementAlgorithm: pageReplacementAlgorithms)
        {
            List<Process> copiedProcessList = new ArrayList<>();
            //todo clear memory
            memory.clear(); //Wipe frames and current size of frames
            memory.setPageReplacementAlgorithm(pageReplacementAlgorithm);

            //todo reset process list
            copiedProcessList.clear();

            for(Process process: masterProcessList)
                copiedProcessList.add(new Process(process));

            //todo reset pages -- probably dont need to do this


            CPU cpu = new CPU(copiedProcessList, memory);
            cpu.addSubscriber(this);
            currentTime = 0; //Resets for each page replacement algorithm

            //While each process still has pages in its page list, keep going
            while(cpu.hasQueuedProcesses() || copiedProcessList.size() > 0)
            {
                cpu.cycle();
            }

            //individual statistics
        }

        //final statistics
    }

    @Override
    public void handleMessage(ObservableMessage message) {
        if(message instanceof ObservableCPUTimeMessage)
        {
            currentTime = ((ObservableCPUTimeMessage) message).getCpuTimeTick();

        }
    }
}
