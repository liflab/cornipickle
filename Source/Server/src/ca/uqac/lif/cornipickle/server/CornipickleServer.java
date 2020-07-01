/*
    Cornipickle, validation of layout bugs in web applications
    Copyright (C) 2015-2018 Sylvain Hallé

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.jerrydog.RequestCallback;
import ca.uqac.lif.jerrydog.Server;

public class CornipickleServer extends Server
{
	/**
	 * The Cornipickle interpreter used by this server
	 */
  protected Interpreter m_interpreter;
 
  protected Date m_lastProbeContact;
  
  /**
   * A list of notifications to call whenever the interpreter changes its state
   */
  protected List<InterpreterNotification> m_notifications;
  
  /**
   * Whether to persist the state of the interpreter between calls
   */
  protected boolean m_persistState = false;

  public CornipickleServer(String server_name, int port)
  {
    super();
    setServerName(server_name);
    setServerPort(port);
    // Instantiate Cornipickle interpreter
    m_interpreter = new Interpreter();
    m_notifications = new ArrayList<InterpreterNotification>();
    // Register callbacks
    registerCallback(new ResetHistory(m_interpreter));
    registerCallback(new AddProperty(m_interpreter, this));
    registerCallback(new AddPropertyMobile(m_interpreter, this));
    registerCallback(new StatusPageCallback(m_interpreter, this));
    registerCallback(new GetProbe(m_interpreter, m_serverName, m_port));
    registerCallback(new DummyEmptyImage(m_interpreter, this));
    registerCallback(new DummyImage(m_interpreter, this));
    registerCallback(new DummyImageMobile(m_interpreter, this));
    registerCallback(new PreEvaluation(m_interpreter, this));
    registerCallback(new JsonGetState(m_interpreter, this));
    registerCallback(new JsonPutState(m_interpreter, this));
    registerCallback(new FiddleCallback());
    registerCallback(new FileCallback(this));
  }
  
  	/**
	 * Sets the timeout (in ms) to give the time for the page to load
	 * @param timeout The timeout
	 */
	public void setTimeout(int timeout)
	{
		m_interpreter.setTimeout(timeout);
	}
  
  /**
   * Sets whether to persist the state of the interpreter between calls
   * @param b Set to {@code true} to persist state, {@code false}
   * otherwise
   */
  public void persistState(boolean b)
  {
	  m_persistState = b;
  }
  
  /**
   * Returns whether the server persists the state of the interpreter
   * between calls
   * @return {@code true} if state is kept, {@code false} otherwise
   */
  public boolean doesPersistState()
  {
	  return m_persistState;
  }
  
  /**
   * Registers a new notification for the interpreter
   * @param in The notification to add
   */
  public void registerNotification(InterpreterNotification in)
  {
    m_notifications.add(in);
  }
  
  /**
   * Sets the interpreter associated with this server. This will discard
   * the current interpreter and notify all registered server callbacks
   * of the change.
   * @param i The new interpreter
   */
  public void setInterpreter(Interpreter i)
  {
    m_interpreter = i;
    // Must notify all callbacks that the reference to the interpreter
    // has changed
    for (RequestCallback cb : m_callbacks)
    {
      if (cb instanceof InterpreterCallback)
      {
        ((InterpreterCallback) cb).setInterpreter(i);
      }
    }
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
      String corni_file_contents = Interpreter.readFile(filename);
      m_interpreter.parseProperties(corni_file_contents);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
  }
  
  public static String escapeQuotes(String s)
  {
    return s.replaceAll("\"", "\\\\\"");
  }
}
