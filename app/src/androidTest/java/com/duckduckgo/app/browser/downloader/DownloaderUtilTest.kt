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

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DownloaderUtilTest {

    private lateinit var testee: DownloaderUtil

    @Before
    fun setup() {
        testee = DownloaderUtil
    }

    @Test
    fun mytest0() {
        assertEquals(
            "display_pdf.pdf",
            testee.guessFileName(
                url =
                    "https://app.ecourts.gov.in/ecourt_mobile_encrypted_DC/display_pdf.php?filename=tsdf3b4j9eDY7P484mIWwB4thbfVfb9VP5F2YO%2FsEL01hhbEhRN7YUHOyJ95xJ7L&caseno=hoK77H9Pkg5OBjIcPryg7r8RVKQtM2WgWLGpNcdQiZI%3D&cCode=9ZlIEp0G5VNxKgqa5ZA1kA%3D%3D&appFlag=&state_cd=DytSfGyRsIC664sNz2SgoA%3D%3D&dist_cd=84p6UkEEfUOd%2BhVJ3YrfmA%3D%3D&court_code=9ZlIEp0G5VNxKgqa5ZA1kA%3D%3D&bilingual_flag=0",
                contentDisposition = null,
                mimeType = "application/pdf"))
    }

    @Test
    fun mytest1() {
        assertEquals(
            "0_BRkMUINdhCYPk1KM.jpg",
            testee.guessFileName(
                url = "https://miro.medium.com/max/1050/0*BRkMUINdhCYPk1KM.jpg",
                contentDisposition = null,
                mimeType = "image/jpeg"))
    }

    @Test
    fun mytest2() {
        assertEquals(
            "MENU_2103.pdf",
            testee.guessFileName(
                url =
                    "https://www.jdwetherspoon.com/~/media/files/pdf-documents/menus/currentmenus/menu_2103.pdf",
                contentDisposition = """attachment; filename="MENU_2103.pdf"""",
                mimeType = "application/pdf"))
    }

    @Test
    fun mytest3() {
        assertEquals(
            "Telegram.apk",
            testee.guessFileName(
                url =
                    "https://cdn4.telesco.pe/file/Telegram.apk?token=NuxgpIu_V2xfigA0w9J9OYJ4VObGzBN3UIE4lHKxU_fDAwpCygehc7X7HMtKyX0-81yZ25nAnXYPrqQld92Bj7MDqx2uLYdqC2PXlrwKrsxHPcstsWLccYkecYLKg3tXLiP0pCqcW4jOqn44cq9sReibdT-HpGaB3POji9b9MyR1deOaZLbbIXyU8j8EpltuuN28vDDAx8E1rHXLCYt0x_pGMWYpOMIAYVZmx05Rbw0C3t05VwkVLnmj28kRt2p39dtdU3y4Fg7AGjH1rRk5goOKSuOrncazjRlZSWfferWTvm5rYWQdtAn16izMh03pr7v4dq8_88SDP4RRqTj_V0X5_lzZhB-GP1deYWAlZcf9R-t3QyELA3qxA2K5U6VXHiD93PLNowwWadeqdeEEXZiTqycH8FMJZwP6PHf6dxHLr5sQI1nSfJh0ulei-bJwvTrXslY-0-hy5FaRzfQk6F-y-w8dxIGYThH8zhFzyCnaFLJg72z8DeXb49X8ukqQhz-Iv4ufXvzuZw0zBXbM0WE2BxTSbYnZOjnkCabxaNqiYtLrg5GHtvE5Uq5c2pT4sin4duCNsFVHAo6hSkiolZo1YDrbzu3lgGoMyFML_9hJ4vldgKCCBSRs_wgLpLc_Lyxh6T2rYu_e4g6UtE_zD3wooBl76vcgVMSuUfoZgUwGClPBKqBAyzlUZI1slIg6d0qvkfP5Sp8sZA5s1QczzWHCQ9ttf4M7YXRfwYexx2cLAOoiFPLZnsrizUQKaWsuenevxQtf7x-z3K4xQIDdnno6qKRZRvdG3t50vyKVzbWdWEL0rsNv1BakQlze0xGKTmoJkFCqHRRYtDPp9zCNV5Xx8jEYy0alwa4sjZO5cSwztNyULXpxJSCDRuERPeifFKmDcz6c3Ce1ZvTLh8D_ECZOC-HKRpmHoLTHgywathGjtpnCMG_wlVTpwK98DSKIPb0nGDAc7Ob4LivUSyYkLjSy2VbAsdqp61GoD0ZnzHZQHFPWyMmuZnGWMaGeJSDjem-SLPhjzZAoZatwKIdTYqgG6hjE7OoRwCdeTCE0X1hdv95S5q3vJsdETU0RXqimUOYZOfibVu9BVsUsex5T5pmFigUEsgl8sBuggAE9R4S95nyd-c_cP6w84IZqBBJbF9zZjDj8DptwqlMwuXzMRox7ORQWQ5NO_6CGcZjFncoIIOKu415aK6prEy4uyFEu6MGrU3k28-gSxWGnxVxj9KxJRUfVBv2zwtC4wBiemxXROBiWYhwE-JKMN0t6UH5fyN6vKbOEAQFO9iR-oOUyeETcEUEi47HV6oSLlT8L-dKMd0IMT4-a7A9Hb-ftPZAxk55tPrpDNihFu7CvCBESuA",
                contentDisposition = """attachment; filename="Telegram.apk"""",
                mimeType = "application/octet-stream"))
    }

    @Test
    fun mytest4() {
        assertEquals(
            "youtube.apk",
            testee.guessFileName(
                url =
                    "https://secure.downloadfp.com/android/US/com.google.android.youtube/114955670/youtube.apk?st=NhRdigQiE6mo5cpAqxZtGQ&e=1640362281",
                contentDisposition = null,
                mimeType = "application/octet-stream"))
    }

    @Test
    fun mytest5() {
        assertEquals(
            "Monitoraggio_Fase_2__report_nazionale_83_finale.pdf",
            testee.guessFileName(
                url =
                    "https://www.iss.it/documents/20126/0/Monitoraggio+Fase+2_+report_nazionale_83_finale.pdf/44233871-6660-ed64-b136-306c984f5b2d?t=1639759695768",
                contentDisposition =
                    """inline; filename="Monitoraggio Fase 2_ report_nazionale_83_finale.pdf"""",
                mimeType = "application/pdf"))
    }

    @Test
    fun mytest6() {
        assertEquals("filename.jpg", testee.parseContentDisposition("""attachment; filename="filename.jpg""""))
    }

    @Test
    fun mytest7() {
        assertEquals("filename.jpg", testee.parseContentDisposition("""attachment ; filename="filename.jpg""""))
    }

    @Test
    fun mytest8() {
        assertEquals("filename.jpg", testee.parseContentDisposition("""attachment;filename="filename.jpg""""))
    }

    @Test
    fun mytest9() {
        assertEquals("filename.jpg", testee.parseContentDisposition("""attachment; filename=filename.jpg"""))
    }

    @Test
    fun mytest10() {
        assertEquals("filename.jpg", testee.parseContentDisposition("""inline; filename="filename.jpg""""))
    }
}
