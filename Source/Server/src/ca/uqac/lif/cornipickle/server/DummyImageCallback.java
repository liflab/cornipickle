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

import ca.uqac.lif.cornipickle.Statement;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonParser;
import ca.uqac.lif.cornipickle.json.JsonParser.JsonParseException;
import ca.uqac.lif.httpserver.Cookie;
import ca.uqac.lif.httpserver.InnerFileServer;
import ca.uqac.lif.httpserver.RequestCallback;
import ca.uqac.lif.httpserver.Server;

import com.sun.net.httpserver.HttpExchange;

class DummyImageCallback extends RequestCallback<CornipickleServer>
{
  /**
   * Dummy image made of one white pixel
   */
  static final byte[] s_dummyImage = InnerFileServer.readBytes(
      CornipickleServer.class.getResourceAsStream("resource/dummy-image.png"));
  
  /**
   * Dummy image made of one red pixel
   */
  static final byte[] s_dummyImageRed = InnerFileServer.readBytes(
      CornipickleServer.class.getResourceAsStream("resource/dummy-image-red.png"));
  
  /**
   * Dummy image made of one green pixel
   */  
  static final byte[] s_dummyImageGreen = InnerFileServer.readBytes(
      CornipickleServer.class.getResourceAsStream("resource/dummy-image-green.png"));

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
        m_server.setLastProbeContact();
      }
    }
    // Select the dummy image to send back
    Map<StatementMetadata,Statement.Verdict> verdicts = m_server.m_interpreter.getVerdicts();
    byte[] image_to_return = selectImage(verdicts);
    // Create cookie response
    String cookie_json_string = createResponseCookie(verdicts);
    m_server.addResponseCookie(t, new Cookie("cornipickle", cookie_json_string));
    m_server.sendResponse(t, Server.HTTP_OK, image_to_return, "image/png");
    return true;
  }

  protected static byte[] selectImage(Map<StatementMetadata,Statement.Verdict> verdicts)
  {
    int num_errors = 0;
    byte[] image_out = s_dummyImage;
    for (StatementMetadata key : verdicts.keySet())
    {
      Statement.Verdict v = verdicts.get(key);
      if (v == Statement.Verdict.FALSE)
      {
        num_errors++;
      }
    }
    if (num_errors == 0)
    {
      if (verdicts.isEmpty())
      {
        // Cornipickle has no property to evaluate
      }
      else
      {
        // All's well! All properties evaluate to true 
      }
    }
    else
    {
      // Errors found
      image_out = s_dummyImageRed;
    }
    return image_out;
  }
  
  protected static String createResponseCookie(Map<StatementMetadata,Statement.Verdict> verdicts)
  {
    StringBuilder out = new StringBuilder();
    int num_false = 0;
    int num_true = 0;
    int num_inconclusive = 0;
    Statement.Verdict outcome = Statement.Verdict.TRUE;
    out.append("{");
    for (StatementMetadata key : verdicts.keySet())
    {
      Statement.Verdict v = verdicts.get(key);
      outcome = Statement.threeValuedAnd(outcome, v);
      if (v == Statement.Verdict.FALSE)
      {
        num_false++;
      }
      else if (v == Statement.Verdict.TRUE)
      {
        num_true++;
      } 
      else
      {
        num_inconclusive++;
      }
    }
    out.append("\"global-verdict\" : \"").append(outcome).append("\",");
    out.append("\"num-true\" : ").append(num_true).append(",");
    out.append("\"num-false\" : ").append(num_false).append(",");
    out.append("\"num-inconclusive\" : ").append(num_inconclusive);
    out.append("}");
    return out.toString();
  }

}
