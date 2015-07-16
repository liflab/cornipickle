/*
    Cornipickle, validation of layout bugs in web applications
    Copyright (C) 2015 Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.cornipickle.server;

import java.io.PrintStream;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import ca.uqac.lif.cornipickle.util.AnsiPrinter;

public class Main
{
	/**
	 * Return codes
	 */
	public static final int ERR_OK = 0;
	public static final int ERR_PARSE = 2;
	public static final int ERR_IO = 3;
	public static final int ERR_ARGUMENTS = 4;
	public static final int ERR_RUNTIME = 6;
	public static final int ERR_GRAMMAR = 7;
	public static final int ERR_INPUT = 9;

	/**
	 * Build string to identify versions
	 */
	protected static final String VERSION_STRING = "0.0";
	protected static final String BUILD_STRING = "20150126";

	/**
	 * Default server name
	 */
	protected static String s_defaultServerName = "localhost";

	/**
	 * Default port to listen to
	 */
	protected static int s_defaultPort = 10101;

	/**
	 * Verbosity level for CLI
	 */
	protected static int s_verbosity = 1;

	/**
	 * Main method
	 * @param args Command-line arguments
	 */
	public static void main(String[] args)
	{
		String server_name = s_defaultServerName;
		int server_port = s_defaultPort;
		String serve_path = null;
		final AnsiPrinter stderr = new AnsiPrinter(System.err);
		final AnsiPrinter stdout = new AnsiPrinter(System.out);
		stdout.setForegroundColor(AnsiPrinter.Color.BLACK);
		stderr.setForegroundColor(AnsiPrinter.Color.BLACK);

		// Propertly close print streams when closing the program
		// https://www.securecoding.cert.org/confluence/display/java/FIO14-J.+Perform+proper+cleanup+at+program+termination
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				stderr.close();
				stdout.close();
			}
		}));

		// Parse command line arguments
		Options options = setupOptions();
		CommandLine c_line = setupCommandLine(args, options, stderr);
		assert c_line != null;
		if (c_line.hasOption("verbosity"))
		{
			s_verbosity = Integer.parseInt(c_line.getOptionValue("verbosity"));
		}
		if (s_verbosity > 0)
		{
			showHeader(stdout);
		}
		if (c_line.hasOption("version"))
		{
			stderr.println("(C) 2015 Sylvain Hallé et al., Université du Québec à Chicoutimi");
			stderr.println("This program comes with ABSOLUTELY NO WARRANTY.");
			stderr.println("This is a free software, and you are welcome to redistribute it");
			stderr.println("under certain conditions. See the file LICENSE for details.\n");
			System.exit(ERR_OK);
		}
		if (c_line.hasOption("h"))
		{
			showUsage(options);
			System.exit(ERR_OK);
		}
		if (c_line.hasOption("p"))
		{
			server_port = Integer.parseInt(c_line.getOptionValue("p"));
		}
		if (c_line.hasOption("serve-as"))
		{
			serve_path = c_line.getOptionValue("serve-as");
		}

		// The remaining arguments are the Cornipickle files to read
		CornipickleServer server = new CornipickleServer(server_name, server_port);
		List<String> remaining_args = c_line.getArgList();
		for (String filename : remaining_args)
		{
			stdout.setForegroundColor(AnsiPrinter.Color.BROWN);
			println(stdout, "Reading properties in " + filename, 1);
			server.readProperties(filename);
		}
		if (serve_path != null)
		{
			server.registerCallback(0, new PassthroughCallback(serve_path, "./"));
			println(stdout, "Serving local folder as " + serve_path, 1);
		}

		// Start server
		server.startServer();
		stdout.setForegroundColor(AnsiPrinter.Color.BLUE);
		println(stdout, "Server started on " + server_name + ":" + server_port, 1);

		// Terminate without error
		//System.exit(ERR_OK);
	}

	protected static void println(PrintStream out, String message, int verbosity_level)
	{
		if (verbosity_level >= s_verbosity)
		{
			out.println(message);
		}
	}

	/**
	 * Sets up the options for the command line parser
	 * @return The options
	 */
	private static Options setupOptions()
	{
		Options options = new Options();
		Option opt;
		opt = Option.builder("h")
				.longOpt("help")
				.desc("Display command line usage")
				.build();
		options.addOption(opt);
		opt = Option.builder("s")
				.longOpt("servername")
				.argName("x")
				.hasArg()
				.desc("Set server name or IP address x (default: " + s_defaultServerName + ")")
				.build();
		options.addOption(opt);
		opt = Option.builder("p")
				.longOpt("port")
				.argName("x")
				.hasArg()
				.desc("Listen on port x (default: " + s_defaultPort + ")")
				.build();
		options.addOption(opt);
		opt = Option.builder()
				.longOpt("serve-as")
				.argName("path")
				.hasArg()
				.desc("Serve local folder as path")
				.build();
		options.addOption(opt);
		return options;
	}

	/**
	 * Show the benchmark's usage
	 * @param options The options created for the command line parser
	 */
	private static void showUsage(Options options)
	{
		HelpFormatter hf = new HelpFormatter();
		hf.printHelp("java -jar Cornipickle.jar [options] [file1 [file2 ...]]", options);
	}
	/**
	 * Sets up the command line parser
	 * @param args The command line arguments passed to the class' {@link main}
	 * method
	 * @param options The command line options to be used by the parser
	 * @return The object that parsed the command line parameters
	 */
	private static CommandLine setupCommandLine(String[] args, Options options, PrintStream stderr)
	{
		CommandLineParser parser = new DefaultParser();
		CommandLine c_line = null;
		try
		{
			// parse the command line arguments
			c_line = parser.parse(options, args);
		}
		catch (org.apache.commons.cli.ParseException exp)
		{
			// oops, something went wrong
			stderr.println("ERROR: " + exp.getMessage() + "\n");
			//HelpFormatter hf = new HelpFormatter();
			//hf.printHelp(t_gen.getAppName() + " [options]", options);
			System.exit(ERR_ARGUMENTS);
		}
		return c_line;
	}

	private static void showHeader(PrintStream out)
	{
		out.println("Cornipickle, a web oracle");
		out.println("Version " + VERSION_STRING + ", build " + BUILD_STRING);
	}


}
