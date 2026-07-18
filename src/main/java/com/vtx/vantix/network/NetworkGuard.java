package com.vtx.vantix.network;

import com.vtx.vantix.core.VNTXConfig;

public class NetworkGuard {

    private NetworkGuard() {
    }

    // Returns false if all networking is disabled
    public static boolean networkingEnabled() {
        if (VNTXConfig.feature == null) return false;
        return !VNTXConfig.feature.network.offlineMode;
    }

    // Telemetry: username, mod list, version sent on server join
    public static boolean telemetryAllowed() {
        if (!networkingEnabled()) return false;
        return !VNTXConfig.feature.network.disableTelemetry;
    }

    // Mod list specifically within telemetry
    public static boolean modListInTelemetryAllowed() {
        if (!telemetryAllowed()) return false;
        return !VNTXConfig.feature.network.disableModListInTelemetry;
    }

    // API calls: capes, profile viewer, supabase, profile parser
    public static boolean apiAllowed() {
        if (!networkingEnabled()) return false;
        return !VNTXConfig.feature.network.disableApiCalls;
    }

    // GitHub calls: repo data used by most mod features
    public static boolean githubAllowed() {
        if (!networkingEnabled()) return false;
        return !VNTXConfig.feature.network.disableGithubCalls;
    }
}