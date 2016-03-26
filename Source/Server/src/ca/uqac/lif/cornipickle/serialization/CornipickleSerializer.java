package ca.uqac.lif.cornipickle.serialization;

import ca.uqac.lif.azrael.json.JsonListHandler;
import ca.uqac.lif.azrael.json.JsonMapHandler;
import ca.uqac.lif.azrael.json.JsonSerializer;

public class CornipickleSerializer extends JsonSerializer
{
	public CornipickleSerializer()
	{
		super();
		addObjectHandler(0, new JsonListHandler(this));
		addObjectHandler(0, new JsonMapHandler(this));
	}
}
