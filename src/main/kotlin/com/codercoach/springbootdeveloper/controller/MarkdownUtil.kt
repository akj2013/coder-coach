package com.example.util

import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.data.MutableDataSet
import com.vladsch.flexmark.html.HtmlRenderer

object MarkdownUtil {
    @JvmStatic
    fun toHtml(markdown: String): String {
        val options = MutableDataSet().apply {
            set(Parser.EXTENSIONS, listOf(TablesExtension.create()))
        }

        val parser = Parser.builder(options).build()
        val renderer = HtmlRenderer.builder(options).build()
        val document = parser.parse(markdown)
        return renderer.render(document)
    }
}
