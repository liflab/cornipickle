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
package ca.uqac.lif.httpserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;

public class InnerFileServer extends Server
{
  public static String m_resourceFolder;

  protected Class<? extends InnerFileServer> m_referenceClass;

  public InnerFileServer()
  {
    super();
    m_resourceFolder = "resource";
    registerCallback(0, new InnerFileCallback(this));
    m_referenceClass = this.getClass();
  }

  protected InnerFileServer(Class<? extends InnerFileServer> c)
  {
    super();
    m_resourceFolder = "resource";
    registerCallback(0, new InnerFileCallback(this));
    m_referenceClass = c;
  }
  
  public InputStream getResourceAsStream(String path)
  {
    return m_referenceClass.getResourceAsStream(path);
  }

  public static class InnerFileCallback extends CachedRequestCallback<InnerFileServer>
  {
    public InnerFileCallback(InnerFileServer s)
    {
      super(s);
    }

    @Override
    public boolean fire(HttpExchange t)
    {
      return true;
    }

    @Override
    public boolean serve(HttpExchange t)
    {
      URI uri = t.getRequestURI();
      int response_code = HTTP_OK;
      // Get file
      InputStream is = m_server.getResourceAsStream(m_resourceFolder + uri.getPath());
      if (is != null)
      {
        byte[] file_contents = readBytes(is);
        m_server.sendResponse(t, response_code, file_contents);
      }
      else
      {
        // Resource not found: send 404
        m_server.sendResponse(t, HTTP_NOT_FOUND);
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
}
