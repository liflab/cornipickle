package ca.uqac.lif.httpserver;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public abstract class RestCallback<T extends Server> extends RequestCallback<T>
{
	/**
	 * The HTTP method that this callback listens to
	 */
	protected Method m_method;
	
	/**
	 * The path that this callback listens to
	 */
	protected String m_path;
	
	/**
	 * Creates a REST callback
	 * @param s The server attached to this callback
	 * @param m The HTTP method this callback listens to
	 * @param path The path this callback listens to
	 */
	public RestCallback(T s, Method m, String path)
	{
		super(s);
		m_method = m;
		m_path = path;
	}

	@Override
	public final boolean fire(HttpExchange t)
	{
    URI u = t.getRequestURI();
    String path = u.getPath();
    String method = t.getRequestMethod();
    return (method.compareToIgnoreCase(methodToString(m_method)) == 0) 
        && path.compareTo(m_path) == 0;
	}

	public Map<String,String> getParameters(HttpExchange t)
	{
		String data = null;
		if (m_method == Method.GET)
		{
	    // Read GET data
			URI u = t.getRequestURI();
			data = u.getQuery();
		}
		else
		{
	    // Read POST data
	    InputStream is_post = t.getRequestBody();
	    data = Server.streamToString(is_post);
		}
    Map<String,String> params = Server.queryToMap(data);
    return params;
	}	
}
