/*
    Cornipickle, validation of layout bugs in web applications
    Copyright (C) 2015-2016 Sylvain Hallé

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

/**
 * Implementation of the Originator role in the
 * <a href="https://en.wikipedia.org/wiki/Memento_pattern">memento design
 * pattern</a>. The originator is some object that has an internal state,
 * which can be "saved" as a "memento" object. A "caretaker" then
 * performs a modification to the originator. To roll back to the state before
 * the operations, it returns the memento object to the originator.
 * 
 * @param T The type of the originator
 * @param U The type of the memento object
 * 
 * @author sylvain
 *
 */
public interface Originator<T,U>
{
	/**
	 * Saves the state of the originator into a memento
	 * @return The memento
	 */
	public U saveToMemento();
	
	/**
	 * Rolls the originator's state back to what it was when some memento
	 * was produced. 
	 * @param memento The memento
	 * @return A new instance of the originator with the appropriate state
	 */
	public T restoreFromMemento(U memento);
}
