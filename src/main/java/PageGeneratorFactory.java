import java.util.HashMap;

public class PageGeneratorFactory
{
    private static final HashMap<PageLookupIndex, Page> pageMap = new HashMap<>(); //ProcessID, PageID

    public Page getPage(int processID, int pageNumber)
    {
        PageLookupIndex lookup = new PageLookupIndex(processID, pageNumber);

        if(pageMap.containsKey(lookup))
            return pageMap.get(lookup);
        else
        {
            Page page = new Page(pageNumber);

            pageMap.put(lookup, page);
            return page;
        }
    }


    private class PageLookupIndex
    {
        private int processID;
        private int pageNumber;
        public int getProcessID(){return processID;}
        public int getPageNumber(){return pageNumber;}

        public PageLookupIndex(int processID, int pageNumber)
        {
            this.processID = processID;
            this.pageNumber = pageNumber;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PageLookupIndex that = (PageLookupIndex) o;

            if (processID != that.processID) return false;
            return pageNumber == that.pageNumber;
        }

        @Override
        public int hashCode() {
            int result = processID;
            result = 31 * result + pageNumber;
            return result;
        }
    }

}