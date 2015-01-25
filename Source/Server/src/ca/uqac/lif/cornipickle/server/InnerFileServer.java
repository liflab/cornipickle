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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class InnerFileServer extends Server
{
	public static String m_resourceFolder;
	
	protected Class<? extends InnerFileServer> m_referenceClass;
	
	public InnerFileServer()
	{
		super();
		m_resourceFolder = "resource";
		registerCallback(0, this.new InnerFileCallback());
		m_referenceClass = this.getClass();
	}
	
	protected InnerFileServer(Class<? extends InnerFileServer> c)
	{
		super();
		m_resourceFolder = "resource";
		registerCallback(0, this.new InnerFileCallback());
		m_referenceClass = c;
	}
	
	public class InnerFileCallback extends RequestCallback
	{

		@Override
		public boolean fire(HttpExchange t)
		{
			return true;
		}

		@Override
		public boolean process(HttpExchange t)
		{
			URI uri = t.getRequestURI();
			int response_code = HTTP_OK;
			// Get file
			InputStream is = m_referenceClass.getResourceAsStream(m_resourceFolder + uri.getPath());
			if (is != null)
			{
				byte[] file_contents = readBytes(is);
				sendResponse(t, response_code, file_contents);
			}
			else
			{
				// Resource not found: send 404
				sendResponse(t, HTTP_NOT_FOUND);
			}
			return true;
		}
	}
	
	public static byte[] readBytes(InputStream is)
	{
		int nRead;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		byte[] data = new byte[2048];
		try
		{
			while ((nRead = is.read(data, 0, data.length)) != -1)
			{
				buffer.write(data, 0, nRead);
			}
			buffer.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return buffer.toByteArray();
	}


	/**
	 * Convenience method to transform a GET query into a map of
	 * attribute-value pairs. For example, given an URI object
	 * representing the URL "http://abc.com/xyz?a=1&b=2", the method
	 * will return an object mapping "a" to "1" and "b" to "2".
	 * @param u The URI to process
	 * @return A map of attribute-value pairs
	 */
	public static Map<String,String> uriToMap(URI u)
	{
		Map<String,String> out = new HashMap<String,String>();
		String query = u.getQuery();
		if (query == null)
			return out;
		String[] pairs = query.split("&");
		for (String pair : pairs)
		{
			String[] av = pair.split("=");
			String att = av[0];
			String val;
			if (av.length >= 1)
				val = av[1];
			else
				val = "";
			out.put(att, val);
		}
		return out;
	} 

	/**
	 * Convenience method to put the contents of an InputStream into
	 * a String object. As one can see from the code, this is one
	 * prime example of Java verbosity!
	 * @param is The InputStream to read from
	 * @return The stream's contents; if any error (e.g. I/O) occurs, the
	 * method returns an empty string.
	 */
	public static String streamToString(InputStream is)
	{
		final String CRLF = System.getProperty("line.separator");
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line;
		try
		{
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null)
			{
				sb.append(line).append(CRLF);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (br != null)
			{
				try
				{
					br.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	} 
}
