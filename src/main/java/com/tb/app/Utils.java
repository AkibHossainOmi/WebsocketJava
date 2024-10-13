//package telcobright.app;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Random;
//public class Utils {
//    public static String getPublicIP() throws InterruptedException {
//        String publicIP = "";
//        HttpURLConnection urlConnection = null;
//        try {
//            URL url = new URL("https://api.ipify.org?format=text"); // Using ipify service to get public IP
//            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.setConnectTimeout(5000); // Set timeout for connection
//            urlConnection.setReadTimeout(5000); // Set timeout for reading input
//            urlConnection.setDoOutput(false);
//            urlConnection.setDoInput(true);
//
//            int responseCode = urlConnection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                String inputLine;
//                StringBuilder response = new StringBuilder();
//
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
//                publicIP = response.toString();
//            } else {
//                // Handle the error response code appropriately
//                System.out.println(("Error: Unable to retrieve public IP. Response code: " + responseCode));
//            }
//        } catch (java.io.FileNotFoundException e) {
//            System.out.println(("FileNotFoundException: URL not found or server not reachable."+ e));
//        } catch (Exception e) {
//           e.printStackTrace();
//
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//        }
//        return reformatIP(publicIP);
//    }
//    private static String reformatIP(String ip) {
//        if (ip == null || ip.isEmpty()) {
//            return "";
//        }
//
//        String[] segments = ip.split("\\.");
//        StringBuilder reformattedIP = new StringBuilder();
//
//        for (String segment : segments) {
//            reformattedIP.append(String.format("%03d", Integer.parseInt(segment)));
//        }
//
//        return reformattedIP.toString();
//    }
//    public static String reformatPhoneNumber(String input) {
//        if (input == null || input.isEmpty()) {
//            return "";
//        }
//        String[] parts = input.split("@");
//        // Consider only the part before the "@" for digit extraction
//        String phonePart = parts[0];
//        // Removing any non-digit characters
//        String digits = phonePart.replaceAll("\\D", "");
//
//        // Checking the length of the digits and prefixing accordingly
//        if (digits.startsWith("880") && digits.length() == 13) {
//            return "00" + digits;
//        } else if (digits.startsWith("880") && digits.length() == 12) {
//            return "00880" + digits.substring(3);
//        } else if (digits.startsWith("0") && digits.length() == 11) {
//            return "00880" + digits.substring(1);
//        } else if (digits.length() == 10 && digits.startsWith("1")) {
//            return "00880" + digits;
//        }  else {
//            return "";
//        }
//    }
//    public static String TID() {
//        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//        int length = 12;
//        Random random = new Random();
//        String transactionID = new String();
//        for (int i = 0; i < length; i++) {
//            int index = random.nextInt(characters.length());
//            transactionID+=(characters.charAt(index));
//        }
//        return transactionID;
//    }
//}
//
//
