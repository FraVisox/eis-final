package it.unipd.dei.dbdc;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

public class CommandLineInterpreter {

    // TODO: completa con altre query (ad esempio la key per il download, cose cosi)

    private static final Options options = new Options();
    private final static HelpFormatter formatter = new HelpFormatter();

    // The actions you can perform: these are mandatory.
    private final static Option[] actions = {
            new Option("h", "help", false, "Print help"),
            new Option("d", "download-files", false, "Download files from the selected API"),
            new Option("a", "analyze-terms", false, "Analyze the top 50 terms of the selected files"),
            new Option("da", "download-and-analyze", false, "Download files from the selected API and analyze the top 50 terms of those files")
    };

    // The download options
    private final static Option[] download = {
            new Option("apf", "api-properties-file", true, "Contains the path to the properties of the API to call"),
            new Option("dowpf", "download-properties-file", true, "Contains the path to the properties file that contains the managers that is possible to call"),
            new Option("totpf", "total-properties-file", true, "Contains the path to the properties file that contains the common format and the number of terms to extract"),
    };

    // The analyze options
    private final static Option[] analyze = {
            new Option("anapf", "analyze-properties-file", true, "Contains the path to the properties file that contains the analyzer to use for the extraction"),
            new Option("despf", "deserializers-properties-file", true, "Contains the path to the properties file that contains the deserializers to use"),
            new Option("serpf", "serializers-properties-file", true, "Contains the path to the properties file that contains the serializers to use"),
            new Option("path", "folder-path", true, "Contains the location of the place to take the files from"),
            new Option("n", "number", true, "Contains the number of terms you want to have in the final output"),
            new Option("stop", "enable-stop-words", true, "True if you want to enable the stop-words in the analysis")
    };

    private final CommandLine cmd;

    public CommandLineInterpreter(String[] args) {
        cmd = parseCommandLine(args);
        /*if (cmd == null) FIXME
        {
            throw new IllegalStateException("Actions not specified");
        }*/
    }

    private static CommandLine parseCommandLine(String[] args) {
        defineOptions();
        CommandLine cmd = parse(args);
        if (cmd == null || cmd.hasOption("h")) {
            formatter.printHelp("App -{et} [options]", options);
        }
        return cmd;
    }

    // There are three stages to command line processing.
    // They are the definition, parsing and interrogation stages.

    /*
    1. DEFINITION:
    Each command line must define the set of options that will be used to define the interface to the application.
    CLI uses the Options class, as a container for Option instances. There are two ways to create Options in CLI.
    One of them is via the constructors, the other way is via the factory methods defined in Options.
    The result of the definition stage is an Options instance.
     */
    private static void defineOptions() {

        // Add the possible actions to an OptionGroup
        OptionGroup actionGroup = new OptionGroup();
        for (Option op : actions) {
            actionGroup.addOption(op);
        }

        // Set the options as required
        // actionGroup.setRequired(true); FIXME
        options.addOptionGroup(actionGroup);

        // download options
        for (Option op : download) {
            options.addOption(op);
        }

        // Search options
        for (Option op : analyze) {
            options.addOption(op);
        }
    }

    /*
    2. PARSING
    The parsing stage is where the text passed into the application via the command line is processed.
    The text is processed according to the rules defined by the parser implementation.
    The parse method defined on CommandLineParser takes an Options instance and a String[] of arguments and returns a CommandLine.
    The result of the parsing stage is a CommandLine instance.
     */
    public static CommandLine parse(String[] args) {

        // There may be several implementations of the CommandLineParser interface, the recommended one is the DefaultParser
        CommandLineParser parser = new DefaultParser();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (org.apache.commons.cli.ParseException ex) {
            System.err.println("ERROR - parsing command line:");
            System.err.println(ex.getMessage());
        }
        return cmd;
    }

    /*
    3. INTERROGATION
    The interrogation stage is where the application queries the CommandLine to decide what execution branch to take
    depending on boolean options and uses the option values to provide the application data.
    This stage is implemented in the user code. The accessor methods on CommandLine provide the interrogation capability to the user code.
    The result of the interrogation stage is that the user code is fully informed of all the text that was supplied
    on the command line and processed according to the parser and Options rules.
     */
    public boolean help() {
        return false;
        // return cmd.hasOption("h"); // FIXME
    }
    public boolean downloadPhase() {
        return true;
        // return cmd.hasOption("d") || cmd.hasOption("ds"); FIXME
    }

    public boolean analyzePhase() {
        return true;
        //return cmd.hasOption("s") || cmd.hasOption("ds"); FIXME
    }

    public String obtainAPIProps() {
        return null;
        //return cmd.getOptionValue("apf"); FIXME
    }
    public String obtainDeserProps() {
        return null;
        //return cmd.getOptionValue("despf"); FIXME
    }
    public String obtainSerProps() {
        return null;
        //return cmd.getOptionValue("serpf"); FIXME
    }
    public String obtainDownProps() {
        return null;
        //return cmd.getOptionValue("dowpf"); FIXME
    }
    public String obtainTotProps() {
        return null;
        //return cmd.getOptionValue("totpf"); FIXME
    }

    public String obtainAnalyzeProps() {
        return null;
        //return cmd.getOptionValue("anapf"); FIXME
    }

    public String obtainPathOption() {
        /*
        return cmd.getOptionValue("path"); FIXME
         */
        return "./database/nytimes_articles_v2";
        //return null;
    }

    public int obtainNumberOption()
    {
        return 50;
        /*
        try
        {
            return Integer.parseInt(cmd.getOptionValue("n")); FIXME
         }
         catch(...)
         {
            return -1;
         }
         */
    }

    public boolean obtainStopWords()
    {
        /*
        if (cmd.getOptionValue("stop").equalsIgnoreCase("false"))
        {
            return false;
        }
         */
        return true;
    }
}