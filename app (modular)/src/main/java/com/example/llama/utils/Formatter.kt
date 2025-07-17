
package com.example.llama.utils

import com.example.llama.model.Module
import java.util.Locale

/**
 * Utility class to handle formatting of input / output from LLM.
 *
 * Default prompt skeleton:
 *
 * Summarize this {moduleName} into these {numOfFields} fields: {fieldNames}.
 * Only output the values of the {numOfFields} fields, as 1 json object. (start) {input} (end)
 */
object Formatter {

    /**
     * Formats the input text into a instruction prompt for summarizing.
     *
     * @param module Module corresponding to the type of input text
     * @param input Input text to be summarized
     * @see Module
     */
    fun formatInput(
        module: Module,
        input: String
    ) : String {
        val category: String = module.moduleName.lowercase()
        val fields: String = module.fieldNamesLiteral
        val numOfFields: Int = module.fieldNames.size

        return String.format(Locale.UK, "Summarize this %s into these %d fields: %s " +
                "\nOnly output the values of the %d fields, as 1 json object." +
                "\n(start) %s (end)",
            category, numOfFields, fields, numOfFields, input)
    }

    /**
     * Strips output string to return the first {...} block.
     */
    fun formatOutputToJsonString(output: String) : String {
        // Regex pattern to match the first {...} block, non-greedy
        val regex = Regex("\\{.*?\\}", RegexOption.DOT_MATCHES_ALL)

        val match = regex.find(output)

        return match?.value?.trimIndent() ?: ""
    }

}
