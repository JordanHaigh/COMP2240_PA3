import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class c3256730A3 implements ISubscriber
{
    private int currentTime = 0;

    public static void main(String[]args)
    {
        c3256730A3 intFace = new c3256730A3();
        intFace.run(args);
    }


    private void run(String[]args)
    {
        if(args.length == 0)
        {
            throw new IllegalArgumentException("Error. Missing program arguments");
        }

        List<Process> processList = new ArrayList<>();

        for(String filePath: args)
        {
            //Read each process file
            Process process = null;
            try { process = readProcess(filePath); } catch (IOException e) { e.printStackTrace(); }
            processList.add(process);
        }

        //Start processing


    }
    private Process readProcess(String filePath) throws IOException {
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

    private Process readProcessDataFromString(String fileContents, String filePath)
    {
        String processName = filePath.replaceAll(".txt","")
                                    .replaceAll(".dat","");

        //Cleanse line to remove \n, \t characters and make the any glyphs toUpperCase
        String cleansedData = cleanseProcessData(fileContents);

        String[] individualPages = cleansedData.split(" ");

        List<Page> pageList = new ArrayList<>();

        for(String page: individualPages)
        {
            int pageId = Integer.parseInt(page);
            pageList.add(new Page(pageId));
        }

        Process process = new Process(processName, pageList);

        return process;
    }

    private String cleanseProcessData(String fileContents)
    {
        String cleansed = fileContents.replaceAll("\r\n", " ") // \r\n is windows version of new line
                .replaceAll("\n", " ")
                .replaceAll("\t", " ")
                .replaceAll("begin", "")
                .replaceAll("end", "");


        while (cleansed.contains("  "))
            cleansed = cleansed.replace("  ", " ");

        return cleansed;
    }


    @Override
    public void handleMessage(ObservableMessage message) {
        if(message instanceof ObservableCPUTimeMessage)
        {
            currentTime = ((ObservableCPUTimeMessage) message).getCpuTimeTick();

        }
    }
}
