package ca.uqac.lif.httpserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Vector;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server implements HttpHandler
{
	/**
	 * Common HTTP response codes
	 */
	public static final int HTTP_OK = 200;
	public static final int HTTP_BAD_REQUEST = 400;
	public static final int HTTP_NOT_FOUND = 404;

	/**
	 * User-agent string
	 */
	protected String m_userAgent = "Server";
	
	/**
	 * Server name, either an IP address or a domain name
	 */
	protected String m_serverName = "localhost";
	
	protected Vector<RequestCallback> m_callbacks;
	
	HttpServer m_server;
	
	public Server()
	{
		super();
		m_callbacks = new Vector<RequestCallback>();
	}
	
	public void startServer(int port)
	{
		try
		{
			m_server = HttpServer.create(new InetSocketAddress(port), 0);
			m_server.createContext("/", this);
			m_server.setExecutor(null); // creates a default executor
			m_server.start();
		}
		catch (IOException e)
		{
			System.err.println("ERROR: cannot instantiate REST interface on port " + port);
			e.printStackTrace();
		} 
	}
	
	public void stopServer()
	{
		m_server.stop(HTTP_OK);
	}
	
	public void setServerName(String name)
	{
		m_serverName = name;
	}
	
	public String getServerName()
	{
		return m_serverName;
	}
	
	public void setUserAgent(String ua)
	{
		m_userAgent = ua;
	}
	
	public void registerCallback(int index, RequestCallback cb)
	{
		m_callbacks.add(index, cb);
	}

	@Override
	public void handle(HttpExchange t) throws IOException
	{
		// Go through registered callbacks
		boolean has_fired = false;
		for (RequestCallback cb : m_callbacks)
		{
			if (cb.fire(t))
			{
				has_fired = cb.process(t);
				if (has_fired)
				{
					System.out.println(t.getRequestURI().getPath());
					break;
				}
			}
		}
		// No callback was triggered: bad request
		if (!has_fired)
		{
			sendResponse(t, HTTP_BAD_REQUEST);
		}
	}
	
	protected void sendResponse(HttpExchange t, int response_code)
	{
		sendResponse(t, response_code, "");
	}
	
	protected void sendResponse(HttpExchange t, int response_code, String contents)
	{
		sendResponse(t, response_code, contents.getBytes());
	}
	
	protected void sendResponse(HttpExchange t, int response_code, byte[] contents)
	{
		sendResponse(t, response_code, contents, "");
	}

	protected void sendResponse(HttpExchange t, int response_code, byte[] contents, String content_type)
	{
		Headers h = t.getResponseHeaders();
		h.add("User-agent", m_userAgent);
		if (content_type != null && !content_type.isEmpty())
		{
			h.add("Content-Type", content_type);
		}
		try
		{
			if (contents == null)
			{
				t.sendResponseHeaders(response_code, 0);	
			}
			else
			{
				t.sendResponseHeaders(response_code, contents.length);
				if (contents.length > 0)
				{
					OutputStream os = t.getResponseBody();
					os.write(contents);
					os.close();
				}
			}
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
