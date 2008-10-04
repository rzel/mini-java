<?xml version="1.0" standalone="yes" ?>
<spec>
<tokens>
	<token>
  		<Type>EQUIVALENCE</Type> 
  		<RegularExpression>&lt;==&gt;</RegularExpression> 
  	</token>
  	<token>
  		<Type>IMPLICATION</Type> 
  		<RegularExpression>==&gt;</RegularExpression> 
  	</token>
  	<token>
  		<Type>LOGICAL_OP</Type> 
  		<RegularExpression>&amp;&amp;|\|\||!</RegularExpression> 
  	</token>  	
	<token>
  		<Type>REL_OP</Type> 
  		<RegularExpression>=|!=|&lt;|&lt;=|&gt;=|&gt;|&lt;:</RegularExpression> 
  	</token>
	<token>
  		<Type>ARITH_OP</Type> 
  		<RegularExpression>+|-|\*|/|%</RegularExpression> 
  	</token>  	
  	<token>
  		<Type>QUAN_OP</Type> 
  		<RegularExpression>forall|exists|::</RegularExpression> 
  	</token>
  	
  	<token>
  		<Type>COMMA</Type> 
  		<RegularExpression>,</RegularExpression> 
  	</token>
  	<token>
  		<Type>SEMICOLON</Type> 
  		<RegularExpression>;</RegularExpression> 
  	</token>
  	<token>
  		<Type>COLON</Type> 
  		<RegularExpression>:</RegularExpression> 
  	</token>
  	<token>
  		<Type>DOT</Type> 
  		<RegularExpression>\.</RegularExpression> 
  	</token>
  	
  	
  	<token>
  		<Type>LEFT_PARA</Type>
  		<RegularExpression>\(</RegularExpression>
  	</token>
  	<token>
  		<Type>RIGHT_PARA</Type>
  		<RegularExpression>\)</RegularExpression>
  	</token>
  	
  	<token>
  		<Type>LEFT_BRACKET</Type>
  		<RegularExpression>{</RegularExpression>
  	</token>
  	<token>
  		<Type>RIGHT_BRACKET</Type>
  		<RegularExpression>}</RegularExpression>
  	</token>
  	
  	<token>
  		<Type>LEFT_SQUARE</Type>
  		<RegularExpression>\[</RegularExpression>
  	</token>
  	<token>
  		<Type>RIGHT_SQUARE</Type>
  		<RegularExpression>\]</RegularExpression>
  	</token>
  	  	
  	
  	<!-- id, int -->
  	<token>
  		<Type>IDENTIFIER</Type> 
  		<RegularExpression>([a-z]|[A-Z]|_|`|\~|#|$|\^|\.|\?)([a-z]|[A-Z]|[0-9]|_|`|\~|#|$|\^|\.|\?)*</RegularExpression> 
  	</token>
  	<token>
  		<Type>INTEGER</Type> 
  		<RegularExpression>0|([1-9][0-9]*)</RegularExpression> 
  	</token>
  
  	<!-- skip -->
  	<token>
  		<Type>SKIP</Type> 
  		<RegularExpression> |\n|\t|\r|\r\n</RegularExpression> 
  	</token>
  	
</tokens>
<modifiers>
	<modifier name="lex.model.BoogiePLKeywordModifier" />
</modifiers>
</spec> 
