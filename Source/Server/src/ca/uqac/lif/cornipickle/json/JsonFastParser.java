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
package ca.uqac.lif.cornipickle.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * JSON parser using the <a href="https://code.google.com/p/json-simple/">json-simple
 * library</a> in the background.
 * This implementation is expected to perform much faster than
 * the parser based on Bullwinkle.
 * @author sylvain
 *
 */
public class JsonFastParser extends JsonParser
{
  protected static JSONParser s_parser = new JSONParser();

  @Override
  public JsonElement parse(String s) throws JsonParseException
  {
    Object obj = null;
    try
    {
      obj = s_parser.parse(s);
    }
    catch (ParseException e)
    {
      throw new JsonParseException(e.toString());
    }
    if (obj != null)
    {
      return wrap(obj);
    }
    return null;
  }
  
  /**
   * Converts a JSONObject to a JsonElement. This is but an
   * inelegant bridge between Cornipickle's own objects for
   * handling JSON, and the ones produced by the JSONParser.
   * @param obj The JSONObject
   * @return The corresponding JsonElement
   */
  protected static JsonElement wrap(Object obj)
  {
    JsonElement out = null;
    if (obj instanceof JSONObject)
    {
      JSONObject o = (JSONObject) obj;
      JsonMap to_out = new JsonMap();
      for (Object key : o.keySet())
      {
        if (key instanceof String)
        {
          String s_key = (String) key;
          to_out.put(s_key, wrap(o.get(key)));
        }
      }
      out = to_out;
    }
    else if (obj instanceof JSONArray)
    {
      JsonList to_out = new JsonList();
      for (Object list_o : (JSONArray) obj)
      {
        to_out.add(wrap(list_o));
      }
      out = to_out;
    }
    else if (obj instanceof String)
    {
      out = new JsonString((String) obj);
    }
    else if (obj instanceof Number)
    {
      out = new JsonNumber((Number) obj);
    }
    return out;
  }

}
