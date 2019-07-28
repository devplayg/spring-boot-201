package com.devplayg.coffee.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkUtils {
    public static List<String> splitNetworks(String text, String delimiter) throws IllegalArgumentException {
        return Arrays.stream(text.split(delimiter))

                // Convert IP address to CIDR notation format
                .map(s -> {
                    s = s.trim();
                    return s + (s.contains("/") ? "" : "/32");
                })

                // Filters
                .filter(network -> network.length() >= 10) // "1.1.1.1/32"'s  length is 10

                // Validate network
                .map(network -> {
                    new SubnetUtils(network);
                    return network;
                })

                // Return
                .collect(Collectors.toList());
    }

    public static long ipToLong(String ipAddress) {
        long result = 0;
        String[] ipAddressInArray = ipAddress.split("\\.");
        for (int i = 3; i >= 0; i--) {
            long ip = Long.parseLong(ipAddressInArray[3 - i]);
            result |= ip << (i * 8);
        }
        return result;
    }

    public static String longToIp(long ip) {
        return ((ip >> 24) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + (ip & 0xFF);
    }
}
