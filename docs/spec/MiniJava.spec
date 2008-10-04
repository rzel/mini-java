<?xml version="1.0" standalone="yes" ?>
<spec>
<tokens>
	<!-- operators -->
  	<token>
  		<Type>+</Type> 
  		<RegularExpression>+</RegularExpression> 
  	</token>
  	<token>
  		<Type>-</Type> 
  		<RegularExpression>-</RegularExpression> 
  	</token>
  	<token>
  		<Type>*</Type> 
  		<RegularExpression>\*</RegularExpression> 
  	</token>
  	
  	<!-- paras, brackets, squares -->
  	<token>
  		<Type>(</Type>
  		<RegularExpression>\(</RegularExpression>
  	</token>
  	<token>
  		<Type>)</Type>
  		<RegularExpression>\)</RegularExpression>
  	</token>

  	
  	<token>
  		<Type>{</Type>
  		<RegularExpression>{</RegularExpression>
  	</token>
  	<token>
  		<Type>}</Type>
  		<RegularExpression>}</RegularExpression>
  	</token>
  	
  	<token>
  		<Type>[</Type>
  		<RegularExpression>\[</RegularExpression>
  	</token>
  	<token>
  		<Type>]</Type>
  		<RegularExpression>\]</RegularExpression>
  	</token>
  	
  	<token>
  		<Type>System.out.println</Type>
  		<RegularExpression>System.out.println</RegularExpression> 
  	</token>
  	
  	<!-- id, int -->
  	<token>
  		<Type>id</Type> 
  		<RegularExpression>([a-z]|[A-Z]|_)([a-z]|[A-Z]|[0-9]|_)*|&lt;|&amp;&amp;|&gt;</RegularExpression> 
  	</token>
  	<token>
  		<Type>integer</Type> 
  		<RegularExpression>0|([1-9][0-9]*)</RegularExpression> 
  	</token>
  	
  	<!-- skip -->
  	<token>
  		<Type>SKIP</Type> 
  		<RegularExpression> |\n|\t|\r|\r\n</RegularExpression> 
  	</token>

  	<token>
  		<Type>COMMENT</Type> 
  		<RegularExpression>/\*(^*)*\*(((\*)*|(~/*)(^*)*)\*)*/|//.*</RegularExpression> 
  	</token>
  
  	<token>
  		<Type>,</Type> 
  		<RegularExpression>,</RegularExpression> 
  	</token>
  	<token>
  		<Type>;</Type> 
  		<RegularExpression>;</RegularExpression> 
  	</token>
  	<token>
  		<Type>.</Type> 
  		<RegularExpression>\.</RegularExpression> 
  	</token>
  	<token>
  		<Type>=</Type> 
  		<RegularExpression>=</RegularExpression> 
  	</token>
  	<token>
  		<Type>!=</Type> 
  		<RegularExpression>!=</RegularExpression> 
  	</token>
  	<token>
  		<Type>!</Type> 
  		<RegularExpression>!</RegularExpression> 
  	</token>
</tokens>
<modifiers>
	<modifier name="lexer.MiniJavaKeywordModifier" />
</modifiers>
</spec> 
