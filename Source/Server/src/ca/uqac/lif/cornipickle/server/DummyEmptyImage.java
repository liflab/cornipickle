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

import java.util.HashMap;
import java.util.Map;

import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.cornipickle.Verdict;
import ca.uqac.lif.cornipickle.Interpreter.StatementMetadata;
import ca.uqac.lif.jerrydog.CallbackResponse;
import ca.uqac.lif.jerrydog.RequestCallback;

import com.sun.net.httpserver.HttpExchange;

/**
 * Returns a dummy one-pixel image. This request is used as a means of
 * communication between the JavaScript probe and the interpreter. The
 * request passes as GET parameters an URL-encoded JSON object containing
 * information about the current state of the page. The response is made
 * of a one-pixel PNG image, and a cookie containing a JSON object. This
 * object is retrieved by the JS probe, which uses it to determine whether
 * any elements of the page should be highlighted.
 * <ul>
 * <li>Method: <b>GET</b></li>
 * <li>Name: <tt>/image</tt></li>
 * <li>Input:
 * <ul>
 * <li>A parameter named <tt>contents</tt>, which contains a urlencoded
 * JSON string of the following form:
 * <pre>
 * {
 *   TODO
 * }
 * </pre>
 * </li>
 * </ul>
 * </li>
 * <li>Response: 
 * <ul>
 * <li>Binary data for a one-pixel PNG image</li>
 * <li>Into a cookie named <tt>cornipickle</tt>, a JSON string of the form:
 * <pre>
 * {
 *   "num-true"           : 0,
 *   "num-false"          : 0,
 *   "num-inconclusive"   : 0,
 *   "global-verdict"     : "TRUE",
 *   "highlight-ids"     : [
 *      {
 *        "ids"     : [[0, 1, 2, ...], [3, 4], ...],
 *        "caption" : "Some text"
 *      },
 *      ...   
 *   ]
 * }
 * </pre>
 * The attributes <tt>num-true</tt>, <tt>num-false</tt> and
 * <tt>num-inconclusive</tt> give the number of Cornipickle statements that
 * evaluate to true, false and inconclusive respectively. The attribute
 * <tt>global-verdict</tt> is either "TRUE", "FALSE" or "INCONCLUSIVE",
 * depending on the verdict of each statement.
 * <p>
 * The property <tt>highlight-ids</tt> is a list of structures, each correpsonding
 * to one Cornipickle statement, and having two attributes. Attribute
 * <tt>ids</tt> is a list, each element of which is a list of ids. These
 * represents the groups of elements that could be highlighted in the browser
 * to signal an error. For example, the group <tt>[0, 1, 2, ...]</tt> is
 * a first group of element IDs involved in one error instance of the property.
 * The group <tt>[3, 4]</tt> represents a second group of element IDs involved
 * in another error instance of the same property. Finally, the attribute
 * <tt>caption</tt> corresponds to the metadata <tt>@caption</tt> associated with
 * the property, if any. Note that properties that are not violated will not
 * create such a structure in the response cookie.
 * </li>
 * </ul>
 * </li>
 * </ul>
 * @author Sylvain
 *
 */
class DummyEmptyImage extends DummyImage
{
	public DummyEmptyImage(Interpreter i, CornipickleServer server)
	{
		super(i, RequestCallback.Method.GET, "/image");
		m_server = server;
	}

	@Override
	public CallbackResponse process(HttpExchange t)
	{
		byte[] image_to_return = s_dummyImage;
		Map<StatementMetadata,Verdict> verdicts = new HashMap<StatementMetadata,Verdict>();
		// Create response
		CallbackResponse cbr = new CallbackResponse(t);
		cbr.setHeader("Access-Control-Allow-Origin", "*");
		cbr.setContents(createResponseBody(verdicts, m_interpreter.saveToMemento(), image_to_return));
		cbr.setContentType(CallbackResponse.ContentType.JSON);
		if (!m_server.doesPersistState())
		{
			m_interpreter.clear();
		}
		return cbr;
	}
}
