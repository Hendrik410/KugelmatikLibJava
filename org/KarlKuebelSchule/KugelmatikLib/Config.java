package org.KarlKuebelSchule.KugelmatikLib;

/**
 * Stellt Einstellungen für die Kugelmatik dar.
 */
public class Config {
    public static int ProtocolPort = 14804;
    public static short MaxHeight = 4000;
    public static byte DefaultWaitTime = 0;
    public static int KugelmatikWidth = 5;
    public static int KugelmatikHeight = 5;
    public static int ReceiveBufferLength = 255;
    public static boolean IgnoreGuaranteedWhenOffline = true;
    public static boolean IgnorePacketWhenOffline = false;
    public static int MinimumResendTimeout = 100;
}
