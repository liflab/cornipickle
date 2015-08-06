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

import java.util.List;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Headers;

import ca.uqac.lif.httpserver.CallbackResponse;
import ca.uqac.lif.httpserver.InnerFileCallback;
import ca.uqac.lif.httpserver.InnerFileServer;

public class PassthroughCallback extends InnerFileCallback
{
	/**
	 * The remote folder exposing the files
	 */
	protected String m_serverFolder;
	
	/**
	 * The local folder corresponding to that path
	 */
	protected String m_localFolder;
	
	/**
	 * The symbol for separating directories (OS-dependent)
	 */
	//protected static final String s_slash = System.getProperty("file.separator");
	protected static final String s_slash = "/";
	
	public PassthroughCallback(String server_folder, String local_folder)
	{
		super(local_folder, PassthroughCallback.class);
		if (!server_folder.startsWith(s_slash))
		{
			server_folder = s_slash + server_folder;
		}
		m_serverFolder = server_folder;
		m_localFolder = local_folder;
	}

	@Override
	public boolean fire(HttpExchange t)
	{
		URI uri = t.getRequestURI();
		String path = uri.getPath();
		return path.startsWith(m_serverFolder);
	}

	@Override
	public CallbackResponse process(HttpExchange t)
	{
		CallbackResponse response = new CallbackResponse(t);
		//Give the right content-type to the browser by giving it what it's looking for
		Headers headers = t.getRequestHeaders();
		String accept_Header = headers.get("Accept").get(0);
		response.setContentType(accept_Header.split(",")[0]);
		
		URI uri = t.getRequestURI();
		String path = uri.getPath();
		String relative_path = path.substring(m_serverFolder.length());
		String local_path = m_localFolder + relative_path;
		File f = new File(local_path);
		FileInputStream fis;
		try
		{
			fis = new FileInputStream(f);
			byte[] contents = InnerFileServer.readBytes(fis);
			response.setContents(contents);
		}
		catch (FileNotFoundException e)
		{
			response.setCode(CallbackResponse.HTTP_NOT_FOUND);
		}
		return response;
	}

}
