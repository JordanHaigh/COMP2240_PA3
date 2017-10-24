package Model;

import java.util.HashMap;

/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * PageGeneratorFactory.java creates pages for the program. Utilises a hashmap so that the program won't create
 * duplicated page objects
 */
public class PageGeneratorFactory
{
    private static final HashMap<PageLookupIndex, Page> pageMap = new HashMap<>(); //ProcessID, PageID

    /**
     * public Page getPage(int processID, int pageNumber)
     * Determines whether the processID and Pagenumber already exist in the Hashmap.
     * If it does exist, it will return that Page.
     * If it doesn't exist, it will add the Page values to the Hashmap and return that page
     * @param processID - ID of the Process
     * @param pageNumber - Page number in the Process
     * @return - Page value stored in hashmap
     */
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

    /**
     * Student Number: 3256730 Jordan Haigh
     * COMP2240 A3
     * Private Class PageLookupIndex stores the processID and page number used for determining if it is in the hashmap
     * or not
     */
    private class PageLookupIndex
    {
        private int processID;
        private int pageNumber;

        /**
         * public PageLookupIndex(int processID, int pageNumber)
         * Overloaded constructor
         * @param processID - ID of Process
         * @param pageNumber Page number
         */
        public PageLookupIndex(int processID, int pageNumber)
        {
            this.processID = processID;
            this.pageNumber = pageNumber;
        }

        /**
         * public boolean equals(Object o)
         * @param o - Second Object to compare to
         * @return - Boolean value if equal or not
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PageLookupIndex that = (PageLookupIndex) o;

            if (processID != that.processID) return false;
            return pageNumber == that.pageNumber;
        }

        /**
         * public int hashCode()
         * @return - Distinct hashcode for PageLookupIndex
         */
        @Override
        public int hashCode() {
            int result = processID;
            result = 31 * result + pageNumber;
            return result;
        }
    }

}