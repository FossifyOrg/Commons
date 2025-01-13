package org.fossify.commons.helpers

import android.telephony.PhoneNumberUtils

/**
 * Mostly depends on [android.telephony.PhoneNumberUtils].
 */
object KeypadHelper {
    // this should probably be split into language-specific mappings
    private val KEYPAD_MAP = mutableMapOf<Char, Int>().apply {
        put('ı', 4)
        put('İ', 4)

        put('ł', 5)
        put('Ł', 5)
    }

    /**
     * Translates any alphabetic letters (i.e. [A-Za-z]) in the
     * specified phone number into the equivalent numeric digits,
     * according to the phone keypad letter mapping described in
     * ITU E.161 and ISO/IEC 9995-8.
     *
     * @return the input string, with alpha letters converted to numeric
     * digits using the phone keypad letter mapping. For example,
     * an input of "1-800-FOSS-411" will return "1-800-3667-411".
     */
    fun convertKeypadLettersToDigits(input: String): String {
        val digits = PhoneNumberUtils.convertKeypadLettersToDigits(input)
        val result = StringBuilder(digits.length)
        for (c in digits) {
            val digit = KEYPAD_MAP[c] ?: c
            result.append(digit)
        }

        return result.toString()
    }
}
