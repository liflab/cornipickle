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

import java.io.IOException;
import java.net.URI;

import ca.uqac.lif.cornipickle.util.PackageFileReader;
import ca.uqac.lif.httpserver.RequestCallback;
import ca.uqac.lif.httpserver.Server;

import com.sun.net.httpserver.HttpExchange;

class ProbeCallback extends RequestCallback<CornipickleServer>
{
  public ProbeCallback(CornipickleServer s)
  {
    super(s);
  }

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
      String witness_code = PackageFileReader.readPackageFile(m_server.getResourceAsStream(CornipickleServer.m_resourceFolder + "/witness.js"));
      String probe_code = PackageFileReader.readPackageFile(m_server.getResourceAsStream(CornipickleServer.m_resourceFolder + "/probe.js"));
      probe_code = probe_code.replace("%%WITNESS_CODE%%", CornipickleServer.escapeString(witness_code));
      probe_code = probe_code.replace("%%SERVER_NAME%%", m_server.getServerName() + ":" + CornipickleServer.s_defaultPort);
      m_server.sendResponse(t, Server.HTTP_OK, probe_code, "application/javascript");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return true;
  }
}
