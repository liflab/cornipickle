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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.serialization.CornipickleDeflateSerializer;
import ca.uqac.lif.jerrydog.CallbackResponse;
import ca.uqac.lif.jerrydog.RequestCallback;


import com.sun.net.httpserver.HttpExchange;

class JsonPutState extends InterpreterCallback
{
	/**
	 * A reference to the server. This is needed as the callback
	 * queries information about the server's state.
	 */
	protected CornipickleServer m_server;

	public JsonPutState(Interpreter i, CornipickleServer s)
	{
		super(i, RequestCallback.Method.POST, "/state");
		m_server = s;
	}

	@Override
	public CallbackResponse process(HttpExchange t)
	{
		CallbackResponse response = new CallbackResponse(t, CallbackResponse.HTTP_OK, "{}", CallbackResponse.ContentType.JSON);
		response.disableCaching();

		// Read request parameters
		Map<String,String> params = getParameters(t);

		// Try to decode and parse it
		String props = params.get("state");
		try {
			props = URLDecoder.decode(props,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (props != null && !props.isEmpty())
		{
			System.out.println("BEFORE INT");
			Interpreter i = null;
			try 
			{
				CornipickleDeflateSerializer ser = new CornipickleDeflateSerializer();
				i = (Interpreter) ser.deserializeAs(props, Interpreter.class);
			}
			catch (SerializerException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("ALL OK");
			if (i != null)
			{
				m_server.setInterpreter(i);
				return response;
			}
		}
		// Baaad request
		response.setCode(CallbackResponse.HTTP_BAD_REQUEST);
		return response;
	}
} 