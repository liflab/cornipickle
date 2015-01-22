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

import java.util.LinkedList;

public class JsonPath
{
  public static JsonElement get(JsonElement root, String path)
  {
    path = path.replace(".", "/");
    path = path.replace("[", "/[");
    String[] parts = path.split("/");
    LinkedList<PathElement> out_path = new LinkedList<PathElement>();
    for (String part : parts)
    {
      if (part.startsWith("["))
      {
        // This is a cardinality
        CardinalityPathElement cpe = new CardinalityPathElement();
        part = part.replace("[", "");
        part = part.replace("]", "");
        int card = Integer.parseInt(part);
        cpe.m_card = card;
        out_path.add(cpe);
      }
      else
      {
        // This is a key
        KeyPathElement kpe = new KeyPathElement();
        kpe.m_key = part;
        out_path.add(kpe);
      }
    }
    return get(root, out_path);
  }
  
  public static JsonElement get(JsonElement root, LinkedList<PathElement> path)
  {
    if (path.isEmpty())
    {
      return root;
    }
    LinkedList<PathElement> new_path = new LinkedList<PathElement>(path);
    PathElement first_path_el = new_path.removeFirst();
    if (first_path_el instanceof KeyPathElement && root instanceof JsonMap)
    {
      KeyPathElement kpe = (KeyPathElement) first_path_el;
      JsonMap map = (JsonMap) root;
      JsonElement cursor = map.get(kpe.m_key);
      return get(cursor, new_path);
    }
    else if (first_path_el instanceof CardinalityPathElement && root instanceof JsonList)
    {
      CardinalityPathElement cpe = (CardinalityPathElement) first_path_el;
      JsonList list = (JsonList) root;
      JsonElement cursor = list.get(cpe.m_card);
      return get(cursor, new_path);      
    }
    return null;
  }
  
  public static String getString(JsonElement root, String path)
  {
    JsonElement answer = get(root, path);
    if (answer == null || !(answer instanceof JsonString))
    {
      return null;
    }
    JsonString s_answer = (JsonString) answer;
    return s_answer.stringValue();
  }
  
  public static Number getNumber(JsonElement root, String path)
  {
    JsonElement answer = get(root, path);
    if (answer == null || !(answer instanceof JsonNumber))
    {
      return null;
    }
    JsonNumber s_answer = (JsonNumber) answer;
    return s_answer.numberValue();
  }
  
  public static JsonList getList(JsonElement root, String path)
  {
    JsonElement answer = get(root, path);
    if (answer == null || !(answer instanceof JsonList))
    {
      return null;
    }
    JsonList s_answer = (JsonList) answer;
    return s_answer;
  }
  
  public static JsonMap getMap(JsonElement root, String path)
  {
    JsonElement answer = get(root, path);
    if (answer == null || !(answer instanceof JsonMap))
    {
      return null;
    }
    JsonMap s_answer = (JsonMap) answer;
    return s_answer;
  }
  
  protected static abstract class PathElement
  {
  }
  
  protected static class KeyPathElement extends PathElement
  {
    public String m_key = "";
  }
  
  protected static class CardinalityPathElement extends PathElement
  {
    public int m_card = -1;
  }
}
