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

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.jerrydog.RequestCallback;
import ca.uqac.lif.jerrydog.RestCallback;

/**
 * Server callback that interacts with a Cornipickle interpreter
 * @author Sylvain
 *
 */
public abstract class InterpreterCallback extends RestCallback
{
	/**
	 * The interpreter this callback interacts with
	 */
	protected Interpreter m_interpreter;
	
	public InterpreterCallback(Interpreter i, RequestCallback.Method m, String path)
	{
		super(m, path);
		m_interpreter = i;
	}
	
	/**
	 * Sets the interpreter for this callback
	 * @param i The interpreter
	 */
	public void setInterpreter(Interpreter i)
	{
	  m_interpreter = i;
	}
}
