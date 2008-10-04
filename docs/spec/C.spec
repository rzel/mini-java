<?xml version="1.0" standalone="yes" ?>
<spec>
<tokens>
	<!-- operators -->
	<token>
  		<Type>BIN_OP</Type> 
  		<RegularExpression>(+|-|&lt;|&amp;&amp;)</RegularExpression> 
  	</token>
  	<token>
  		<Type>STAR</Type> 
  		<RegularExpression>\*</RegularExpression> 
  	</token>
  	<token>
  		<Type>OPERATOR</Type> 
  		<RegularExpression>(-&gt;|&amp;)</RegularExpression> 
  	</token>
  	<!-- paras, brackets, squares -->
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
  		<Type>RIGHT_SQYARE</Type>
  		<RegularExpression>\]</RegularExpression>
  	</token>
  	
  	<!-- id, int -->
  	<token>
  		<Type>IDENTIFIER</Type> 
  		<RegularExpression>([a-z]|[A-Z]|_)([a-z]|[A-Z]|[0-9]|_)*</RegularExpression> 
  	</token>
  	<token>
  		<Type>INTEGER</Type> 
  		<RegularExpression>[1-9]?[0-9]*</RegularExpression> 
  	</token>
  	
  	<!-- skip -->
  	<token>
  		<Type>SKIP</Type> 
  		<RegularExpression> |\n|\t|\r|\n
</RegularExpression> 
  	</token>
  	
  	
  	<token>
  		<Type>INTERPUNCTION</Type> 
  		<RegularExpression>\.|;|,|\?|:</RegularExpression> 
  	</token>
  	<token>
  		<Type>COMMENT</Type> 
  		<RegularExpression>/\*(^*)*\*(((\*)*|(~/*)(^*)*)\*)*/|//.*</RegularExpression> 
  	</token>
</tokens>
<modifiers>
	<modifier name="lex.model.CKeywordModifier" />
</modifiers>
</spec> 
