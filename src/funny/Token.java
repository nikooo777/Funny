/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;

class Token {

    static enum Type {
        
        // Put reserved words between Bob_* and Eob_*.
        // Except for case, the spelling of the enums and the reserved words shall be the same.
        // They are put in the reserved map by the class initializer.
        // The Bob_* and Eob_* marker names have been chosen to reduce the risk of clashes with real-world reserved words.
        
        Bob_7cc9d33489e99a8b210379e7a3a3b2e7b102fd09, // begin of reserved words block
        
        Nil, False, True,
        If, IfNot, Then, Else, Fi,
        While, WhileNot, Do, Od,
        Print, Println,
        
        Eob_7cc9d33489e99a8b210379e7a3a3b2e7b102fd09, // end of reserved words block

        Id, Num, String,
        
        Semicolon, Comma, To,
        
        OpenParen, CloseParen,
        OpenBrace, CloseBrace,
        Not,
        Times, Div, Mod,
        Plus, Minus,
        Lt, Le, Gt, Ge,
        Eq, Ne,
        LogAnd,
        LogOr,
        Becomes, PlusBecomes, MinusBecomes, TimesBecomes, DivBecomes, ModBecomes,
        
        Eos,
        Unknown,
    }

    private static final HashMap<String, Token> reserved = new HashMap<String, Token>();

    private final Type type;
    private final String name;
    private final BigDecimal num;

    static {
        Type[] values = Type.values();
        for (int i = Type.Bob_7cc9d33489e99a8b210379e7a3a3b2e7b102fd09.ordinal() + 1; i < Type.Eob_7cc9d33489e99a8b210379e7a3a3b2e7b102fd09.ordinal(); ++i)
            reserved.put(values[i].name().toLowerCase(Locale.US), new Token(values[i]));
    }

    private Token(Type type, BigDecimal num, String name) {
        this.type = type;
        this.name = name;
        this.num = num;
    }

    private Token(Type type) {
        this(type, null, null);
    }

    Type type() {
        return type;
    }

    String name() {
        return name;
    }

    BigDecimal num() {
        return num;
    }

    static Token id(String name) {
        Token token = reserved.get(name);
        return
            token != null ?
                new Token(token.type(), null, null) :
                new Token(Type.Id, null, name);
    }

    static Token simple(Type type) {
        return new Token(type, null, null);
    }

    static Token num(BigDecimal num) {
        return new Token(Type.Num, num, null);
    }

    static Token string(String s) {
        return new Token(Type.String, null, s);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(80);
        sb.append(Token.class.getSimpleName())
            .append(" [type=")
            .append(type());
        if (name() != null)
            sb.append(", name=").append(name());
        if (num() != null)
            sb.append(", num=").append(num());
        sb.append("]");
        return sb.toString();
    }

}