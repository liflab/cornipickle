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
package ca.uqac.lif.cornipickle;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.net.URI;
import java.net.URLDecoder;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonParser;
import ca.uqac.lif.cornipickle.json.JsonParser.JsonParseException;
import ca.uqac.lif.cornipickle.server.InnerFileServer;
import ca.uqac.lif.cornipickle.server.RequestCallback;
import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.util.FileReadWrite;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

public class CornipickleServer extends InnerFileServer
{
	protected Interpreter m_interpreter;
	
	protected static int s_defaultPort = 10101;

	static byte[] s_dummyImage = readBytes(CornipickleServer.class.getResourceAsStream("resource/dummy-image.png"));

	public CornipickleServer()
	{
		super();
		// Instantiate Cornipickle interpreter
		m_interpreter = new Interpreter();
		// Update class reference
		m_referenceClass = this.getClass();
		// Register callbacks
		registerCallback(0, new DummyImageCallback());
		registerCallback(1, new StatusPageCallback());
		registerCallback(2, new ProbeCallback());
	}

	public void readProperties(String filename)
	{
		try
		{
			String corni_file_contents = FileReadWrite.readFile(filename);
			m_interpreter.parseProperties(corni_file_contents);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		CornipickleServer server = new CornipickleServer();
		server.setServerName(args[0]);
		server.readProperties(args[1]);
		server.startServer(s_defaultPort);
	}
	
	protected class ProbeCallback extends RequestCallback
	{
		@Override
		public boolean fire(HttpExchange t)
		{
			URI u = t.getRequestURI();
			String path = u.getPath();
			return path.compareTo("/probe") == 0;
		}
		
		@Override
		public boolean process(HttpExchange t)
		{
			try
			{
				String witness_code = PackageFileReader.readPackageFile(m_referenceClass.getResourceAsStream(m_resourceFolder + "/witness.js"));
				String probe_code = PackageFileReader.readPackageFile(m_referenceClass.getResourceAsStream(m_resourceFolder + "/probe.js"));
				probe_code = probe_code.replace("%%WITNESS_CODE%%", escapeString(witness_code));
				probe_code = probe_code.replace("%%SERVER_NAME%%", getServerName() + ":" + s_defaultPort);
				sendResponse(t, HTTP_OK, probe_code);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return true;
		}
	}
	
	protected class DummyImageCallback extends RequestCallback
	{
		@Override
		public boolean fire(HttpExchange t)
		{
			URI u = t.getRequestURI();
			String path = u.getPath();
			return path.compareTo("/image") == 0;
		}

		@Override
		public boolean process(HttpExchange t)
		{
			URI uri = t.getRequestURI();
			Map<String,String> attributes = uriToMap(uri); 

			// Extract JSON from URL string
			String json_encoded = attributes.get("contents");
			if (json_encoded != null)
			{
				JsonElement j = null;
				// Parse JSON
				try
				{
					String json_decoded = URLDecoder.decode(json_encoded, "UTF-8");
					j = JsonParser.parse(json_decoded);
					System.out.println("JSON received");
					//System.out.println(json_decoded);
				}
				catch (JsonParseException e)
				{
					e.printStackTrace();
				} 
				catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
				if (j != null)
				{
					m_interpreter.evaluateAll(j);
				}
			}
			// Whatever happens, serve the dummy image
			sendResponse(t, HTTP_OK, s_dummyImage);
			return true;
		}
	}

	protected class StatusPageCallback extends RequestCallback
	{
		@Override
		public boolean fire(HttpExchange t)
		{
			URI u = t.getRequestURI();
			String path = u.getPath();
			return path.compareTo("/status") == 0;
		}

		@Override
		public boolean process(HttpExchange t)
		{
			StringBuilder page = new StringBuilder();
			page.append("<!DOCTYPE html>\n");
			page.append("<html>\n");
			page.append("<head>\n");
			page.append("<title>Cornipickle Status</title>\n");
			page.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"screen.css\" />\n");
			page.append("</head>\n");
			page.append("<body>\n");
			page.append("<h1>Cornipickle Status</h1>");
			Map<String,Boolean> verdicts = m_interpreter.getVerdicts();
			page.append("<ul class=\"verdicts\">\n");
			for (String key : verdicts.keySet())
			{
				boolean v = verdicts.get(key);
				if (v)
				{
					page.append("<li class=\"true\">").append(key).append("</li>");
				}
				else
				{
					page.append("<li class=\"false\">").append(key).append("</li>");
				}
			}
			page.append("</ul>\n");
			page.append("<hr />\n");
			Date d = new Date();
			page.append(d);
			page.append("</body>\n</html>\n");
			String page_string = page.toString();
			// Disable caching on the client
			Headers h = t.getResponseHeaders();
			h.add("Pragma", "no-cache");
			h.add("Cache-Control", "no-cache, no-store, must-revalidate");
			h.add("Expires", "0"); 
			sendResponse(t, HTTP_OK, page_string);
			return true;
		}
	}	
	
	/**
	 * Escapes a string for JavaScript
	 * @param s The string
	 * @return
	 */
	protected static String escapeString(String s)
	{
		s = s.replaceAll("\"", "\\\\\"");
		s = s.replaceAll("\n", "\\\\n");
		s = s.replaceAll("\r", "\\\\r");
		return s;
	}
}
