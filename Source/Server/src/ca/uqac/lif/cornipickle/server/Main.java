/*
    Cornipickle, validation of layout bugs in web applications
    Copyright (C) 2015-2018 Sylvain Hallé

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

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Level;

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.util.AnsiPrinter;
import ca.uqac.lif.util.CliParser;
import ca.uqac.lif.util.CliParser.Argument;
import ca.uqac.lif.util.CliParser.ArgumentMap;

public class Main
{
	/**
	 * Exit codes
	 */
	public static final int ERR_OK = 0;
	public static final int ERR_PARSE = 2;
	public static final int ERR_IO = 3;
	public static final int ERR_ARGUMENTS = 4;
	public static final int ERR_RUNTIME = 6;
	public static final int ERR_GRAMMAR = 7;
	public static final int ERR_INPUT = 9;

	/**
	 * Major version number
	 */
	public static final int s_majorVersion = 1;
	
	/**
	 * Minor version number
	 */
	public static final int s_minorVersion = 3;
	
	/**
	 * Revision version number
	 */
	public static final int s_revisionVersion = 4;

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

	public enum PlatformType { web, android_native };

	public static PlatformType s_platform = PlatformType.web;

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

		// Properly close print streams when closing the program
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
		CliParser parser = setupCommandLine();
		ArgumentMap c_line = parser.parse(args);
		if (c_line == null)
		{
			// oops, something went wrong
			stderr.println("ERROR parsing command line arguments\n");
			System.exit(ERR_ARGUMENTS);
		}
		assert c_line != null;
		if (c_line.hasOption("verbosity"))
		{
			s_verbosity = Integer.parseInt(c_line.getOptionValue("verbosity"));
		}

		if (c_line.hasOption("version"))
		{
			stderr.println("(C) 2015-2018 Laboratoire d'informatique formelle");
			stderr.println("This program comes with ABSOLUTELY NO WARRANTY.");
			stderr.println("This is a free software, and you are welcome to redistribute it");
			stderr.println("under certain conditions. See the file LICENSE for details.\n");
			System.exit(ERR_OK);
		}
		if (c_line.hasOption("help"))
		{
			showUsage(parser, stderr);
			System.exit(ERR_OK);
		}
		if (c_line.hasOption("port"))
		{
			server_port = Integer.parseInt(c_line.getOptionValue("port"));
		}
		if (c_line.hasOption("serve-as"))
		{
			serve_path = c_line.getOptionValue("serve-as");
		}
		if(c_line.hasOption("servername"))
		{
			server_name = c_line.getOptionValue("servername");
		}
		if(c_line.hasOption("a")){

			s_platform=PlatformType.android_native;
		}
		if (s_verbosity > 0)
		{
			showHeader(stdout);
		}

		// The remaining arguments are the Cornipickle files to read
		CornipickleServer server = new CornipickleServer(server_name, server_port);
		// When called from the command-line (and hence working in stand-alone
		// mode), the interpreter persists its state between requests by default
		if (!c_line.hasOption("memento"))
		{
			server.persistState(true);
		}
		else
		{
			println(stdout, "Interpreter state is not kept between calls", 1);
		}
		List<String> remaining_args = c_line.getOthers();
		for (String filename : remaining_args)
		{
			println(stdout, "Reading properties in " + filename, 1);
			server.readProperties(filename);
		}
		if (serve_path != null)
		{
			PassthroughCallback ptc = new PassthroughCallback(serve_path, "./");
			ptc.send404(false);
			// This is tricky. We must put this callback *after* the probe, add, etc.
			// callbacks, but before the callback for the "resource" folder, which
			// normally is the last. Hence we put it in next-to-last position.
			server.registerCallback(-1, ptc);
			println(stdout, "Serving local folder as " + serve_path, 1);
		}

		// Start server
		try 
		{
			server.startServer();
		} 
		catch (IOException e) 
		{
			Interpreter.LOGGER.log(Level.SEVERE, e.toString());
			System.exit(ERR_IO);
		}
		println(stdout, "Server started on " + server_name + ":" + server_port, 1);
	}

	protected static void println(PrintStream out, String message, int verbosity_level)
	{
		if (verbosity_level >= s_verbosity)
		{
			out.println(message);
		}
	}

	/**
	 * Show the benchmark's usage
	 * @param options The options created for the command line parser
	 */
	private static void showUsage(CliParser parser, PrintStream stderr)
	{
		parser.printHelp("java -jar cornipickle.jar [options] [file1 [file2 ...]]", stderr);
	}
	/**
	 * Sets up the command line parser
	 * @return The object that parses the command line parameters
	 */
	private static CliParser setupCommandLine()
	{
		CliParser parser = new CliParser();
		parser.addArgument(new Argument().withShortName("h")
				.withLongName("help")
				.withDescription("Display command line usage"));
		parser.addArgument(new Argument().withShortName("s")
				.withLongName("servername")
				.withArgument("x")
				.withDescription("Set server name or IP address x (default: " + s_defaultServerName + ")"));
		parser.addArgument(new Argument().withShortName("p")
				.withLongName("port")
				.withArgument("x")
				.withDescription("Listen on port x (default: " + s_defaultPort + ")"));
		parser.addArgument(new Argument()
				.withLongName("serve-as")
				.withArgument("path")
				.withDescription("Serve local folder as path"));
		parser.addArgument(new Argument().withShortName("a")
				.withLongName("android")
				.withDescription("Change platform type to Android"));
		parser.addArgument(new Argument()
				.withLongName("memento")
				.withDescription("Pass interpreter state through queries as a memento"));
		return parser;
	}

	private static void showHeader(PrintStream out)
	{
		String platform_type = "A web GUI";
		if (s_platform == PlatformType.android_native)
		{
			platform_type = "An Android GUI";
		}
		out.println("Cornipickle v" + formatVersion() + " - "+ platform_type +" oracle");
		out.println("(C) 2015-2020 Laboratoire d'informatique formelle");
		out.println("Université du Québec à Chicoutimi, Canada");
	}

	public static String formatVersion()
	{
		String out = "" + s_majorVersion + "." + s_minorVersion;
		if (s_revisionVersion > 0)
		{
			out += "." + s_revisionVersion;
		}
		return out;
	}
}
