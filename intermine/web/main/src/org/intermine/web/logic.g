//
// Grammer for path expressions
//

header {
package org.intermine.web;
}

//
// LogicParser
//

class LogicParser extends Parser;

options {
  buildAST = true;
  exportVocab = Logic;
  defaultErrorHandler = false;
}

tokens {
    OR_EXPR;
    AND_EXPR;
}

expr
    : orExpr
    ;

orExpr
    : andExpr ( "or"^ andExpr )*
    ;
    
andExpr
    : atom ( "and"^ atom )*
    ;

atom
    : IDENTIFIER
    | bracketedExpr
    ;
    
bracketedExpr
    : OPEN_PAREN! expr CLOSE_PAREN!
    ;


//
// LogicLexer
//

class LogicLexer extends Lexer;

options {
  exportVocab = Logic;
}

OPEN_PAREN   : '(' ;
CLOSE_PAREN  : ')' ;
WS           : ' ' { $setType(Token.SKIP); } ; // Ignore this token

IDENTIFIER
    :  ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*
    ;
    