package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Student Number: 3256730 Jordan Haigh
 * COMP2240 A3
 * ProcessFileReader.java Class reads from a data file and extracts all information relevant for the program.
 */
public class ProcessFileReader
{
    private PageGeneratorFactory factory = new PageGeneratorFactory();
    private List<SchedulingProcess> schedulingProcessList = new ArrayList<>();

    /**
     * public List<SchedulingProcess> run(String[]args)
     * Checks if there are arguments in the command line.
     * Builds a list of processes and its pages from each argument and adds to a list
     * @param args - String arguments from command line
     * @return - List of Scheduling Processes
     * @throws IOException
     */
    public List<SchedulingProcess> run(String[]args) throws IOException {
        if(args.length == 0)
            throw new IllegalArgumentException("Error. Missing program arguments");

        for(String filePath: args)
        {
            //Read each schedulingProcess file
            SchedulingProcess schedulingProcess = readProcess(filePath);
            schedulingProcessList.add(schedulingProcess);
        }

        return schedulingProcessList;
    }

    /**
     * public SchedulingProcess readProcess(String filePath)
     * Reads the file location and processes the data into a list of processes
     * @param filePath - Filepath of data file
     * @return - List of Processes found in the data file
     */
    public SchedulingProcess readProcess(String filePath) throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String fileContents = readWholeFile(reader);

            // Once file has been fully read, convert the string builder into a standard java string
            return readProcessDataFromString(fileContents ,filePath);
        }
        catch(Exception e)
        {
            throw e;
        }

    }

    /**
     * public String readWholeFile(Buffered reader)
     * Reads the entire input data file and appends to a stringbuilder
     * @param reader - BufferedReader used to read the file
     * @return - String containing all data
     */
    private String readWholeFile(BufferedReader reader) throws IOException
    {
        //Modified from http://abhinandanmk.blogspot.com.au/2012/05/java-how-to-read-complete-text-file.html
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = reader.readLine()) != null)
        {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }

    /**
     * private SchedulingProcess readProcessDataFromString(String fileContents, String filePath) throws Exception
     * Firstly cleanses the string and stores in another variable so the original text data is unaltered
     * Then creates a process from the cleansed string dataset
     * @param fileContents - Line of all data
     * @param filePath - Name of file path
     * @return - List<Process> Object with all Processes found in data file
     * @throws Exception - Check if file was not imported correctly
     */
    private SchedulingProcess readProcessDataFromString(String fileContents, String filePath)
    {
        String processName = filePath.replaceAll(".txt","")
                .replaceAll(".dat","")
                .replaceAll("process","")
                .replaceAll("Process","");

        int processId = Integer.parseInt(processName);

        //Cleanse line to remove \n, \t characters and make the any glyphs toUpperCase
        String cleansedData = cleanseProcessData(fileContents);

        String[] individualPages = cleansedData.split(" ");

        List<Page> pageList = new ArrayList<>();


        for(String page: individualPages)
        {
            int pageId = Integer.parseInt(page);

            Page newPage = factory.getPage(processId, pageId);

            pageList.add(newPage);
        }

        return new SchedulingProcess(processId, pageList);
    }
    /**
     * private String cleanseProcessDataFile(String fileContents)
     * Removes all unnecessary characters and returns a cleaner string to be worked with
     * @param fileContents - Data file to string
     * @return - String of all data without certain characters (Double spaces, new lines, tabs)
     */
    private String cleanseProcessData(String fileContents)
    {
        String cleansed = fileContents.replaceAll("begin\n", "")
                .replaceAll("end\n", "")
                .replaceAll("\r\n", " ") // \r\n is windows version of new line
                .replaceAll("\n", " ")
                .replaceAll("\t", " ");



        while (cleansed.contains("  "))
            cleansed = cleansed.replace("  ", " ");

        return cleansed;
    }

}
