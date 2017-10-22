package Machine;

import Algorithms.IPageReplacementAlgorithm;
import Model.*;
import ObserverPattern.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Memory implements ISubscriber
{
    private static final int MAX_FRAMES = 30;
    private int fixedAllocationNumber;
    private IPageReplacementAlgorithm pageReplacementAlgorithm;

    private int size;

    private Page[] frames = new Page[MAX_FRAMES];


    public Memory(int numberOfProcesses)
    {
        calculateFixedAllocationNumber(numberOfProcesses);
        size = 0;
    }


    private void calculateFixedAllocationNumber(int numberOfProcesses) { fixedAllocationNumber = (int)MAX_FRAMES/numberOfProcesses; }

    public int getFixedAllocationNumber(){return fixedAllocationNumber; }

    public boolean isFrameOccupied(int index)  { return frames[index] != null; }

    private boolean processHasReachedMaxAllocation(SchedulingProcess process) { return process.getCurrentNumberPagesRunning() == fixedAllocationNumber; }

    public void setPageReplacementAlgorithm(IPageReplacementAlgorithm pageReplacementAlgorithm) { this.pageReplacementAlgorithm = pageReplacementAlgorithm; }

    public int getMaxFrames() {return MAX_FRAMES; }

    public Page[] getFrames(){ return frames; }

    public void clear()
    {
        pageReplacementAlgorithm = null;

        size = 0;
        Arrays.fill(frames, null);
    }

    public int getCountOfAllPagesRunning()
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

    public List<Frame> findAllPagesInMemory(SchedulingProcess parentProcess)
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


    public void addToMemory(Page pageToInsert, int currentTime)
    {
        int index = pageReplacementAlgorithm.getReplacementIndex(pageToInsert);

        if(isFrameOccupied(index))
            unloadPageAtIndex(index, currentTime);

        loadPageAtIndex(pageToInsert, index, currentTime);
        pageToInsert.setTimeLastUsed(currentTime);

    }

    private void unloadPageAtIndex(int index, int currentTime)
    {
        if(index < 0 || index > MAX_FRAMES)
            throw new IllegalArgumentException("Index used to unload page is out of bounds");

        frames[index].setLoadedInMemory(false);
        frames[index] = null;
        size--;
    }

    private void loadPageAtIndex(Page page, int index, int currentTime)
    {
        if(index < 0 || index > MAX_FRAMES)
            throw new IllegalArgumentException("Index used to unload page is out of bounds");

        page.setLoadedInMemory(true);
        page.setUseBit(true);
        frames[index] = page;
        size++;
    }


    @Override
    public void handleMessage(ObservableMessage message) {
        if(message instanceof ObservablePageReadyMessage)
        {
            Page page = ((ObservablePageReadyMessage) message).getPage();
            int currentTime = ((ObservablePageReadyMessage) message).getCurrentTime();
            addToMemory(page, currentTime);
        }
    }
}