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

import java.io.IOException;
import java.util.Map;

import ca.uqac.lif.cornipickle.CornipickleParser.ParseException;
import ca.uqac.lif.cornipickle.json.JsonElement;
import ca.uqac.lif.cornipickle.json.JsonParser;
import ca.uqac.lif.cornipickle.json.JsonParser.JsonParseException;
import ca.uqac.lif.util.FileReadWrite;

public class Interpreter
{
  public static void main(String[] args) throws IOException, JsonParseException, ParseException
  {
    String corni_filename = args[0];
    String json_filename = args[1];
    String corni_file_contents = FileReadWrite.readFile(corni_filename);
    String json_file_contents = FileReadWrite.readFile(json_filename);
    JsonElement jse = JsonParser.parse(json_file_contents);
    CornipickleParser parser = new CornipickleParser();
    parser.parseProperties(corni_file_contents);
    Map<String,Boolean> verdicts = parser.evaluateAll(jse);
    System.out.println(verdicts);
  }
}
