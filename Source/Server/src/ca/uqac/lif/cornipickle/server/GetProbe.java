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

import java.util.Set;

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.jerrydog.CallbackResponse;
import ca.uqac.lif.jerrydog.RequestCallback;

import com.sun.net.httpserver.HttpExchange;

/**
 * Retrieves the JavaScript probe associated with the current state of
 * the interpreter
 * <ul>
 * <li>Method: <b>GET</b></li>
 * <li>Name: <tt>/probe</tt></li>
 * <li>Input: nothing</li>
 * <li>Response: JavaScript. Returns a JS file with the code for the
 *   probe to inject in the page.
 * </li>
 * </ul>
 */
class GetProbe extends InterpreterCallback
{
	/**
	 * The template code for the JavaScript probe
	 */
	protected static final String s_probeCode = readProbeCode();

	/**
	 * The template code for the witness
	 */
	protected static final String s_witnessCode = readWitnessCode();

	/**
	 * The server name to generate the probe
	 */
	protected String m_serverName;

	/**
	 * The server port to generate the probe
	 */
	protected int m_serverPort;

	public GetProbe(Interpreter i, String server_name, int server_port)
	{
		this(i, server_name, server_port, false);
	}

	public GetProbe(Interpreter i, String server_name, int server_port, boolean minify)
	{
		super(i, RequestCallback.Method.GET, "/probe");
		m_serverName = server_name;
		m_serverPort = server_port;
	}

	@Override
	public CallbackResponse process(HttpExchange t)
	{
		String probe_code = generateProbeCode();
		return new CallbackResponse(t, CallbackResponse.HTTP_OK, probe_code, CallbackResponse.ContentType.JS);
	}

	protected String generateProbeCode()
	{
		String probe_code = null;
		//String witness_code = PackageFileReader.readPackageFile(m_server.getResourceAsStream(m_server.getResourceFolderName() + "/witness.inc.html"));
		probe_code = new String(s_probeCode);
		probe_code = probe_code.replace("%%WITNESS_CODE%%", escapeString(s_witnessCode));
		probe_code = probe_code.replace("%%SERVER_NAME%%", m_serverName + ":" + m_serverPort);
		// Add attributes to include
		Set<String> attributes = m_interpreter.getAttributes();
		StringBuilder attribute_string = new StringBuilder();
		for (String att : attributes)
		{
			attribute_string.append("\"").append(att).append("\",");
		}
		probe_code = probe_code.replace("/*%%ATTRIBUTE_LIST%%*/", attribute_string.toString());
		Set<String> tags = m_interpreter.getTagNames();
		StringBuilder tag_string = new StringBuilder();
		for (String tag : tags)
		{
			tag_string.append("\"").append(tag).append("\",");
		}
		probe_code = probe_code.replace("/*%%TAG_LIST%%*/", tag_string.toString());
		return probe_code;    
	}

	/**
	 * Escapes a string for JavaScript
	 * @param s The string
	 * @return The escaped String
	 */
	protected static String escapeString(String s)
	{
		s = s.replaceAll("\"", "\\\\\"");
		s = s.replaceAll("\n", "\\\\n");
		s = s.replaceAll("\r", "\\\\r");
		return s;
	}

	protected static String readProbeCode()
	{
		return PackageFileReader.readPackageFile(CornipickleServer.class, "resource/probe.inc.js");
	}

	protected static String readWitnessCode()
	{
		return PackageFileReader.readPackageFile(CornipickleServer.class, "resource/witness.inc.html");
	}  
}
