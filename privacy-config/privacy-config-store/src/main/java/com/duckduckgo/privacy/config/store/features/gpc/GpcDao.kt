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

package com.duckduckgo.privacy.config.store.features.gpc

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.duckduckgo.privacy.config.store.GpcExceptionEntity

@Dao
abstract class GpcDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(domains: List<GpcExceptionEntity>)

    @Transaction
    open fun updateAll(domains: List<GpcExceptionEntity>) {
        deleteAll()
        insertAll(domains)
    }

    @Query("select * from gpc_exceptions where domain = :domain")
    abstract fun get(domain: String): GpcExceptionEntity

    @Query("select * from gpc_exceptions") abstract fun getAll(): List<GpcExceptionEntity>

    @Query("delete from gpc_exceptions") abstract fun deleteAll()
}
