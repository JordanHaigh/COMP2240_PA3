import java.util.ArrayList;
import java.util.List;

public class Memory
{
    private static final int MAX_FRAMES = 30;
    private int numberOfProcesses;
    private int fixedAllocationNumber;
    private IPageReplacementAlgorithm pageReplacementAlgorithm;

    private int size;

    private Page[] frames = new Page[MAX_FRAMES];


    public Memory(int numberOfProcesses)
    {
        this.numberOfProcesses = numberOfProcesses;
        calculateFixedAllocationNumber(numberOfProcesses);
        size = 0;
    }

    private void calculateFixedAllocationNumber(int numberOfProcesses) { fixedAllocationNumber = (int)MAX_FRAMES/numberOfProcesses; }

    public int getFixedAllocationNumber(){return fixedAllocationNumber; }

    public boolean isFrameOccupied(int index)  { return frames[index] != null; }

    private boolean processHasReachedMaxAllocation(Process process) { return process.getCurrentNumberPagesRunning() == fixedAllocationNumber; }

    public Page[] getFrames(){ return frames; }

    public int getFramesSize()
    {
        int count = 0;

        for(int i = 0; i < MAX_FRAMES; i++)
        {
            if(frames[i] != null)
                count++;
        }
        return count;
    }

    public boolean isPageRunning(Page page)
    {
        for(Page runningPage: frames)
        {
            if(page == runningPage)
                return true;
        }
        return false;
    }

    public List<Frame> findAllPagesInMemory(Process parentProcess)
    {
        List<Frame> pagesBelongingToParentProcess = new ArrayList<>();
        for(int i = 0; i < MAX_FRAMES; i++)
        {
            if(frames[i] != null && frames[i].getParentProcess() == parentProcess)
                pagesBelongingToParentProcess.add(new Frame(frames[i], i));

        }
        return pagesBelongingToParentProcess;
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

    public void addToMemory(Page pageToInsert)
    {
        int index = pageReplacementAlgorithm.getReplacementIndex(pageToInsert);
        if(isFrameOccupied(index))
            unloadPageAtIndex(index);

        loadPageAtIndex(pageToInsert, index);
    }

    private void unloadPageAtIndex(int index)
    {
        if(index < 0 || index > MAX_FRAMES)
            throw new IllegalArgumentException("Index used to unload page is out of bounds");

        frames[index] = null;
    }

    private void loadPageAtIndex(Page page, int index)
    {
        if(index < 0 || index > MAX_FRAMES)
            throw new IllegalArgumentException("Index used to unload page is out of bounds");

        frames[index] = page;
        //todo page fault, increment time etc
    }
}
