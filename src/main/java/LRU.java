import java.util.ArrayList;
import java.util.List;

public class LRU implements IPageReplacementAlgorithm
{
    Memory memory;

    public LRU(Memory memory)
    {
        this.memory = memory;
    }


    private List<Frame> populateLoadedPages(Process process)
    {
        return memory.findAllPagesInMemory(process);
    }

    @Override
    public int getReplacementIndex(Page pageToInsert)
    {
        //Need to find out who the page belongs to
        Process parentProcess = pageToInsert.getProcess();

        //Need to find out what pages are currently loaded
        //Look through the memory module and pick out pages that belong to the process
        //Add that to a separate list, making note of their indexes (for replacement)
        List<Frame> loadedPages = populateLoadedPages(parentProcess);

        //Need to find the least recently used page in that is loaded in the memory module
        //page needs to know the last time it was used

        Frame leastRecentlyUsedPage = loadedPages.get(0); //Initialise first

        //look through the list of pages loaded in memory and keep track of what their last time used is
        //then determine what the lowest time value is of all pages - this will be the page to be replaced.

        for(Frame frame: loadedPages)
        {
            if(frame.getPage().getTimeLastUsed() < leastRecentlyUsedPage.getPage().getTimeLastUsed())
                leastRecentlyUsedPage = frame;
        }

        return leastRecentlyUsedPage.getIndex();
        //todo implement page least recent used time
    }

    @Override
    public void replacePage(Page pageToInsert, int replacementIndex) {

    }
}
