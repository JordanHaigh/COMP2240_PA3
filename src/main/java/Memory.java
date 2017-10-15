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

    public void addToMemory(Page pageToInsert)
    {
        boolean needToRunPageReplacement = true;

        List<Frame> loadedProcessPages = findAllPagesInMemory(pageToInsert.getProcess());

        ////////////////SCENARIO 1 - PAGE ALREADY RUNNING////////////////////

        //Cater for scenario where page may already be running
        for(Frame frame: loadedProcessPages)
        {
            //We already know they have the same parent process
            // Find out if they both have the same page number
            if(frame.getPage().getPageNumber() == pageToInsert.getPageNumber())
            {
                //This page number is already running in memory
                //Then we don't need to replace this page and issue a page fault
                needToRunPageReplacement = false;
            }
        }

        //////////////SCENARIO 2 - EMPTY FRAMES FOR PAGE ENTRY/////////////////////

        //Cater for the scenario that frames are empty
        if(loadedProcessPages.size() < getFixedAllocationNumber())
        {
            //Find the next empty index in the frames
            needToRunPageReplacement = false;
            int replacementIndex = findNextEmptyIndex(); //Will never return -1 since we have already checked for empty spots
            frames[replacementIndex] = pageToInsert;

            //In case of clock replacement
            frames[replacementIndex].setUseBit(true);
            //todo issue page fault, update current time it entered, etc.

        }
        //////////////////SCENARIO 3 - NEED TO USE THE PAGE REPLACEMENT ALGORITHM////////////////////
        if(needToRunPageReplacement)
        {
            pageReplacementAlgorithm.replacePage(pageToInsert, pageReplacementAlgorithm.getReplacementIndex(pageToInsert));
            //Issue page fault
            //todo need to implement page faulting when replacing an index
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
