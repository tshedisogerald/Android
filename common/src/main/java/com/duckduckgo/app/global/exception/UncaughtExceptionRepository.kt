/*
 * Copyright (c) 2019 DuckDuckGo
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

package com.duckduckgo.app.global.exception

import androidx.annotation.VisibleForTesting
import com.duckduckgo.app.global.device.DeviceInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

interface UncaughtExceptionRepository {
    suspend fun recordUncaughtException(e: Throwable?, exceptionSource: UncaughtExceptionSource)
    suspend fun getExceptions(): List<UncaughtExceptionEntity>
    suspend fun deleteException(id: Long)
}

class UncaughtExceptionRepositoryDb(
    private val uncaughtExceptionDao: UncaughtExceptionDao,
    private val rootExceptionFinder: RootExceptionFinder,
    private val deviceInfo: DeviceInfo
) : UncaughtExceptionRepository {

    private var lastSeenException: Throwable? = null

    override suspend fun recordUncaughtException(e: Throwable?, exceptionSource: UncaughtExceptionSource) {
        return withContext(Dispatchers.IO) {
            if (e == lastSeenException) {
                return@withContext
            }

            Timber.e(e, "Uncaught exception - $exceptionSource")

            val rootCause = rootExceptionFinder.findRootException(e)
            val exceptionEntity = UncaughtExceptionEntity(
                message = rootCause.extractExceptionCause(),
                exceptionSource = exceptionSource,
                version = deviceInfo.appVersion
            )

            if (isNotDuplicate(exceptionEntity)) {
                uncaughtExceptionDao.add(exceptionEntity)
            }

            lastSeenException = e
        }
    }

    @VisibleForTesting
    fun isNotDuplicate(incomingException: UncaughtExceptionEntity): Boolean {

        val lastRecordedException = uncaughtExceptionDao.getLatestException() ?: return true

        if (incomingException.message == lastRecordedException.message &&
            incomingException.exceptionSource == lastRecordedException.exceptionSource &&
            incomingException.version == lastRecordedException.version
        ) {

            val timeDiff = incomingException.timestamp - lastRecordedException.timestamp

            return if (timeDiff > TIME_THRESHOLD_MILLIS) {
                true
            } else {
                uncaughtExceptionDao.update(lastRecordedException.copy(timestamp = incomingException.timestamp))
                false
            }
        }
        return true
    }

    override suspend fun getExceptions(): List<UncaughtExceptionEntity> {
        return withContext(Dispatchers.IO) {
            uncaughtExceptionDao.all()
        }
    }

    override suspend fun deleteException(id: Long) {
        return withContext(Dispatchers.IO) {
            uncaughtExceptionDao.delete(id)
        }
    }

    companion object {
        const val TIME_THRESHOLD_MILLIS = 1000L
    }
}
