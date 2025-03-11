import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class LexicalAnalyser {
	
	public enum State {
		Sstart, Snumber, Soperator, Szero, SwhitespaceOperator, SwhitespaceNumber,
		SdecimalPoint, Sne, See
	}


	public static List<Token> analyse(String input) throws NumberException, ExpressionException {

		
		StringBuilder s = new StringBuilder();
		List<Token> tokenList = new ArrayList<>();
		State state = State.Sstart;

		if (input.isEmpty()) {
        throw new ExpressionException("Input is empty");
    }
		for(int i = 0; i < input.length(); i++){
			char c = input.charAt(i);
			Token.TokenType  token = Token.typeOf(c);

			switch(state) {
				case Sstart:
					if(token == Token.TokenType.NUMBER){
						// if(numberFlag){
						// 	throw new ExpressionException("Number after a number");
						// }
						//firstOperator = false;
						//operatorFlag = false;
						s.append(c);
						if(c == '0'){
						state = State.Szero;
						} else {
							state = State.Snumber;
						}
					}
					else if (c == ' '){
						//continue;
						throw new ExpressionException("Whitespaces are only allowed between number and operator");
					}
					else if(ifOperator(token)){
								// if(operatorFlag){
								// 	throw new ExpressionException("Operator after Operator");
								// }
								// if(firstOperator){
								// 	throw new ExpressionException("Cannot start with an operator");
								// }
								// operatorFlag = true;
								throw new ExpressionException("Cannot start with an operator");
								// tokenList.add(new Token(token));
								// state = State.Soperator;
					}
					else if(c == '.'){
						throw new NumberException("Cannot start with a decimal point");
					//	state = State.Sne;
					}
					else if(token == Token.TokenType.NONE){
						throw new ExpressionException("Input cannot be empty");
					//	state = State.See;
					}
					else {
						throw new ExpressionException("Invalid character");
					//	state = State.See;
					}
					break;

				case SwhitespaceNumber:
						if (token == Token.TokenType.NUMBER){
							throw new ExpressionException("whitespace between numbers");
					//			state = State.See;
						} else if(c == ' '){
							continue;
					    } else if(ifOperator(token)){
						// tokenList.add(new Token(Double.parseDouble(s.toString())));
                    	// s.setLength(0);
						tokenList.add(new Token(token));
						state = State.Soperator;
						} else if (c == '.'){
							throw new NumberException("There should be something before a decimal point");
					//			state = State.Sne;
						} else {
							throw new ExpressionException("Invalid character after operator whitespace.");
					//			state = State.See;
						}
						break;

				case SwhitespaceOperator:
						if (token == Token.TokenType.NUMBER){
						s.append(c);
						if(c == '0'){
						state = State.Szero;
						} else {
						state = State.Snumber;
						} }
						 else if(c == ' '){
						continue;
					    } else if(ifOperator(token)){
							throw new ExpressionException("Operator after operator");
					//			state = State.See;
						} else if(c == '.'){
							throw new NumberException("There should be something before decimal point.");
					//			state = State.Sne;
						} else {
							throw new ExpressionException("Invalid character after operator whitespace.");
					//			state = State.See;
						}
						break;

				case Szero:
					if (token == Token.TokenType.NUMBER){
						s.append(c);
						if(c == '0'){
						//state = State.Szero;
						throw new NumberException("consecutive zeroes");
					//		state = State.Sne;
						} else {
						//state = State.Snumber;
						throw new NumberException("No digits allowed after 0");
						}
						
					} else if(c == '.'){
						// if(decimalFlag){
						// 	throw new NumberException("Incomplete number");
						// }
						s.append(c);
						//decimalFlag = true;
						state = State.SdecimalPoint;
					}
					else if(c == ' '){
						tokenList.add(new Token(Double.parseDouble(s.toString())));
						s.setLength(0);
						state = State.SwhitespaceNumber;
					} 
					else if(ifOperator(token)){
					tokenList.add(new Token(Double.parseDouble(s.toString())));
                    s.setLength(0);
					//operatorFlag = true;
					tokenList.add(new Token(token));
					state = State.Soperator;
					}
					else {
						throw new ExpressionException("Invalid character after number");
					//		state = State.See;
					}
					// numberFlag = true;
					// operatorFlag = false;
					break;


				case Snumber:
					if (token == Token.TokenType.NUMBER){
						s.append(c);
						
					}

					else if(ifOperator(token)){
					//if(operatorFlag){ throw new ExpressionException("Operator after Operator");}
					tokenList.add(new Token(Double.parseDouble(s.toString())));
                    s.setLength(0);
					//operatorFlag = true;
					tokenList.add(new Token(token));
					state = State.Soperator;
					}

					else if(c == '.'){
						// buffer.append(c);
						// state = State.Sdecimal;
						throw new NumberException("Decimal cannot start with a non zero");
					//		state = State.Sne;
					}
					else if(c == ' '){
						tokenList.add(new Token(Double.parseDouble(s.toString())));
						s.setLength(0);
						// if(decimalFlag){
						// 	throw new NumberException("Incomplete number");
						// }
						state = State.SwhitespaceNumber;
					}

					else {
						throw new ExpressionException("Invalid character after number");
					//		state = State.See;
					}
					// numberFlag = true;
					// operatorFlag = false;
					break;

				case Soperator:
					/*if (ifOperator(token)){
						tokenList.add(new Token(token));
					}*/
					 if(c == ' '){
						state = State.SwhitespaceOperator;
					}
					else if(token == Token.TokenType.NUMBER){
						s.append(c);
						//operatorFlag = false;
						//state = State.Snumber;
						if(c == '0'){
						state = State.Szero;
						} else {
						state = State.Snumber;
						}
					} 
					else {
						throw new ExpressionException("Invalid Character after operator");
					//		state = State.See;
					}
					// operatorFlag = true;
					//numberFlag = false;
					break;

				case SdecimalPoint:
					 if (token == Token.TokenType.NUMBER){
							s.append(c);
						// 	if(c == '0'){
						// 		state = State.Szero;
						// } else {
								state = State.Snumber;
								
					 } 
					 	else {
					throw new NumberException("Incomplete number with after decimal point");
					//	state = State.Sne;
					} 
					break;


				// case Sdecimal:
				// 	if(token == Token.TokenType.NUMBER){
				// 		s.append(c);
				// 		decimalFlag = false;
				// 	}
				// 	else if(c == ' '){
				// 		if(decimalFlag){
				// 			throw new NumberException("Incomplete number");
				// 		}
				// 		tokenList.add(new Token(Double.parseDouble(s.toString())));
				// 		s.setLength(0);				
				// 		state = State.SwhitespaceNumber;
				// 	}
				// 	else if(ifOperator(token)){
				// 		if(decimalFlag){
				// 			throw new NumberException("Incomplete number");
				// 		}
				// 		tokenList.add(new Token(Double.parseDouble(s.toString())));
                //         s.setLength(0);
				// 		operatorFlag = true;
				// 		tokenList.add(new Token(token));
				// 		state = State.Soperator;
				// 	}
				// 	else{
				// 		throw new ExpressionException("Invalid character after decimal");
				// 	}
				// 	// numberFlag = true;
				// 	// operatorFlag = false;
				// 	break;
				/*case See:
				break;
				case Sne:
				break;*/
				default:
					continue;
			}
		}

		if(s.length() > 0){
			// if(decimalFlag){
			// 				throw new NumberException("Incomplete number");
			// 			}
			tokenList.add(new Token(Double.parseDouble(s.toString())));
		}
		// if(operatorFlag){
		// 	throw new ExpressionException("Cannot end with an operator");
		// }

		if(state == State.Soperator){
			throw new ExpressionException("Cannot end with an operator");
		}
		if(state == State.SdecimalPoint){
			throw new NumberException("Cannot end with a decimal");
		}
		if(state ==State.SwhitespaceNumber || state == State.SwhitespaceOperator ){
			throw new ExpressionException("Whitespaces are only allowed between number and operator");
		}
		
		

		return tokenList;




	}
	
	
	public static boolean ifOperator(Token.TokenType t){
		if(t == Token.TokenType.PLUS || t == Token.TokenType.MINUS || t == Token.TokenType.TIMES || t == Token.TokenType.DIVIDE)
		{return true;}
		return false;
	}
	
}
