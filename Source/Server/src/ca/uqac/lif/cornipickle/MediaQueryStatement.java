package ca.uqac.lif.cornipickle;

import java.util.Map;
import java.util.logging.Level;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonPath;
import ca.uqac.lif.json.JsonString;

public class MediaQueryStatement extends Statement {

  private static int m_mediaQueryCounter = 0;
  
  private int m_id;
  
  private String m_mediaQuery;
  
  public MediaQueryStatement()
  {
    super();
    m_id = m_mediaQueryCounter++;
  }
  
  public MediaQueryStatement(String mediaquery)
  {
    super();
    m_id = m_mediaQueryCounter++;
    m_mediaQuery = mediaquery;
  }
  
  public int getId()
  {
    return m_id;
  }
  
  public String getMediaQuery()
  {
    return m_mediaQuery;
  }
  
  public void setMediaQuery(String query)
  {
    m_mediaQuery = query;
  }
  
  public String getAttributeFormat()
  {
    return m_id + "_" + m_mediaQuery;
  }
  
  @Override
  public Verdict evaluateAtemporal(JsonElement j, Map<String, JsonElement> d)
  {
    JsonMap mediaQueryList = (JsonMap) JsonPath.get(j, "mediaqueries");
    if (mediaQueryList.isEmpty())
    {
    	Interpreter.LOGGER.log(Level.WARNING, "Media query list is empty");
    }
    JsonElement verdict = mediaQueryList.get(String.valueOf(m_id));
    JsonString verdictString = (JsonString) verdict;
    if(verdictString.stringValue().equals("true"))
    {
      return new Verdict(Verdict.Value.TRUE);
    }
    else
    {
      return new Verdict(Verdict.Value.FALSE);
    }
  }

  @Override
  public boolean isTemporal()
  {
    return false;
  }

  @Override
  public Verdict evaluateTemporal(JsonElement j, Map<String, JsonElement> d)
  {
    JsonMap mediaQueryList = (JsonMap) JsonPath.get(j, "mediaqueries");
    JsonElement verdict = mediaQueryList.get(String.valueOf(m_id));
    JsonString verdictString = (JsonString) verdict;
    if(verdictString.stringValue().equals("true"))
    {
      return new Verdict(Verdict.Value.TRUE);
    }
    else
    {
      return new Verdict(Verdict.Value.FALSE);
    }
  }

  @Override
  public Statement getClone()
  {
    MediaQueryStatement out = new MediaQueryStatement(m_mediaQuery);
    out.m_id = this.m_id;
    m_mediaQueryCounter--;
    return out;
  }

  @Override
  public void resetHistory()
  {
    m_verdict = new Verdict(Verdict.Value.INCONCLUSIVE);
  }

  @Override
  public String toString(String indent)
  {
    return "The media query \"" + m_mediaQuery + "\" applies";
  }

  @Override
  public void prefixAccept(LanguageElementVisitor visitor)
  {
    visitor.visit(this);
    visitor.pop();
  }

  @Override
  public void postfixAccept(LanguageElementVisitor visitor)
  {
    visitor.visit(this);
    visitor.pop();
  }

}
