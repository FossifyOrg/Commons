package org.fossify.commons.samples

import com.github.ajalt.reprint.core.Reprint
import org.fossify.commons.FossifyApp

class App : FossifyApp() {
    override fun onCreate() {
        super.onCreate()
        Reprint.initialize(this)
    }
}
