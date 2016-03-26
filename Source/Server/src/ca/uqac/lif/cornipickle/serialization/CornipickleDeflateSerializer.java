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
package ca.uqac.lif.cornipickle.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.xml.bind.DatatypeConverter;

import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.cornipickle.Interpreter;

/**
 * Serializer for the Cornipickle interpreter. It calls a JSON serializer
 * in the background, but adds a deflating/inflating step (with library
 * zlib) and base64 encoding to the serialization to make it more compact.
 * @author sylvain
 *
 */
public class CornipickleDeflateSerializer extends CornipickleSerializer
{
	protected static final transient int s_byteArraySize = 1024;

	public CornipickleDeflateSerializer()
	{
		super();
	}

	@Override
	public String serializeAs(Object o, Class<?> clazz) throws SerializerException
	{
		String je_string = super.serializeAs(o, clazz);
		byte[] output_bytes = new byte[0];
		try
		{
			byte[] je_bytes = je_string.getBytes("UTF-8");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(je_bytes.length);
			// Compress the bytes
			Deflater deflater = new Deflater();
			deflater.setInput(je_bytes);
			deflater.finish();
			byte[] buffer = new byte[s_byteArraySize];
			while (!deflater.finished())
			{
				int count = deflater.deflate(buffer);  
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
			deflater.end();
			output_bytes = outputStream.toByteArray();
		}
		catch (UnsupportedEncodingException ex)
		{
			throw new SerializerException(ex);
		}
		catch (IOException ex) 
		{
			throw new SerializerException(ex);
		}
		if (output_bytes.length == 0)
		{
			// Something wrong happened
			return "";
		}
		// Encode resulting byte array into base-64
		String base64_result = DatatypeConverter.printBase64Binary(output_bytes);
		return base64_result;
	}

	@Override
	public Interpreter deserializeAs(String e, Class<?> clazz) throws SerializerException
	{
		// Decode base-64 string into byte array
		byte[] input_bytes = DatatypeConverter.parseBase64Binary(e);
		// Inflate byte array
		Inflater inflater = new Inflater();   
		inflater.setInput(input_bytes);  
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(input_bytes.length);  
		byte[] buffer = new byte[s_byteArraySize];  
		byte[] output = new byte[0];
		try
		{
			while (!inflater.finished())
			{  
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);  
			}
			outputStream.close();  
			output = outputStream.toByteArray();
			// Get interpreter from string
			String input_string = new String(output, "UTF-8");
			return super.deserializeAs(input_string, Interpreter.class);
		} 
		catch (DataFormatException ex)
		{
			throw new SerializerException(ex);
		} 
		catch (IOException ex) 
		{
			throw new SerializerException(ex);
		}
	}


}
