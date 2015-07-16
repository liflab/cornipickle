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
import java.util.Date;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.httpserver.Server;
import ca.uqac.lif.util.FileReadWrite;

public class CornipickleServer extends Server
{
	/**
	 * The Cornipickle interpreter used by this server
	 */
  protected Interpreter m_interpreter;
 
  protected Date m_lastProbeContact;

  public CornipickleServer(String server_name, int port)
  {
    super();
    setServerName(server_name);
    setServerPort(port);
    // Instantiate Cornipickle interpreter
    m_interpreter = new Interpreter();
    // Register callbacks
    registerCallback(new ResetHistory(m_interpreter));
    registerCallback(new AddProperty(m_interpreter));
    registerCallback(new StatusPageCallback(m_interpreter));
    registerCallback(new GetProbe(m_interpreter, m_serverName, m_port));
    registerCallback(new DummyImage(m_interpreter));
    registerCallback(new FileCallback(this));
  }
  
  /**
   * Sets the time of the last contact from JS probe to current time
   */
  public void setLastProbeContact()
  {
  	m_lastProbeContact = new Date();
  }
  
  /**
   * Retrieves moment of last contact from JS probe
   * @return The time of last contact
   */
  public Date getLastProbeContact()
  {
  	return m_lastProbeContact;
  }

  public void readProperties(String filename)
  {
    try
    {
      String corni_file_contents = FileReadWrite.readFile(filename);
      m_interpreter.parseProperties(corni_file_contents);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public static String escapeQuotes(String s)
  {
    return s.replaceAll("\"", "\\\\\"");
  }
}
