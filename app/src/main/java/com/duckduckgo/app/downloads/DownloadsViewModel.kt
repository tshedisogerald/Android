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

package com.duckduckgo.app.downloads

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duckduckgo.app.downloads.model.DownloadItem
import com.duckduckgo.app.downloads.model.DownloadsRepository
import com.duckduckgo.app.global.plugins.view_model.ViewModelFactoryPlugin
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesMultibinding
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Provider

class DownloadsViewModel @Inject constructor(private val downloadsRepository: DownloadsRepository) :
    ViewModel() {

    data class ViewState(val downloadItems: List<DownloadViewItem>)

    fun downloads(): StateFlow<ViewState> = flow {
        downloadsRepository.getDownloads().collect {
            emit(ViewState(downloadItems = it.mapToDownloadViewItems()))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ViewState(downloadItems = listOf(
        DownloadViewItem.Empty
    )))

    private fun DownloadItem.mapToDownloadViewItem(): DownloadViewItem =
        DownloadViewItem.Item(this)

    private fun List<DownloadItem>.mapToDownloadViewItems(): List<DownloadViewItem> =
        this.map { it.mapToDownloadViewItem() }
}

@ContributesMultibinding(AppScope::class)
class DownloadsViewModelFactory
@Inject
constructor(private val viewModel: Provider<DownloadsViewModel>) : ViewModelFactoryPlugin {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T? {
        with(modelClass) {
            return when {
                isAssignableFrom(DownloadsViewModel::class.java) -> viewModel.get() as T
                else -> null
            }
        }
    }
}
