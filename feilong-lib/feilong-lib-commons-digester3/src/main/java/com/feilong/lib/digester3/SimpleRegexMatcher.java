package com.feilong.lib.digester3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Simple regex pattern matching algorithm.
 * </p>
 * <p>
 * This uses just two wildcards:
 * <ul>
 * <li><code>*</code> matches any sequence of none, one or more characters
 * <li><code>?</code> matches any one character
 * </ul>
 * Escaping these wildcards is not supported .
 * </p>
 * 
 * @since 1.5
 */
public class SimpleRegexMatcher extends RegexMatcher{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRegexMatcher.class);

    // --------------------------------------------------------- Properties

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean match(String basePattern,String regexPattern){
        // check for nulls
        if (basePattern == null || regexPattern == null){
            return false;
        }
        return match(basePattern, regexPattern, 0, 0);
    }

    // --------------------------------------------------------- Implementations Methods

    /**
     * Implementation of regex matching algorithm. This calls itself recursively.
     *
     * @param basePattern
     *            the standard digester path representing the element
     * @param regexPattern
     *            the regex pattern the path will be tested against
     * @param baseAt
     *            FIXME
     * @param regexAt
     *            FIXME
     */
    private boolean match(String basePattern,String regexPattern,int baseAt,int regexAt){
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("Base: " + basePattern);
            LOGGER.trace("Regex: " + regexPattern);
            LOGGER.trace("Base@" + baseAt);
            LOGGER.trace("Regex@" + regexAt);
        }

        // check bounds
        if (regexAt >= regexPattern.length()){
            // maybe we've got a match
            if (baseAt >= basePattern.length()){
                // ok!
                return true;
            }
            // run out early
            return false;

        }
        if (baseAt >= basePattern.length()){
            // run out early
            return false;
        }

        // ok both within bounds
        char regexCurrent = regexPattern.charAt(regexAt);
        switch (regexCurrent) {
            case '*':
                // this is the tricky case
                // check for terminal
                if (++regexAt >= regexPattern.length()){
                    // this matches anything let - so return true
                    return true;
                }
                // go through every subsequent apperance of the next character
                // and so if the rest of the regex matches
                char nextRegex = regexPattern.charAt(regexAt);
                if (LOGGER.isTraceEnabled()){
                    LOGGER.trace("Searching for next '" + nextRegex + "' char");
                }
                int nextMatch = basePattern.indexOf(nextRegex, baseAt);
                while (nextMatch != -1){
                    if (LOGGER.isTraceEnabled()){
                        LOGGER.trace("Trying '*' match@" + nextMatch);
                    }
                    if (match(basePattern, regexPattern, nextMatch, regexAt)){
                        return true;
                    }
                    nextMatch = basePattern.indexOf(nextRegex, nextMatch + 1);
                }
                LOGGER.trace("No matches found.");
                return false;

            case '?':
                // this matches anything
                return match(basePattern, regexPattern, ++baseAt, ++regexAt);

            default:
                if (LOGGER.isTraceEnabled()){
                    LOGGER.trace("Camparing " + regexCurrent + " to " + basePattern.charAt(baseAt));
                }
                if (regexCurrent == basePattern.charAt(baseAt)){
                    // still got more to go
                    return match(basePattern, regexPattern, ++baseAt, ++regexAt);
                }
                return false;
        }
    }

}
