package org.fossify.commons.extensions

import android.content.Context
import org.fossify.commons.models.FileDirItem

fun FileDirItem.isRecycleBinPath(context: Context): Boolean {
    return path.startsWith(context.recycleBinPath)
}
