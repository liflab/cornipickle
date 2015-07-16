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

public class JsonNumber extends JsonElement
{
  protected Number m_number;
  
  public JsonNumber(Number n)
  {
    super();
    m_number = n;
  }
  
  public Number numberValue()
  {
    return m_number;
  }
  
  @Override
  public String toString(String indent, boolean compact)
  {
    return m_number.toString();
  }
  
  public static boolean isNumeric(String str)  
  {  
    try  
    {  
      Double.parseDouble(str);  
    }  
    catch(NumberFormatException nfe)  
    {  
      return false;  
    }  
    return true;  
  }
  
  public static boolean isNumeric(JsonString str)  
  {
    return isNumeric(str.stringValue());
  }
  
}
