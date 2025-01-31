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

package com.duckduckgo.app.global

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.duckduckgo.app.global.plugins.PluginPoint
import com.duckduckgo.di.DaggerSet
import com.duckduckgo.di.scopes.AppScope
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import dagger.SingleInstanceIn

interface ActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}

private class ActivityLifecycleCallbacksPluginPoint(
    private val plugins: DaggerSet<ActivityLifecycleCallbacks>
) : PluginPoint<ActivityLifecycleCallbacks> {
    override fun getPlugins(): Collection<ActivityLifecycleCallbacks> {
        return plugins.sortedBy { it.javaClass.simpleName }
    }
}

@Module
@ContributesTo(AppScope::class)
class ActivityLifecycleCallbacksModule {
    @Provides
    @SingleInstanceIn(AppScope::class)
    fun provideActivityLifecycleCallbacksPluginPoint(
        plugins: DaggerSet<ActivityLifecycleCallbacks>
    ): PluginPoint<ActivityLifecycleCallbacks> = ActivityLifecycleCallbacksPluginPoint(plugins)
}
