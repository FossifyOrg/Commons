package com.simplemobiletools.commons.extensions

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import com.simplemobiletools.commons.helpers.EXTERNAL_STORAGE_PROVIDER_AUTHORITY
import com.simplemobiletools.commons.helpers.isRPlus
import com.simplemobiletools.commons.helpers.isSPlus
import com.simplemobiletools.commons.models.FileDirItem
import java.io.File

private const val DOWNLOAD_DIR = "Download"
private const val ANDROID_DIR = "Android"
private val DIRS_INACCESSIBLE_WITH_SAF_SDK_30 = if(isSPlus()) listOf(DOWNLOAD_DIR, ANDROID_DIR) else listOf(DOWNLOAD_DIR)

fun Context.hasProperStoredFirstParentUri(path: String): Boolean {
    val firstParentUri = createFirstParentTreeUri(path)
    return contentResolver.persistedUriPermissions.any { it.uri.toString() == firstParentUri.toString() }
}

fun Context.isAccessibleWithSAFSdk30(path: String): Boolean {
    if (path.startsWith(recycleBinPath) || isExternalStorageManager()) {
        return false
    }

    val level = getFirstParentLevel(path)
    val firstParentDir =  path.getFirstParentDirName(this, level)
    val firstParentPath =  path.getFirstParentPath(this, level)

    return firstParentDir != null && File(firstParentPath).isDirectory &&
        DIRS_INACCESSIBLE_WITH_SAF_SDK_30.all {
            !firstParentDir.equals(it, true)
        }
}

fun Context.getFirstParentLevel(path: String): Int {
    return when {
        isSPlus() && (isInAndroidDir(path) || isInDownloadDir(path)) -> 1
        isRPlus() && isInDownloadDir(path) -> 1
        else -> 0
    }
}

fun Context.isRestrictedWithSAFSdk30(path: String): Boolean {
    if (path.startsWith(recycleBinPath) || isExternalStorageManager()) {
        return true
    }

    val level = getFirstParentLevel(path)
    val firstParentDir =  path.getFirstParentDirName(this, level)
    val firstParentPath =  path.getFirstParentPath(this, level)

    return (firstParentDir == null || (File(firstParentPath).isDirectory &&
        DIRS_INACCESSIBLE_WITH_SAF_SDK_30.any {
            firstParentDir.equals(it, true)
        }))
}

fun Context.isInDownloadDir(path: String): Boolean {
    if (path.startsWith(recycleBinPath)) {
        return false
    }
    val firstParentDir = path.getFirstParentDirName(this, 0)
    return firstParentDir.equals(DOWNLOAD_DIR, true)
}

fun Context.isInAndroidDir(path: String): Boolean {
    if (path.startsWith(recycleBinPath)) {
        return false
    }
    val firstParentDir = path.getFirstParentDirName(this, 0)
    return firstParentDir.equals(ANDROID_DIR, true)
}

fun isNotExternalStorageManager(): Boolean {
    return isRPlus() && !Environment.isExternalStorageManager()
}

fun isExternalStorageManager(): Boolean {
    return isRPlus() && Environment.isExternalStorageManager()
}

fun Context.createFirstParentTreeUriUsingRootTree(fullPath: String): Uri {
    val storageId = getSAFStorageId(fullPath)
    val level = getFirstParentLevel(fullPath)
    val rootParentDirName = fullPath.getFirstParentDirName(this, level)
    val treeUri = DocumentsContract.buildTreeDocumentUri(EXTERNAL_STORAGE_PROVIDER_AUTHORITY, "$storageId:")
    val documentId = "${storageId}:$rootParentDirName"
    return DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
}

fun Context.createFirstParentTreeUri(fullPath: String): Uri {
    val storageId = getSAFStorageId(fullPath)
    val level = getFirstParentLevel(fullPath)
    val rootParentDirName = fullPath.getFirstParentDirName(this, level)
    val firstParentId = "$storageId:$rootParentDirName"
    return DocumentsContract.buildTreeDocumentUri(EXTERNAL_STORAGE_PROVIDER_AUTHORITY, firstParentId)
}

fun Context.createDocumentUriUsingFirstParentTreeUri(fullPath: String): Uri {
    val storageId = getSAFStorageId(fullPath)
    val relativePath = when {
        fullPath.startsWith(internalStoragePath) -> fullPath.substring(internalStoragePath.length).trim('/')
        else -> fullPath.substringAfter(storageId).trim('/')
    }
    val treeUri = createFirstParentTreeUri(fullPath)
    val documentId = "${storageId}:$relativePath"
    return DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
}

fun Context.getSAFDocumentId(path: String): String {
    val basePath = path.getBasePath(this)
    val relativePath = path.substring(basePath.length).trim('/')
    val storageId = getSAFStorageId(path)
    return "$storageId:$relativePath"
}

fun Context.createSAFDirectorySdk30(path: String): Boolean {
    return try {
        val treeUri = createFirstParentTreeUri(path)
        val parentPath = path.getParentPath()
        if (!getDoesFilePathExistSdk30(parentPath)) {
            createSAFDirectorySdk30(parentPath)
        }

        val documentId = getSAFDocumentId(parentPath)
        val parentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
        DocumentsContract.createDocument(contentResolver, parentUri, DocumentsContract.Document.MIME_TYPE_DIR, path.getFilenameFromPath()) != null
    } catch (e: IllegalStateException) {
        showErrorToast(e)
        false
    }
}

fun Context.createSAFFileSdk30(path: String): Boolean {
    return try {
        val treeUri = createFirstParentTreeUri(path)
        val parentPath = path.getParentPath()
        if (!getDoesFilePathExistSdk30(parentPath)) {
            createSAFDirectorySdk30(parentPath)
        }

        val documentId = getSAFDocumentId(parentPath)
        val parentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
        DocumentsContract.createDocument(contentResolver, parentUri, path.getMimeType(), path.getFilenameFromPath()) != null
    } catch (e: IllegalStateException) {
        showErrorToast(e)
        false
    }
}

fun Context.getDoesFilePathExistSdk30(path: String): Boolean {
    return when {
        isAccessibleWithSAFSdk30(path) -> getFastDocumentSdk30(path)?.exists() ?: false
        else -> File(path).exists()
    }
}

fun Context.getSomeDocumentSdk30(path: String): DocumentFile? = getFastDocumentSdk30(path) ?: getDocumentSdk30(path)

fun Context.getFastDocumentSdk30(path: String): DocumentFile? {
    val uri = createDocumentUriUsingFirstParentTreeUri(path)
    return DocumentFile.fromSingleUri(this, uri)
}

fun Context.getDocumentSdk30(path: String): DocumentFile? {
    val level = getFirstParentLevel(path)
    val firstParentPath = path.getFirstParentPath(this, level)
    var relativePath = path.substring(firstParentPath.length)
    if (relativePath.startsWith(File.separator)) {
        relativePath = relativePath.substring(1)
    }

    return try {
        val treeUri = createFirstParentTreeUri(path)
        var document = DocumentFile.fromTreeUri(applicationContext, treeUri)
        val parts = relativePath.split("/").filter { it.isNotEmpty() }
        for (part in parts) {
            document = document?.findFile(part)
        }
        document
    } catch (ignored: Exception) {
        null
    }
}


fun Context.deleteDocumentWithSAFSdk30(fileDirItem: FileDirItem, allowDeleteFolder: Boolean, callback: ((wasSuccess: Boolean) -> Unit)?) {
    try {
        var fileDeleted = false
        if (fileDirItem.isDirectory.not() || allowDeleteFolder) {
            val fileUri = createDocumentUriUsingFirstParentTreeUri(fileDirItem.path)
            fileDeleted = DocumentsContract.deleteDocument(contentResolver, fileUri)
        }

        if (fileDeleted) {
            deleteFromMediaStore(fileDirItem.path)
            callback?.invoke(true)
        }

    } catch (e: Exception) {
        callback?.invoke(false)
        showErrorToast(e)
    }
}

fun Context.renameDocumentSdk30(oldPath: String, newPath: String): Boolean {
    return try {
        val treeUri = createFirstParentTreeUri(oldPath)
        val documentId = getSAFDocumentId(oldPath)
        val parentUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, documentId)
        DocumentsContract.renameDocument(contentResolver, parentUri, newPath.getFilenameFromPath()) != null
    } catch (e: IllegalStateException) {
        showErrorToast(e)
        false
    }
}

fun Context.hasProperStoredDocumentUriSdk30(path: String): Boolean {
    val documentUri = buildDocumentUriSdk30(path)
    return contentResolver.persistedUriPermissions.any { it.uri.toString() == documentUri.toString() }
}

fun Context.buildDocumentUriSdk30(fullPath: String): Uri {
    val storageId = getSAFStorageId(fullPath)

    val relativePath = when {
        fullPath.startsWith(internalStoragePath) -> fullPath.substring(internalStoragePath.length).trim('/')
        else -> fullPath.substringAfter(storageId).trim('/')
    }

    val documentId = "${storageId}:$relativePath"
    return DocumentsContract.buildDocumentUri(EXTERNAL_STORAGE_PROVIDER_AUTHORITY, documentId)
}
