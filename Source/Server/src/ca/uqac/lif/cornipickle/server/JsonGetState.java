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

import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.serialization.CornipickleDeflateSerializer;
import ca.uqac.lif.jerrydog.CallbackResponse;
import ca.uqac.lif.jerrydog.RequestCallback;

import com.sun.net.httpserver.HttpExchange;

class JsonGetState extends InterpreterCallback
{
	/**
	 * A reference to the server. This is needed as the callback
	 * queries information about the server's state.
	 */
	protected CornipickleServer m_server;
  
  public JsonGetState(Interpreter i, CornipickleServer s)
  {
    super(i, RequestCallback.Method.GET, "/state");
    m_server = s;
  }

  @Override
  public CallbackResponse process(HttpExchange t)
  {
    StringBuilder state = new StringBuilder();
    //GsonBuilder builder = new GsonBuilder();
    CornipickleDeflateSerializer ser = new CornipickleDeflateSerializer();
    String je;
	try 
	{
		je = ser.serialize(m_interpreter);
		state.append(je);
	} 
	catch (SerializerException e) 
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    CallbackResponse out = new CallbackResponse(t, CallbackResponse.HTTP_OK, state.toString(), CallbackResponse.ContentType.JSON);
    out.disableCaching();
    return out;
  }
} 