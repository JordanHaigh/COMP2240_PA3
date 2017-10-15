import java.util.ArrayList;
import java.util.List;

public class Memory
{
    private static final int MAX_FRAMES = 30;
    private int numberOfProcesses;
    private int fixedAllocationNumber;

    private int size;

    private Page[] frames = new Page[MAX_FRAMES];


    public Memory(int numberOfProcesses)
    {
        this.numberOfProcesses = numberOfProcesses;
        calculateFixedAllocationNumber(numberOfProcesses);
        size = 0;
    }

    public int size() {return size;}
    public Page[] getFrames(){return frames; }


    public boolean framesIsFull() {return size == MAX_FRAMES; }
    public boolean framesIsEmpty() {return size == 0; }

    public boolean isPageRunning(Page page)
    {
        for(Page runningPage: frames)
        {
            if(page == runningPage)
                return true;
        }
        return false;
    }

    public void calculateFixedAllocationNumber(int numberOfProcesses)
    {
        fixedAllocationNumber = (int)MAX_FRAMES/numberOfProcesses;
    }
    public int getFixedAllocationNumber(){return fixedAllocationNumber; }

    public boolean isFrameOccupied(int index)  { return frames[index] != null; }

    public List<Frame> findAllPagesInMemory(Process parentProcess)
    {
        List<Frame> pagesBelongingToParentProcess = new ArrayList<>();
        for(int i = 0; i < MAX_FRAMES; i++)
        {
            if(frames[i].getProcess() == parentProcess)
                pagesBelongingToParentProcess.add(new Frame(frames[i], i));

        }
        return pagesBelongingToParentProcess;
    }

    public void addToMemory(Page page)
    {
        if(!processHasReachedMaxAllocation(page.getProcess()))
        {
            //Allow it to add to memory
        }
        else
        {
            //do not allow adding to memory
        }

    }

    public int findNextEmptyIndex()
    {
        for(int i = 0; i < MAX_FRAMES; i++)
        {
            if(frames[i] == null)
                return i;
        }

        return -1;
    }

    private boolean processHasReachedMaxAllocation(Process process)
    {
        return process.getCurrentNumberPagesRunning() == fixedAllocationNumber;
    }



}
