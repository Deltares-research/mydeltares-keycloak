package nl.deltares.keycloak.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.deltares.keycloak.storage.rest.MailingRepresentation;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class KeycloakUtilsImpl {

    private static final String KEYCLOAK_BASEURL_KEY = "keycloak.baseurl";
    private static final String KEYCLOAK_BASEAPIURL_KEY = "keycloak.baseapiurl";
    private static final String KEYCLOAK_ACCOUNT_PATH = "account";
    private static final String KEYCLOAK_USER_MAILING_PATH = "user-mailings";
    private static final String KEYCLOAK_MAILING_PATH = "mailing-provider";
    private static final String KEYCLOAK_AVATAR_PATH = "avatar-provider";
    private static final String KEYCLOAK_USERS_PATH = "users";

    private static final String KEYCLOAK_ADMIN_USER_MAILING_PATH = KEYCLOAK_USER_MAILING_PATH + "/admin";
    private static final String KEYCLOAK_ADMIN_MAILING_PATH = KEYCLOAK_MAILING_PATH + "/admin";
    private static final String KEYCLOAK_ADMIN_AVATAR_PATH = KEYCLOAK_AVATAR_PATH + "/admin";
    private static final String KEYCLOAK_OPENID_TOKEN_PATH = "protocol/openid-connect/token";
    private static final String KEYCLOAK_CLIENTID_KEY = "keycloak.clientid";
    private static final String KEYCLOAK_CLIENTSECRET_KEY = "keycloak.clientsecret";

    private final Properties properties;
    private String accessToken;
    private long expTimeMillis;

    public KeycloakUtilsImpl(File propertiesFile){
        this.properties = loadProperties(propertiesFile.getAbsolutePath());
    }

    public KeycloakUtilsImpl(Properties properties) {
        this.properties = properties;
    }

    public String getAccountPath() {
        String basePath = getKeycloakBasePath();
        return basePath + KEYCLOAK_ACCOUNT_PATH;
    }

    public String getAdminMailingPath() {
        String basePath = getKeycloakBasePath();
        return basePath + KEYCLOAK_ADMIN_MAILING_PATH;
    }

    public String getMailingPath() {
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

        HttpURLConnection urlConnection = getConnection(getUsersPath() + "?email=" + email, "GET", getAccessToken(), null);
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

        HttpURLConnection urlConnection = getConnection(getUsersPath() + "/" + id, "DELETE", getAccessToken(), null);
        checkResponse(urlConnection);

    }

    public byte[] getUserAvatarAdminApi(String email) throws IOException {

        HttpURLConnection urlConnection = getConnection(getAdminAvatarPath() + "?email=" + email, "GET", getAccessToken(), null);
        checkResponse(urlConnection);
        return readAllBytes(urlConnection.getInputStream());
    }

    public byte[] getUserAvatarUserApi(String userName, String password) throws IOException {
        HttpURLConnection urlConnection = getConnection(getAvatarPath(), "GET", getAccessToken(userName, password), null);
        checkResponse(urlConnection);
        return readAllBytes(urlConnection.getInputStream());
    }

    public int deleteUserAvatarAdminApi(String userId) throws IOException {
        return deleteUserAvatar(new URL(getAdminAvatarPath() + '/' + userId), getAccessToken());
    }

    public int deleteUserAvatarUserApi(String userName, String password) throws IOException {
        return deleteUserAvatar(new URL(getAvatarPath()), getAccessToken(userName, password));
    }

    public int uploadUserAvatarAdminApi(String userId, File portraitFile) throws IOException {
        return uploadUserAvatar(new URL(getAdminAvatarPath() + '/' + userId), portraitFile, getAccessToken());
    }

    public int uploadUserAvatarUserApi(File portraitFile, String username, String password) throws IOException {
        return uploadUserAvatar(new URL(getAvatarPath()), portraitFile, getAccessToken(username, password));
    }

    public String getUsers(int start, int maxResults) throws IOException {

        HttpURLConnection urlConnection = getConnection(getUsersPath() + "?first=" + start + "&max=" + maxResults, "GET", getAccessToken(), null);
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
     * @param mailingId dadd
     * @param csvUserIds dads
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
                    urlConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
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
                    batchCount += lineCount;
                    while ((lineRead = httpResponseReader.readLine()) != null) {
                        System.out.println(lineRead);
                        System.out.println("Line count=" + batchCount);
                    }
                    // Close the streams
                    outputStream.close();
                    httpRequestBodyWriter.close();
                    urlConnection = null;

                    System.out.println("Finish writing " + batchCount);
                }

            }

        } catch (Exception e){
            throw new IOException(String.format("Error importing user mailing %s: %s", mailingId, e.getMessage()));
        }

    }

    public MailingRepresentation getMailingAdminApi(String id) throws IOException {
        HttpURLConnection urlConnection = getConnection(getAdminMailingPath() + '/' + id, "GET", getAccessToken(), null);
        int response = checkResponse(urlConnection);
        if (response == Response.Status.NO_CONTENT.getStatusCode()) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(urlConnection.getInputStream(), mapper.getTypeFactory().constructType(MailingRepresentation.class));
    }

    public List<MailingRepresentation> getMailingsAdminApi(String search, String name) throws IOException {

        String path;
        if (search != null) {
            path = "?search=" + search;
        } else if (name != null){
            path = "?name=" + name;
        } else {
            path = "";
        }

        HttpURLConnection urlConnection = getConnection(getAdminMailingPath() + path, "GET", getAccessToken(), null);
        int response = checkResponse(urlConnection);
        if (response == Response.Status.NO_CONTENT.getStatusCode()) {
            return Collections.emptyList();
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(urlConnection.getInputStream(), mapper.getTypeFactory().constructCollectionType(List.class, MailingRepresentation.class));
    }

    public int createMailingAdminApi(MailingRepresentation mailing) throws IOException {

        HashMap<String, String> map = new HashMap<>();
        map.put("Content-Type", MediaType.APPLICATION_JSON);
        HttpURLConnection urlConnection = getConnection(getAdminMailingPath(), "POST", getAccessToken(), map);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(urlConnection.getOutputStream(), mailing);
        return checkResponse(urlConnection);
    }

    public int deleteMailingAdminApi(String id) throws IOException {
        HttpURLConnection urlConnection = getConnection(getAdminMailingPath() + '/' + id, "DELETE", getAccessToken(), null);
        return checkResponse(urlConnection);
    }

    public int updateMailingAdminApi(MailingRepresentation mailing) throws IOException {

        HashMap<String, String> map = new HashMap<>();
        map.put("Content-Type", MediaType.APPLICATION_JSON);
        HttpURLConnection urlConnection = getConnection(getAdminMailingPath(), "PUT", getAccessToken(), map);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(urlConnection.getOutputStream(), mailing);
        return checkResponse(urlConnection);
    }

    private int checkResponse(HttpURLConnection urlConnection) throws IOException {
        int responseCode = urlConnection.getResponseCode();
        if (responseCode > 299) {
            InputStream errorStream = urlConnection.getErrorStream();
            if (errorStream != null) {
                throw new IOException("Error " + responseCode + ": " + readAll(errorStream));
            } else {
                throw new IOException("Error " + responseCode + ": no message");
            }
        }
        return responseCode;
    }

    private HttpURLConnection getConnection(String path, String method, String accessToken, Map<String, String> props) throws IOException {
        URL url;
        try {
            url = new URL(path);
        } catch (MalformedURLException e) {
            throw new IOException(String.format("Invalid path %s: %s", path, e.getMessage()));
        }

        HttpURLConnection httpConnection = getHttpConnection(url, method, props);
        httpConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
        return httpConnection;
    }


    private int deleteUserAvatar(URL url, String accessToken) throws IOException {
        HttpURLConnection urlConnection = getHttpConnection(url, "DELETE", null);
        urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
        try {
            urlConnection.getInputStream();
            return urlConnection.getResponseCode();
        } catch (Exception e) {
            int responseCode = urlConnection.getResponseCode();
            InputStream errorStream = urlConnection.getErrorStream();
            if (errorStream != null) {
                throw new IOException("Error " + responseCode + ": " + readAll(errorStream));
            } else {
                throw new IOException("Error " + responseCode + ": no message");
            }
        }

    }

    private HttpURLConnection getHttpConnection(URL url, String method, Map<String, String> props) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(!method.equals("GET"));
        urlConnection.setRequestMethod(method);
        urlConnection.setConnectTimeout(1000);

        if (props != null) {
            Set<String> keys = props.keySet();
            for (String key : keys) {
                urlConnection.setRequestProperty(key, props.get(key));
            }
        }
        return urlConnection;
    }

    private String getTokenPath() {
        String basePath = getKeycloakBasePath();
        return basePath + KEYCLOAK_OPENID_TOKEN_PATH;
    }

    private String getAccessToken() {
        return getAccessToken(null, null);
    }

    private String getAccessToken(String resourceOwner, String resourceOwnerPw){

        if (resourceOwner == null) {
            String cachedToken = getCachedToken();
            if (cachedToken != null) return cachedToken;
        }

        try {
            String jsonResponse = getAccessTokenJson(
                    getTokenPath(),
                    getOpenIdClientId(),
                    getOpenIdClientSecret(),
                    resourceOwner,
                    resourceOwnerPw);
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

    private static String getAccessTokenJson(String tokenUrl, String clientId, String clientSec, String resourceOwner, String resourceOwnerPw) throws IOException {

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("grant_type", resourceOwner == null ? "client_credentials" : "password");
        params.put("client_id", clientId);
        params.put("client_secret", clientSec);
        if (resourceOwner != null){
            params.put("username", resourceOwner);
            params.put("password", resourceOwnerPw);
        }

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
        Map map = mapper.readValue(jsonResponse, Map.class);

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

    private int uploadUserAvatar(URL url, File portraitFile, String accessToken) throws IOException {
        String boundaryString = "----UploadAvatar";

        HashMap<String, String> map = new HashMap<>();
        map.put("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        HttpURLConnection urlConnection = getHttpConnection(url, "POST", map);
        urlConnection.setRequestProperty("Authorization", "Bearer " + accessToken);

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
        String fileName = portraitFile.getName();
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
        try {
            urlConnection.getInputStream();
            return urlConnection.getResponseCode();
        } catch (Exception e) {
            int responseCode = urlConnection.getResponseCode();
            InputStream errorStream = urlConnection.getErrorStream();
            if (errorStream != null) {
                throw new IOException("Error " + responseCode + ": " + readAll(errorStream));
            } else {
                throw new IOException("Error " + responseCode + ": no message");
            }
        }
    }

    private static Properties loadProperties(String arg) {
        try (InputStream input = new FileInputStream(arg)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            return prop;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
