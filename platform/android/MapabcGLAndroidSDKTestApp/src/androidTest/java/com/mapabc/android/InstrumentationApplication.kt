package com.mapabc.android

class InstrumentationApplication : com.mapabc.android.testapp.MapLibreApplication() {
    fun initializeLeakCanary(): Boolean {
        // do not initialize leak canary during instrumentation tests
        return true
    }
}
