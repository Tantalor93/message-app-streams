package com.github.tantalor93

import spock.lang.Specification
import spock.lang.Unroll

class ApplicationSpec extends Specification {

    @Unroll
    def "countAlphabeticLetters shoud properly count alphabetic letters for string \"#string\""() {
        expect:
        Application.countAlphabeticLetters(string) == expected

        where:
        string         | expected
        "abcd"         | 4
        ""             | 0
        "1a23b"        | 2
        "a b c d"      | 4
        "a 1 b 2 defg" | 6
        "\" \'"        | 0
    }
}
