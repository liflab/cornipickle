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
import java.net.URI;
import java.net.URLDecoder;
import java.util.Map;

import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonParser;
import ca.uqac.lif.cornipickle.json.JsonParser.JsonParseException;
import ca.uqac.lif.httpserver.InnerFileServer;
import ca.uqac.lif.httpserver.RequestCallback;
import ca.uqac.lif.httpserver.Server;

import com.sun.net.httpserver.HttpExchange;

class DummyImageCallback extends RequestCallback<CornipickleServer>
{
  static final byte[] s_dummyImage = InnerFileServer.readBytes(CornipickleServer.class.getResourceAsStream("resource/dummy-image.png"));
  
  public DummyImageCallback(CornipickleServer s)
  {
    super(s);
  }

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
    Map<String,String> attributes = Server.uriToMap(uri); 

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
        m_server.m_interpreter.evaluateAll(j);
      }
    }
    // Whatever happens, serve the dummy image
    m_server.sendResponse(t, Server.HTTP_OK, s_dummyImage, "image/png");
    return true;
  }
}
