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
package ca.uqac.lif.cornipickle;

public abstract class LanguageElement
{
  public abstract String toString(String indent);
  
  /**
   * Prefix traversal of the parse tree by a visitor
   * @param visitor The visitor
   */
  public abstract void prefixAccept(LanguageElementVisitor visitor);
  
  /**
   * Postfix traversal of the parse tree by a visitor
   * @param visitor The visitor
   */
  public abstract void postfixAccept(LanguageElementVisitor visitor);
  
  @Override
  public final String toString()
  {
    return toString("");
  }
}
