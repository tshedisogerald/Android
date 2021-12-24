/*
 * Copyright (c) 2021 DuckDuckGo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duckduckgo.app.browser.downloader

import android.net.Uri
import android.webkit.MimeTypeMap
import timber.log.Timber
import java.lang.IllegalStateException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

object DownloaderUtil {

    // Slightly different than the one in URLUtil (also contains `inline`).
    private val CONTENT_DISPOSITION_PATTERN =
        Pattern.compile(
            "(inline|attachment)\\s*;\\s*filename\\s*=\\s*(\"((?:\\\\.|[^\"\\\\])*)\"|[^;]*)\\s*",
            Pattern.CASE_INSENSITIVE,
        )

    fun guessFileName(url: String?, contentDisposition: String?, mimeType: String?): String {
        var filename: String? = null
        var extension: String? = null

        if (contentDisposition != null) {
            filename = extractFileNameFromContentDisposition(contentDisposition)
        }

        if (filename == null) {
            filename = extractFileNameFromUrl(url)
        }

        // Dummy name if we couldn't extract it.
        if (filename.isNullOrEmpty()) {
            filename = "downloadfile"
        }

        // Check / detect extension.
        val dotIndex = filename.lastIndexOf('.')
        if (dotIndex < 0) { // The filename has no extension.
            if (mimeType !=
                null) { // We have a mime type, extract the extension from there if possible.
                extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                if (extension != null) {
                    extension = ".$extension"
                }
            }
            if (extension == null) {
                extension =
                    if (mimeType != null && mimeType.lowercase(Locale.ROOT).startsWith("text/")) {
                        if (mimeType.equals("text/html", ignoreCase = true)) {
                            ".html"
                        } else {
                            ".txt"
                        }
                    } else {
                        ".bin"
                    }
            }
        } else { // The filename has an extension.
            val fileExtension = filename.substring(dotIndex + 1)
            if (mimeType != null) { // We have a mime type, check if the extension matches it.
                val mimeTypeExtension =
                    MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
                val typeFromExt = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
                if (typeFromExt == null) {
                    extension =
                        if (fileExtension == mimeTypeExtension || mimeTypeExtension == null) {
                            ".$fileExtension"
                        } else {
                            ".$mimeTypeExtension"
                        }
                } else {
                    Timber.i("Is there anything to do here?")
                }
            }
            if (extension == null) {
                extension = ".$fileExtension"
            }
            filename = filename.substring(0, dotIndex)
        }
        return filename.sanitizeFileName() + extension
    }

    private fun extractFileNameFromUrl(url: String?): String? {
        var filename: String? = null
        var decodedUrl = Uri.decode(url)
        if (decodedUrl != null) {
            val queryIndex = decodedUrl.indexOf('?')
            // If there is a query string strip it, same as desktop browsers
            if (queryIndex > 0) {
                decodedUrl = decodedUrl.substring(0, queryIndex)
            }
            if (!decodedUrl.endsWith("/")) {
                filename = decodedUrl.substringAfterLast('/')
            }
        }
        return filename
    }

    private fun extractFileNameFromContentDisposition(contentDisposition: String?): String? {
        val filename = parseContentDisposition(contentDisposition)
        return filename?.substringAfterLast('/')
    }

    fun parseContentDisposition(contentDisposition: String?): String? {
        try {
            val m: Matcher = CONTENT_DISPOSITION_PATTERN.matcher(contentDisposition)
            if (m.find()) {
                val quotedFileName = m.group(2)
                return quotedFileName?.replace("\"", "")
            }
        } catch (ex: IllegalStateException) {
            // This function is defined as returning null when it can't parse the header
        }
        return null
    }

    private fun String.sanitizeFileName(): String {
        return this.replace('*', '_').replace(" ", "_")
    }
}
