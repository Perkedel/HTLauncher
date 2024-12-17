package com.perkedel.htlauncher.func

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import com.perkedel.htlauncher.data.SearchableApps

class AsyncService {
    // https://stackoverflow.com/questions/77550167/how-to-update-ui-on-async-callbacks-in-jetpack-compose
    // https://insert-koin.io/docs/setup/koin
    // https://mvnrepository.com/artifact/io.insert-koin/koin-androidx-compose
    // https://www.youtube.com/watch?v=CfL6Dl2_dAE
    // https://stackoverflow.com/questions/70324983/get-context-in-a-jetpack-compose-noncomposable-function
    // https://medium.com/@mahbooberezaee68/search-in-list-in-jetpack-compose-19bdb74ab220
    // https://blog.logrocket.com/create-static-methods-classes-kotlin/
    // https://stackoverflow.com/a/13027415/9079640

    suspend fun getSearchableApps(fromThe:List<PackageInfo>, pm:PackageManager):List<SearchableApps>{
        return fromThe.map {
            SearchableApps(
                packageName = it.packageName,
                label = it.applicationInfo?.loadLabel(pm).toString()
            )
        }
    }
    suspend fun getSearchableAppsResolve(fromThe:List<ResolveInfo>, pm:PackageManager):List<SearchableApps>{
        return fromThe.map {
            SearchableApps(
                packageName = it.activityInfo.packageName,
                label = it.loadLabel(pm).toString()
            )
        }
    }
    suspend fun appendSearchableApps(base:List<SearchableApps>, fromThe:List<PackageInfo>, pm:PackageManager):List<SearchableApps>{
        val compiled = base.toMutableList()
        for (i in fromThe) {
            if (base.contains(
                    SearchableApps(
                        packageName = i.packageName,
                        label = i.applicationInfo?.loadLabel(pm).toString()
                    )
                )
            ) {
                continue
            }
            compiled.add(
                SearchableApps(
                    packageName = i.packageName,
                    label = i.applicationInfo?.loadLabel(pm).toString()
                )
            )
        }
        return compiled
    }
    suspend fun appendSearchableAppsResolve(base:List<SearchableApps>, fromThe:List<ResolveInfo>, pm:PackageManager):List<SearchableApps>{
        val compiled = base.toMutableList()
        for (i in fromThe) {
            if (base.contains(
                    SearchableApps(
                        packageName = i.activityInfo.packageName,
                        label = i.loadLabel(pm).toString()
                    )
                )
            ) {
                continue
            }
            compiled.add(
                SearchableApps(
                    packageName = i.activityInfo.packageName,
                    label = i.loadLabel(pm).toString()
                )
            )
        }
        return compiled
    }
}