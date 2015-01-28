$(document).ready(function () {
  // Doesn't look good
  // $("#main-accordion").accordion({ "collapsible" : true });
  $("ul.sets li").add("ul.predicates li").add("ul.verdicts li").draggable();
});

highlight_predicate = function(predicate_name)
{
  $("." + predicate_name).toggleClass("highlighted");
};
