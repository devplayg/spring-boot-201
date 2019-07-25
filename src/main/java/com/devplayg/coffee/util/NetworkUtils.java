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


//    private static boolean isValidNetwork(String net) {
//        try {
//            new SubnetUtils(net);
//            return true;
//        } catch (IllegalArgumentException e) {
//            return false;
//        }
//    }

}
