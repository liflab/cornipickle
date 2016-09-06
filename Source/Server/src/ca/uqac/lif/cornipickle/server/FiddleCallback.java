package ca.uqac.lif.cornipickle.server;

import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import ca.uqac.lif.cornipickle.fiddle.Fiddle;
import ca.uqac.lif.cornipickle.fiddle.FiddlePair;
import ca.uqac.lif.jerrydog.CallbackResponse;
import ca.uqac.lif.jerrydog.RequestCallback;
import ca.uqac.lif.jerrydog.RestCallback;
import ca.uqac.lif.json.JsonMap;

public class FiddleCallback extends RestCallback {
  protected Fiddle m_fiddle;
  
  public FiddleCallback() {
    super(RequestCallback.Method.POST, "/fiddle/");
    m_fiddle = new Fiddle();
  }

  @Override
  public CallbackResponse process(HttpExchange t) {
    CallbackResponse response = new CallbackResponse(t);
    Map<String,String> attributes = getParameters(t);
    FiddlePair fp = m_fiddle.doOperation("", attributes.get(""));
    response.setContents(fp.getArgument());
    return response;
  }

}
