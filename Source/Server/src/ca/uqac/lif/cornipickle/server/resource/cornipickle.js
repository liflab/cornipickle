html_entities = function(str) {
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
};

remove_spaces = function(line)
{
  var new_line = "";
  for (var j = 0; j < line.length; j++)
  {
    if (line[j] === " ")
    {
      new_line += "&nbsp;";
    }
    else
    {
      new_line += line.substring(j);
      break;
    }
  }
  return new_line;
};

cornipickle_colourize = function(text)
{
  var out = "";
  var lines = text.split("\n");
  var in_comment = false;
  for (var i = 0; i < lines.length; i++)
  {
    var line = lines[i];
    line = html_entities(line);
    if (line === '&quot;&quot;&quot;')
    {
      in_comment = !in_comment;
    }
    if (in_comment === true)
    {
      out += cornipickle_colourize_comment(line);
      continue;
    }
    if (line.trim()[0] === "#")
    {
      // One-line comment
      line = remove_spaces(line);
      line = '<span class="line-comment">' + line + '</span><br />\n';
      out += line;
      continue;
    }
    out += cornipickle_colourize_code(line);
  }
  return out;
};

cornipickle_colourize_code = function(line)
{
  line = remove_spaces(line);
  line = line.replace(/'s (left|right|top|bottom|height|width|text|id|class)/g, '<span class="property-name">\'s $1</span>');
  line = line.replace(/We say that/, '<span class="predicate-definition">We say that</span>');
  line = line.replace(/when/, '<span class="predicate-definition">when</span>');
  line = line.replace(/For each (.*) in/, '<span class="forall">For each <span class="element-name">$1</span> in</span>');
  line = line.replace(/There exists (.*) such that/, '<span class="exists">There exists <span class="element-name">$1</span> such that</span>');
  line = line.replace(/And/, '<span class="nary-statement">And</span>');
  line = line.replace(/Or/, '<span class="nary-statement">Or</span>');
  line = line.replace(/A (.*) is any of/, '<span class="set-definition">A $1 is any of</span>');
  line = line.replace(/\$(\w+)/g, '<span class="element-name">$$$1</span>');
  line = line.replace(/\$\((.*?)\)/, '<span class="css-selector">$($1)</span>');
  line = line.replace(/\(/g, '<span class="parenthesis">(</span>');
    line = line.replace(/\)/g, '<span class="parenthesis">)</span>');
  line = line.replace(/(equals|is greater than|is less than)/g, '<span class="comparison-statement">$1</span>');
  line += "<br />\n";
  return line;
};

cornipickle_colourize_comment = function(line)
{
  line = remove_spaces(line);
  line = line.replace(/(@[\w\d]+)/, "<span class=\"attribute-name\">$1</span>");
  line = '<span class="comment">' + line + "</span><br />\n";
  return line;
};

cornipickle_colourize_all = function()
{
  $(".cornipickle-code").each(function(index) {
        $(this).html(cornipickle_colourize($(this).text()));
    });
};

javascript_colourize_all = function()
{
  $(".procedural-equivalent").each(function(index) {
        $(this).html(javascript_colourize($(this).text()));
    });
};

javascript_colourize = function(text)
{
  var out = "";
  var lines = text.split("\n");
  var in_comment = false;
  for (var i = 0; i < lines.length; i++)
  {
    var line = lines[i];
    line = html_entities(line);
    if (line === '/*')
    {
      in_comment = true;
    }
    if (line === '*/')
    {
      in_comment = false;
    }
    if (in_comment === true)
    {
      out += javascript_colourize_comment(line);
      continue;
    }
    if (line.trim()[0] === "/" && line.trim()[1] === "/")
    {
      // One-line comment
      line = remove_spaces(line);
      line = '<span class="line-comment">' + line + '</span><br />\n';
      out += line;
      continue;
    }
    out += javascript_colourize_code(line);
  }
  return out;
};

javascript_colourize_code = function(line)
{
  line = remove_spaces(line);
  line = line.replace(/\b([^\s\);]+?)\(/g, '<span class="javascript-fct">$1</span>(');
  line = line.replace(/\b(function|for|in|if|else|elseif)\b/g, '<span class="javascript-keyword">$1</span>');
  line += "<br />\n";
  return line;
};

cornipickle_colourize_comment = function(line)
{
  line = remove_spaces(line);
  line = '<span class="comment">' + line + "</span><br />\n";
  return line;
};

add_to_cornipickle = function(text)
{
  $.ajax({
    url : "/add",
    type : "PUT",
    data : text,
    success : function(result) {
      //$("#to-cornipickle").prop('disabled', true);
      cp_probe.setAttributesToInclude(result.attributes);
      cp_probe.setTagNamesToInclude(result.tagnames);
      //cp_probe.handleEvent();
    },
  });
};
