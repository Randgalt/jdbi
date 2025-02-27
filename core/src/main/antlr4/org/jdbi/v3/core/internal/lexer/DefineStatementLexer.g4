/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
lexer grammar DefineStatementLexer;

fragment ESCAPE_IN_QUOTE: '\\' ['\\] ;
fragment DOUBLE_QUOTE: '"' ;
fragment LT: '<' ;
fragment GT: '>' ;
fragment NAME: JAVA_LETTER | [0-9];

/* Lovingly lifted from https://github.com/antlr/grammars-v4/blob/master/java/java/JavaLexer.g4 */
fragment JAVA_LETTER : [a-zA-Z$_] | ~[\u0000-\u007F\uD800-\uDBFF] | [\uD800-\uDBFF] [\uDC00-\uDFFF];

COMMENT: '/*' .*? '*/' | '--' ~('\r' | '\n')* | '//' ~('\r' | '\n')*;
QUOTED_TEXT: '\'' (~['\\\r\n] | ESCAPE_IN_QUOTE)* '\'';
DOUBLE_QUOTED_TEXT: DOUBLE_QUOTE (~'"')+ DOUBLE_QUOTE;
ESCAPED_TEXT : '\\' . ;

DEFINE: LT (NAME)+ GT;

LITERAL: .;
