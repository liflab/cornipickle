package ca.uqac.lif.cornipickle.fiddle;

import java.util.List;

import ca.uqac.lif.bullwinkle.BnfParser;
import ca.uqac.lif.bullwinkle.BnfParser.InvalidGrammarException;
import ca.uqac.lif.bullwinkle.BnfRule;
import ca.uqac.lif.bullwinkle.TokenString;
import ca.uqac.lif.cornipickle.Interpreter;
import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonList;
import ca.uqac.lif.json.JsonMap;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.cornipickle.util.PackageFileReader;

public class GetGrammar extends FiddleOperation {

  @Override
  public boolean fire(JsonMap in) {
    String action = ((JsonString) in.get("action")).stringValue();
    return action.compareToIgnoreCase("getgrammar") == 0;
  }

  @Override
  public JsonElement doOperation(JsonMap argument, Interpreter i) {
    List<BnfRule> rules;
    try {
      rules = BnfParser.getRules(PackageFileReader.readPackageFile(Interpreter.class,"cornipickle.bnf"));
    } catch (InvalidGrammarException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
    JsonMap map = new JsonMap();
    for(BnfRule rule : rules){
      String left = rule.getLeftHandSide().getName();
      JsonList right = new JsonList();
      for(TokenString st : rule.getAlternatives()){
        right.add(st.toString());
      }
      map.put(left, right);
    }
    return map;
  }
  
}
