/*
 * Copyright © 2011-2015, SUPSI. All rights reserved.
 * Author: Raffaello Giulietti
 */

package funny;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;

import funny.Token.Type;

class Tokenizer {

    private static final int EOS = -1;
    private static final int CR = '\r';
    private static final int LF = '\n';
    
    private final BufferedReader in;
    private final StringBuilder builder = new StringBuilder();
    private int ch;
    private boolean stepBack;
    private Token token;
    private Token prevToken;

    Tokenizer(Reader in) {
        this.in = new BufferedReader(in);
    }

    Token prev() {
        if (stepBack)
            throw new TokenizerException("already in step back mode");
        stepBack = true;
        return prevToken;
    }
    
    Token token() {
        return stepBack ? prevToken : token;
    }
    
    Token next() throws IOException {
        if (stepBack) {
            stepBack = false;
            return token;
        }
        prevToken = token;
        skipWhites();
        if ((token = id()) != null)
            return token;
        if ((token = num()) != null)
            return token;
        if ((token = operatorOrDelimiter()) != null)
            return token;
        if ((token = string()) != null)
            return token;
        return token = Token.simple(Type.Unknown);
    }

    /*
     * Upon entry the stream position is just before the operator or delimiter.
     * Upon exit the stream position is just after the operator or delimiter.
     */
    private Token operatorOrDelimiter() throws IOException {
        markAndRead(1);
        switch (ch) {
        case EOS: return Token.simple(Type.Eos);
        case '+': return plusOp();
        case '-': return minusOp();
        case '*': return timesOp();
        case '/': return divOp();
        case '%': return modOp();
        case '<': return lessOp();
        case '>': return greaterOp();
        case '!': return bangOp();
        case '=': return equalsOp();
        case '&': return andOp();
        case '|': return orOp();
        case '(': return Token.simple(Type.OpenParen);
        case ')': return Token.simple(Type.CloseParen);
        case '{': return Token.simple(Type.OpenBrace);
        case '}': return Token.simple(Type.CloseBrace);
        case ';': return Token.simple(Type.Semicolon);
        case ',': return Token.simple(Type.Comma);
        }
        return resetAndToken(null);
    }

    private Token andOp() throws IOException {
        markAndRead(1);
        switch (ch) {
        case '&': return Token.simple(Type.LogAnd);
        }
        return resetAndToken(Token.simple(Type.Unknown));
    }

    private Token orOp() throws IOException {
        markAndRead(1);
        switch (ch) {
        case '|': return Token.simple(Type.LogOr);
        }
        return resetAndToken(Token.simple(Type.Unknown));
    }

    private Token lessOp() throws IOException {
        markAndRead(1);
        switch (ch) {
        case '=': return Token.simple(Type.Le);
        }
        return resetAndToken(Token.simple(Type.Lt));
    }

    private Token greaterOp() throws IOException {
        markAndRead(1);
        switch (ch) {
        case '=': return Token.simple(Type.Ge);
        }
        return resetAndToken(Token.simple(Type.Gt));
    }

    private Token bangOp() throws IOException {
        markAndRead(1);
        switch (ch) {
        case '=': return Token.simple(Type.Ne);
        }
        return resetAndToken(Token.simple(Type.Not));
    }

    private Token equalsOp() throws IOException {
        markAndRead(1);
        switch (ch) {
        case '=': return Token.simple(Type.Eq);
        }
        return resetAndToken(Token.simple(Type.Becomes));
    }

    private Token divOp() throws IOException {
        markAndRead(1);
        switch (ch) {
        case '=': return Token.simple(Type.DivBecomes);
        }
        return resetAndToken(Token.simple(Type.Div));
    }

    private Token modOp() throws IOException {
        markAndRead(1);
        switch (ch) {
        case '=': return Token.simple(Type.ModBecomes);
        }
        return resetAndToken(Token.simple(Type.Mod));
    }

    private Token timesOp() throws IOException {
        markAndRead(1);
        switch (ch) {
        case '=': return Token.simple(Type.TimesBecomes);
        }
        return resetAndToken(Token.simple(Type.Times));
    }

    private Token minusOp() throws IOException {
        markAndRead(1);
        switch (ch) {
        case '=': return Token.simple(Type.MinusBecomes);
        case '>': return Token.simple(Type.To);
        }
        return resetAndToken(Token.simple(Type.Minus));
    }

    private Token plusOp() throws IOException {
        markAndRead(1);
        switch (ch) {
        case '=': return Token.simple(Type.PlusBecomes);
        }
        return resetAndToken(Token.simple(Type.Plus));
    }

    /*
     * Upon entry the stream position is just before the id.
     * Upon exit the stream position is just after the id.
     */
    private Token id() throws IOException {
        markAndRead(1);
        if (!isIdStart())
            return resetAndToken(null);
        appendMarkAndRead();
        while (isIdPart())
            appendMarkAndRead();
        return resetAndToken(Token.id(toStringAndClear()));
    }

    /*
     * Upon entry the stream position is just before the num.
     * Upon exit the stream position is just after the num.
     */
    private Token num() throws IOException {
        markAndRead(2);
        if (isDigit()) {
            digits();
            if (isFractionIndicator()) {
                appendMarkAndRead();
                if (isDigit())
                    digits();
            }
        } else if (isFractionIndicator()) {
            append();
            read();
            if (!isDigit())
                return resetAndToken(null);
            digits();
        } else
            return resetAndToken(null);
        markAndRead(3);
        if (isExponentIndicator())
            exponent();
        return resetAndToken(Token.num(new BigDecimal(toStringAndClear())));
    }

    private void exponent() throws IOException {
        int length = builder.length();
        append();
        read();
        if (isSign()) {
            append();
            read();
        }
        if (isDigit())
            digits();
        else
            builder.setLength(length);
    }

    private void digits() throws IOException {
        if (!(isDigit()))
            throw new AssertionError();
        appendMarkAndRead();
        while (isDigit())
            appendMarkAndRead();
        reset();
    }

    /*
     * Strings are sequences of UTF-16 encoded Unicode characters enclosed in quotation marks ".
     * The following escape sequences are honored:
     * \\ a single \
     * \" a "
     * \' a '
     * \r a CR
     * \n a LF
     * \t a HT
     * \b a BS
     * \f a FF
     * \xuuuu, where uuuu is a sequence of exactly 4 hexadecimal digits, is the Unicode character U+uuuu
     * Sequences of \CR or \LF are dropped
     */
    private Token string() throws IOException {
        markAndRead(1);
        if (ch != '"')
            return resetAndToken(null);
        for (;;) {
            markAndRead(1);
            if (ch == '"') {
                markAndRead(1);
                break;
            }
            switch (ch) {
            case '\\':
                escapeSequence();
                break;
            case CR:
            case LF:
                throw new TokenizerException("illegal character in string");
            case EOS:
                throw new TokenizerException("unclosed string");
            default:
                append();
            }
        }
        return resetAndToken(Token.string(toStringAndClear()));
    }

    private void escapeSequence() throws IOException {
        read();
        switch (ch) {
        case 'b':
            append('\b');
            return;
        case 't':
            append('\t');
            return;
        case 'n':
            append('\n');
            return;
        case 'f':
            append('\f');
            return;
        case 'r':
            append('\r');
            return;
        case '"':
            append('"');
            return;
        case '\'':
            append('\'');
            return;
        case '\\':
            append('\\');
            return;
        case 'x':
            unicodeEscapeSequence();
            return;
        case CR:
        case LF:
            return;
        default:
            throw new TokenizerException("illegal escape sequence in string");
        }
    }

    private void unicodeEscapeSequence() throws IOException {
        int c = 0;
        for (int i = 0; i < 4; ++i) {
            read();
            int d = hexDigit();
            if (d >= 0)
                c = c << 4 | d;
            else
                throw new TokenizerException("illegal Unicode escape sequence in string");
        }
        append(c);
    }

    /*
     * Upon entry stream position is before the optional whites.
     * Upon exit stream position is just after them.
     */
    private void skipWhites() throws IOException {
        for (;;) {
            markAndRead(2);
            if (ch == '/') {
                read();
                if (ch == '/')
                    skipLineComment();
                else if (ch == '*')
                    skipNestingComment();
                else
                    break;
            } else if (!isWhite())
                break;
        }
        reset();
    }

    private void skipNestingComment() throws IOException {
        for (;;) {
            read();
            if (ch == EOS)
                throw new TokenizerException("unclosed nesting comment");
            if (ch == '*') {
                read();
                if (ch == '/')
                    return;
                continue;
            }
            if (ch == '/') {
                read();
                if (ch == '*')
                    skipNestingComment();
                else
                    continue;
            }
        }
    }

    private void skipLineComment() throws IOException {
        while (!isEol());
            read();
    }

    private void markAndRead(int readAheadLimit) throws IOException {
        in.mark(readAheadLimit);
        read();
    }

    private void read() throws IOException {
        ch = in.read();
    }

    private void reset() throws IOException {
        in.reset();
    }

    private void append() {
        append(ch);
    }

    private void append(int c) {
        builder.append((char) c);
    }

    private void appendMarkAndRead() throws IOException {
        append();
        markAndRead(1);
    }

    private Token resetAndToken(Token tok) throws IOException {
        reset();
        return tok;
    }

    private boolean isEol() {
        return ch == CR || ch == LF || ch == EOS;
    }

    private boolean isDigit() {
        return Character.isDigit(ch);
    }

    private boolean isWhite() {
        return Character.isWhitespace(ch);
    }
    
    private boolean isSign() {
        return ch == '-' || ch == '+';
    }

    private boolean isExponentIndicator() {
        return ch == 'e' || ch == 'E';
    }

    private boolean isFractionIndicator() {
        return ch == '.';
    }

    private boolean isIdPart() {
        return Character.isJavaIdentifierPart(ch);
    }

    private boolean isIdStart() {
        return Character.isJavaIdentifierStart(ch);
    }

    private int hexDigit() {
        switch (ch) {
        case '0': case '1': case '2': case '3': case '4':
        case '5': case '6': case '7': case '8': case '9':
            return ch - '0';
        case 'A': case 'B': case 'C': case 'D': case 'E': case 'F':
            return 10 + (ch - 'A');
        case 'a': case 'b': case 'c': case 'd': case 'e': case 'f':
            return 10 + (ch - 'a');
        default: return -1;
        }
    }

    private String toStringAndClear() {
        String s = builder.toString();
        builder.setLength(0);
        return s;
    }
    
}