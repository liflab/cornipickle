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

import ca.uqac.lif.util.EmptyException;

public abstract class JsonParser
{
  public abstract JsonElement parse(String s) throws JsonParseException;
  
  public static class JsonParseException extends EmptyException
  {
    /**
     * Dummy UID
     */
    private static final long serialVersionUID = 1L;

    public JsonParseException(String message)
    {
      super(message);
    }

  }
}
