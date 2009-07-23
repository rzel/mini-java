package mini.java.lex;

import mini.java.regex.RegexCompiler;

public class RegexMatcher extends FAMatcher {    
    private final String _regex;

    public RegexMatcher(String type_, String regex_) {
        super(type_, RegexCompiler.compile(regex_));
        _regex = regex_;
    }

    public String getRegex() {
        return _regex;
    }
}
