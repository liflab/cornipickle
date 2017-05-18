package ca.uqac.lif.cornipickle.serialization;

import java.util.HashMap;
import java.util.Map;

import ca.uqac.lif.azrael.GenericSerializer;
import ca.uqac.lif.azrael.ObjectHandler;
import ca.uqac.lif.azrael.SerializerException;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonString;

public class PatternHandler extends ObjectHandler<JsonElement>{

  public PatternHandler(GenericSerializer<JsonElement> s) {
    super(s);
  }
  
  @Override
  public boolean appliesTo(Class<?> clazz) {
    return java.util.regex.Pattern.class.isAssignableFrom(clazz);
  }

  @Override
  public Map<String, Object> deserializeContents(JsonElement contents, Class<?> clazz) throws SerializerException
  {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("pattern", ((JsonMap)contents).get("pattern"));
    return map;
  }
  
  @Override
  protected Object getInstance(Map<String,Object> contents, Class<?> clazz) throws SerializerException
  {
    JsonString patternJsonString = (JsonString) contents.get("pattern");
    return java.util.regex.Pattern.compile(patternJsonString.stringValue());
  }
  
  @Override
  protected Object populateObject(Object o, Map<String,Object> contents, Class<?> clazz) throws SerializerException
  {
    return o;
  }

  @Override
  public JsonElement serializeObject(Object o, Map<String, JsonElement> contents) throws SerializerException
  {
    JsonMap map = new JsonMap();
    map.put("pattern", contents.get("pattern"));
    return map;
  }

}
