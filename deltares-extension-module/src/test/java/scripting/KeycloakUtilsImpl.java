package scripting;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class KeycloakUtilsImpl {

    private static final String KEYCLOAK_BASEURL_KEY = "keycloak.baseurl";
    private static final String KEYCLOAK_BASEAPIURL_KEY = "keycloak.baseapiurl";
    private static final String KEYCLOAK_ACCOUNT_PATH = "account";
    private static final String KEYCLOAK_MAILING_PATH = "user-mailings";
    private static final String KEYCLOAK_AVATAR_PATH = "avatar-provider";
    private static final String KEYCLOAK_USERS_PATH = "users";

    private static final String KEYCLOAK_ADMIN_AVATAR_PATH = "avatar-provider/admin";
    private static final String KEYCLOAK_OPENID_TOKEN_PATH = "protocol/openid-connect/token";
    private static final String KEYCLOAK_CLIENTID_KEY = "keycloak.clientid";
    private static final String KEYCLOAK_CLIENTSECRET_KEY = "keycloak.clientsecret";

    private final Properties properties;
    private String accessToken;
    private long expTimeMillis;

    public KeycloakUtilsImpl(Properties properties) {
        this.properties = properties;
    }

    public String getAccountPath() {
        String basePath = getKeycloakBasePath();
        return basePath + KEYCLOAK_ACCOUNT_PATH;
    }

    String getMailingPath() {
        String basePath = getKeycloakBasePath();
        return basePath + KEYCLOAK_MAILING_PATH;
    }

    public String getAvatarPath() {
        String basePath = getKeycloakBasePath();
        return basePath + KEYCLOAK_AVATAR_PATH;
    }

    public String getAdminAvatarPath() {
        String basePath = getKeycloakBasePath();
        return basePath + KEYCLOAK_ADMIN_AVATAR_PATH;
    }

    public String getUsersPath() {
        String basePath = getKeycloakBaseApiPath();
        return basePath + KEYCLOAK_USERS_PATH;
    }

    public String getUserId(String email) throws IOException {
        URL url;
        try {
            url = new URL(getUsersPath() + "?email=" + email);
        } catch (MalformedURLException e) {
            System.out.println(String.format("Invalid path %s: %s", getUsersPath(), e.getMessage()));
            return null;
        }

        HttpURLConnection urlConnection = getHttpConnection(url, "GET", null);
        int responseCode = urlConnection.getResponseCode();
        if (responseCode > 299) {
            InputStream errorStream = urlConnection.getErrorStream();
            if (errorStream != null) {
                throw new IOException(String.format("Error getUserId for %s: %s %s", email, responseCode, readAll(errorStream)));
            } else {
                throw new IOException(String.format("Error getUserId for %s: %s", email, responseCode));
            }
        }
        String jsonResponse = readAll(urlConnection.getInputStream());
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> map = mapper.readValue(jsonResponse, mapper.getTypeFactory().constructCollectionType(List.class, Map.class));
        if (map.size() > 0) {
            return (String) map.get(0).get("id");
        }
        return null;
    }

    public void deleteUser(String id) throws IOException {

        URL url = new URL(getUsersPath() + "/" + id);
        HttpURLConnection urlConnection = getHttpConnection(url, "DELETE", null);
        int responseCode = urlConnection.getResponseCode();
        if (responseCode > 299) {
            InputStream errorStream = urlConnection.getErrorStream();
            if (errorStream != null) {
                throw new IOException("Error " + responseCode + ": " + readAll(errorStream));
            } else {
                throw new IOException("Error " + responseCode + ": no message");
            }
        }

    }

    public byte[] getUserAvatar(String email) {
        URL url;
        try {
            url = new URL(getAdminAvatarPath() + "?email=" + email);
        } catch (MalformedURLException e) {
            System.out.println(String.format("Invalid path %s: %s", getAdminAvatarPath(), e.getMessage()));
            return null;
        }

        try {
            HttpURLConnection urlConnection = getHttpConnection(url, "GET", null);
            return readAllBytes(urlConnection.getInputStream());
        } catch (IOException e) {
            System.out.println(String.format("Error getting user avatar for %s: %s", email, e.getMessage()));
        }
        return null;
    }

    private HttpURLConnection getHttpConnection(URL url, String method, Map<String, String> props) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(method != "GET");
        urlConnection.setRequestMethod(method);
        urlConnection.setConnectTimeout(1000);

        if (props != null) {
            Set<String> keys = props.keySet();
            for (String key : keys) {
                urlConnection.setRequestProperty(key, props.get(key));
            }
        }
        urlConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        return urlConnection;
    }

    private String getTokenPath() {
        String basePath = getKeycloakBasePath();
        return basePath + KEYCLOAK_OPENID_TOKEN_PATH;
    }

    private String getAccessToken() {

        String cachedToken = getCachedToken();
        if (cachedToken != null) return cachedToken;

        try {
            String jsonResponse = getAccessTokenJson(
                    getTokenPath(),
                    getOpenIdClientId(),
                    getOpenIdClientSecret());
            return parseTokenJson(jsonResponse);

        } catch (IOException e) {
            System.out.println("Failed to get access token: " + e.getMessage());
        }
        return null;
    }


    private String getOpenIdClientId() {
        return properties.getProperty(KEYCLOAK_CLIENTID_KEY);
    }

    private String getOpenIdClientSecret() {
        return properties.getProperty(KEYCLOAK_CLIENTSECRET_KEY);
    }

    private String getKeycloakBasePath() {
        String basePath = properties.getProperty(KEYCLOAK_BASEURL_KEY);

        if (basePath.endsWith("/")) {
            return basePath;
        }
        return basePath + '/';
    }

    private String getKeycloakBaseApiPath() {
        String basePath = properties.getProperty(KEYCLOAK_BASEAPIURL_KEY);

        if (basePath.endsWith("/")) {
            return basePath;
        }
        return basePath + '/';
    }

    private static String getAccessTokenJson(String tokenUrl, String clientId, String clientSec) throws IOException {

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("grant_type", "client_credentials");
        params.put("client_id", clientId);
        params.put("client_secret", clientSec);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8.name()));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), StandardCharsets.UTF_8.name()));
        }
        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

        URL url = new URL(tokenUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);

        urlConnection.getOutputStream().write(postDataBytes);
        int responseCode = urlConnection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Error " + responseCode + ": " + readAll(urlConnection.getErrorStream()));
        }
        return readAll(urlConnection.getInputStream());

    }

    private static String readAll(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        // StandardCharsets.UTF_8.name() > JDK 7
        return result.toString("UTF-8");
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        // StandardCharsets.UTF_8.name() > JDK 7
        return result.toByteArray();
    }

    private String parseTokenJson(String jsonResponse) throws IOException {
        // parsing file "JSONExample.json"
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(jsonResponse, Map.class);

        long expMillis = (Integer)map.get("expires_in") * 1000;
        expTimeMillis = expMillis + System.currentTimeMillis();

        accessToken = (String) map.get("access_token");
        return accessToken;
    }

    private String getCachedToken() {
        if (accessToken != null) {
            if (expTimeMillis > System.currentTimeMillis()) {
                return accessToken;
            }
        }
        return null;
    }

    public void uploadUserAvatar(String userId, File portraitFile) throws IOException {

        URL url = new URL(getAdminAvatarPath() + '/' + userId);
        String boundaryString = "----UploadAvatar";

        Map map = new HashMap();
        map.put("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        HttpURLConnection urlConnection = getHttpConnection(url, "POST", map);

        OutputStream outputStream = urlConnection.getOutputStream();
        BufferedWriter httpRequestBodyWriter =
                new BufferedWriter(new OutputStreamWriter(outputStream));

//            // Include value from the myFileDescription text area in the post data
//            httpRequestBodyWriter.write("\n\n--" + boundaryString + "\n");
//            httpRequestBodyWriter.write("Content-Disposition: form-data; name=\"userAvatar\"");
//            httpRequestBodyWriter.write("\n\n");
//

        // Include the section to describe the file
        httpRequestBodyWriter.write("\n--" + boundaryString + "\n");
        String fileName = portraitFile.getParentFile().getName();
        httpRequestBodyWriter.write("Content-Disposition: form-data;"
                + "name=\"image\";"
                + "filename=\"" + fileName + "\""
                + "\nContent-Type: " +  URLConnection.guessContentTypeFromName(fileName) + "\n\n");
        httpRequestBodyWriter.flush();

        // Write the actual file contents
        FileInputStream inputStreamToLogFile = new FileInputStream(portraitFile);

        int bytesRead;
        byte[] dataBuffer = new byte[1024];
        while ((bytesRead = inputStreamToLogFile.read(dataBuffer)) != -1) {
            outputStream.write(dataBuffer, 0, bytesRead);
        }

        outputStream.flush();

        // Mark the end of the multipart http request
        httpRequestBodyWriter.write("\n--" + boundaryString + "--\n");
        httpRequestBodyWriter.flush();

        // Close the streams
        outputStream.close();
        httpRequestBodyWriter.close();


        // Read response from web server, which will trigger the multipart HTTP request to be sent.
        BufferedReader httpResponseReader =
                new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String lineRead;
        while((lineRead = httpResponseReader.readLine()) != null) {
            System.out.println(lineRead);
        }
    }

    public String getUsers(int start, int maxResults) throws IOException {
        URL url;
        try {
            url = new URL(getUsersPath() + "?first=" + start + "&max=" + maxResults);
        } catch (MalformedURLException e) {
            System.out.println(String.format("Invalid path %s: %s", getUsersPath(), e.getMessage()));
            return null;
        }

        HttpURLConnection urlConnection = getHttpConnection(url, "GET", null);
        int responseCode = urlConnection.getResponseCode();
        if (responseCode > 299) {
            InputStream errorStream = urlConnection.getErrorStream();
            if (errorStream != null) {
                throw new IOException(String.format("Error getUsers : %s %s", responseCode, readAll(errorStream)));
            } else {
                throw new IOException(String.format("Error getUsers : %s", responseCode));
            }
        }
        return readAll(urlConnection.getInputStream());
    }

    /**
     * Write usermailings for users in input file to the server. Input needs to be chunked to avoid exceptions.
     * @param mailingId
     * @param csvUserIds
     * @throws IOException
     */
    public void importUserMailings(String mailingId, File csvUserIds) throws IOException {

        URL url = new URL(getMailingPath() + "/admin/import/" + mailingId);
        String boundaryString = "----ImportUserMailings";

        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "multipart/form-data; boundary=" + boundaryString);

        // Write the actual file contents
        int maxLinesPerRequest = 1000;
        int lineCount = 0;
        int batchCount = 0;
        HttpURLConnection urlConnection = null;
        OutputStream outputStream = null ;
        BufferedWriter httpRequestBodyWriter = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvUserIds))) {
            String line = reader.readLine();
            while (line != null) {
                if (urlConnection == null) {
                    //---- open new request -----//
                    urlConnection = getHttpConnection(url, "POST", map);
                    outputStream = urlConnection.getOutputStream();
                    httpRequestBodyWriter =
                            new BufferedWriter(new OutputStreamWriter(outputStream));

                    // Include the section to describe the file
                    httpRequestBodyWriter.write("\n--" + boundaryString + "\n");
                    httpRequestBodyWriter.write("Content-Disposition: form-data;"
                            + "name=\"data\";"
                            + "filename=\"" + csvUserIds + "\""
                            + "\nContent-Type: text/csv\n\n");
                    httpRequestBodyWriter.flush();
                    lineCount = 0;

                }

                outputStream.write(line.getBytes());
                outputStream.write('\n');
                lineCount++;

                line = reader.readLine();

                if (lineCount == maxLinesPerRequest || line == null) {
                    //----  send request ---//
                    // Mark the end of the multipart http request
                    httpRequestBodyWriter.write("\n--" + boundaryString + "--\n");
                    httpRequestBodyWriter.flush();
                    // Read response from web server, which will trigger the multipart HTTP request to be sent.
                    BufferedReader httpResponseReader =
                            new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String lineRead;
                    while ((lineRead = httpResponseReader.readLine()) != null) {
                        System.out.println(lineRead);
                    }

                    // Close the streams
                    outputStream.close();
                    httpRequestBodyWriter.close();
                    urlConnection = null;
                    batchCount += lineCount;
                    System.out.println("Finish writing " + batchCount);
                }

            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
