package com.tb.calling.jingle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IceCandidateExtractor {

    public static String extractIpAndPort(String xml) {
        // Regular expression to match the <candidate> tag and extract the ip and port attributes
        String regex = "<candidate[^>]+ip=['\"]([^'\"]+)['\"][^>]+port=['\"](\\d+)['\"][^>]*>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(xml);

        if (matcher.find()) {
            String ip = matcher.group(1);
            String port = matcher.group(2);
            return ip + ":" + port;  // Return in "ip:port" format
        }

        return null; // Return null if no match is found
    }

}
