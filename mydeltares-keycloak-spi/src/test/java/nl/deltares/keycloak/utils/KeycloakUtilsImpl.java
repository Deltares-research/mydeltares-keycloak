package nl.deltares.keycloak.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.representations.idm.UserRepresentation;

import java.io.*;
import java.net.*;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class KeycloakUtilsImpl {

    private static final String KEYCLOAK_BASEURL_KEY = "keycloak.baseurl";
    private static final String KEYCLOAK_BASEAPIURL_KEY = "keycloak.baseapiurl";
    private static final String KEYCLOAK_ATTRIBUTE_PATH = "user-attributes";
    private static final String KEYCLOAK_AVATAR_PATH = "avatar-provider";
    private static final String KEYCLOAK_USERS_PATH = "users";
    private static final String KEYCLOAK_USERS_DELTARES_PATH = "users-deltares";
    private static final String KEYCLOAK_OPENID_TOKEN_PATH = "protocol/openid-connect/token";
    private static final String KEYCLOAK_CLIENTID_KEY = "keycloak.clientid";
    private static final String KEYCLOAK_CLIENTSECRET_KEY = "keycloak.clientsecret";

    private final Properties properties;
    private String accessToken;
    private long expTimeMillis;

    public KeycloakUtilsImpl(File propertiesFile) {
        this.properties = loadProperties(propertiesFile.getAbsolutePath());
    }

    public KeycloakUtilsImpl(Properties properties) {
        this.properties = properties;
    }

    private String getAdminUserAttributePath() {
        String basePath = getKeycloakBasePath();
        return basePath + KEYCLOAK_ATTRIBUTE_PATH;
    }

    private String getAvatarPath() {
        String basePath = getKeycloakBasePath();
        return basePath + KEYCLOAK_AVATAR_PATH;
    }

    private String getUsersPath() {
        String basePath = getKeycloakBaseApiPath();
        return basePath + KEYCLOAK_USERS_PATH;
    }

    private String getUsersDeltaresPath() {
        String basePath = getKeycloakBasePath();
        return basePath + KEYCLOAK_USERS_DELTARES_PATH;
    }

    public List<UserRepresentation> searchUser(String searchString) throws IOException {

        HttpURLConnection urlConnection = getConnection(getUsersPath() + "?search=" + searchString, "GET", getAccessToken(), null);
        int response = checkResponse(urlConnection);
        if (response == Response.Status.NO_CONTENT.getStatusCode()) {
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = urlConnection.getInputStream()) {
            return mapper.readValue(inputStream, mapper.getTypeFactory().constructCollectionType(List.class, UserRepresentation.class));
        } finally {
            urlConnection.disconnect();
        }
    }

    public String sayHello() throws IOException {
        HttpURLConnection urlConnection = getConnection(getAvatarPath() + "/hello", "GET", getAccessToken(), null);
        return readAll(urlConnection);
    }

    public void updateUser(UserRepresentation user) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        map.put("Content-Type", MediaType.APPLICATION_JSON);
        HttpURLConnection urlConnection = getConnection(getUsersPath() + '/' + user.getId(), "PUT", getAccessToken(), map);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(urlConnection.getOutputStream(), user);
        checkResponse(urlConnection);
    }

    public UserRepresentation getUserByEmail(String email) throws IOException {

        HttpURLConnection urlConnection = getConnection(getUsersPath() + "?email=" + email, "GET", getAccessToken(), null);
        int responseCode = urlConnection.getResponseCode();
        if (responseCode > 299) {
            throw new IOException(String.format("Error getUserId for %s: %s %s", email, responseCode, readError(urlConnection)));
        }
        String jsonResponse = readAll(urlConnection);
        ObjectMapper mapper = new ObjectMapper();
        List<UserRepresentation> map = mapper.readValue(jsonResponse, mapper.getTypeFactory().constructCollectionType(List.class, UserRepresentation.class));
        if (!map.isEmpty()) {
            return map.get(0);
        }
        return null;
    }

    public void deleteUser(String id) throws IOException {
        HttpURLConnection urlConnection = getConnection(getUsersPath() + "/" + id, "DELETE", getAccessToken(), null);
        checkResponse(urlConnection);
    }

    public byte[] getUserAvatarApiById(String id) throws IOException {

        HttpURLConnection urlConnection = getConnection(getAvatarPath() + "/" + id, "GET", getAccessToken(), null);
        checkResponse(urlConnection);
        return readAllBytes(urlConnection.getInputStream());
    }

    public byte[] getUserAvatarApiByEmail(String email) throws IOException {

        HttpURLConnection urlConnection = getConnection(getAvatarPath() + "?email=" + email, "GET", getAccessToken(), null);
        checkResponse(urlConnection);
        return readAllBytes(urlConnection.getInputStream());
    }

    public int deleteUserAvatarApi(String userId) throws IOException {
        return deleteUserAvatar(new URL(getAvatarPath() + '/' + userId), getAccessToken());
    }

    public String uploadCheckUsersExistAdminApi(File dataFile) throws IOException {

        HttpClient httpclient = HttpClientBuilder.create().build();
        final HttpPost httpPost = new HttpPost(getUsersDeltaresPath() + "/check-users-exist");
//        httpPost.addHeader("Content-Type", "multipart/form-data");
        httpPost.addHeader("Authorization", "Bearer " + accessToken);

//        final FileBody fileBody = new FileBody(dataFile, Files.probeContentType(dataFile.toPath()));
        final MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        reqEntity.addBinaryBody("data", dataFile, ContentType.TEXT_PLAIN, "data.csv");
        httpPost.setEntity(reqEntity.build());

        final org.apache.http.HttpResponse response = httpclient.execute(httpPost);

        // Read response from web server, which will trigger the multipart HTTP request to be sent.
        return readAll(response.getEntity().getContent());
    }

    public int uploadUserAvatarApi(String userId, File portraitFile) throws Exception {
        return uploadUserAvatar(getAvatarPath() + '/' + userId, portraitFile, getAccessToken());
    }

    public void exportInvalidUsers(Writer writer) throws IOException {

        HashMap<String, String> map = new HashMap<>();
        map.put("Content-Type", MediaType.TEXT_HTML);
        HttpURLConnection urlConnection = getConnection(getUsersDeltaresPath() + "/invalid", "GET", getAccessToken(), map);

        checkResponse(urlConnection);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write('\n');
            }
            writer.flush();
        }
    }

    public String getUserAsJson(String id) throws IOException {
        HttpURLConnection urlConnection = getConnection(getUsersPath() + "/" + id, "GET", getAccessToken(), null);
        checkResponse(urlConnection);
        return readAll(urlConnection);
    }

    public String getUsersAdminApi(int start, int maxResults, String search) throws IOException {

        String path = getUsersPath() + "?first=" + start + "&max=" + maxResults;
        if (search != null) {
            path += "&search=" + search;
        }
        HttpURLConnection urlConnection = getConnection(path, "GET", getAccessToken(), null);
        int responseCode = urlConnection.getResponseCode();
        if (responseCode > 299) {
            throw new IOException(String.format("Error getUsers : %s %s", responseCode, readError(urlConnection)));
        }
        return readAll(urlConnection);
    }

    public String createUser(UserRepresentation user) throws IOException {

        HashMap<String, String> map = new HashMap<>();
        map.put("Content-Type", MediaType.APPLICATION_JSON);
        HttpURLConnection urlConnection = getConnection(getUsersPath(), "POST", getAccessToken(), map);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(urlConnection.getOutputStream(), user);
        checkResponse(urlConnection);

        final UserRepresentation userByEmail = getUserByEmail(user.getEmail());
        return userByEmail.getId();
    }

    public int exportUserAttributesAdminApi(Writer writer, String search) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        map.put("Content-Type", MediaType.TEXT_HTML);
        HttpURLConnection urlConnection = getConnection(getAdminUserAttributePath() + "/export?search=" + search, "GET", getAccessToken(), map);

        int status = checkResponse(urlConnection);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write('\n');
            }
            writer.flush();
        }
        return status;

    }

    private int checkResponse(HttpURLConnection urlConnection) throws IOException {
        int responseCode = urlConnection.getResponseCode();
        if (responseCode > 299) {
            throw new IOException("Error " + responseCode + ": " + readError(urlConnection));

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
        return readResponse(urlConnection);

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

        String cachedToken = getCachedToken();
        if (cachedToken != null) return cachedToken;

        try {
            String jsonResponse = getAccessTokenJson(
                    getTokenPath(),
                    getOpenIdClientId(),
                    getOpenIdClientSecret(),
                    null,
                    null);
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

    private static String getAccessTokenJson(String tokenUrl, String clientId, String clientSec,
                                             @SuppressWarnings("SameParameterValue") String resourceOwner,
                                             @SuppressWarnings("SameParameterValue") String resourceOwnerPw) throws IOException {

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("grant_type", resourceOwner == null ? "client_credentials" : "password");
        params.put("client_id", clientId);
        params.put("client_secret", clientSec);
        if (resourceOwner != null) {
            params.put("username", resourceOwner);
            params.put("password", resourceOwnerPw);
        }

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (!postData.isEmpty()) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), StandardCharsets.UTF_8));
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
            throw new IOException("Error " + responseCode + ": " + readError(urlConnection));
        }
        return readAll(urlConnection);

    }

    private static String readError(HttpURLConnection connection) throws IOException {

        try (InputStream inputStream = connection.getErrorStream()) {
            if (inputStream == null) return connection.getResponseMessage();
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            // StandardCharsets.UTF_8.name() > JDK 7
            return result.toString(StandardCharsets.UTF_8);
        }

    }

    private static String readAll(HttpURLConnection connection) throws IOException {
        return readAll(connection.getInputStream());
    }

    private static String readAll(InputStream inputStream) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        // StandardCharsets.UTF_8.name() > JDK 7
        return result.toString(StandardCharsets.UTF_8);

    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toByteArray();
    }

    private String parseTokenJson(String jsonResponse) throws IOException {
        // parsing file "JSONExample.json"
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(jsonResponse, Map.class);

        long expMillis = (Integer) map.get("expires_in") * 1000;
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

    private int uploadUserAvatar(String url, File portraitFile, String accessToken) throws IOException {

        HttpClient httpclient = HttpClientBuilder.create().build();
        final HttpPost httpPost = new HttpPost(url);
//        httpPost.addHeader("Content-Type", "multipart/form-data");
        httpPost.addHeader("Authorization", "Bearer " + accessToken);

//        final FileBody fileBody = new FileBody(portraitFile, Files.probeContentType(portraitFile.toPath()));
        final MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
        final ContentType contentType = ContentType.create(Files.probeContentType(portraitFile.toPath()));
        reqEntity.addBinaryBody("image", portraitFile, contentType, portraitFile.getName());
        httpPost.setEntity(reqEntity.build());

        final org.apache.http.HttpResponse response = httpclient.execute(httpPost);

        return response.getStatusLine().getStatusCode();

    }

    public void uploadTestUserAvatar(String userId, File portraitFile) throws Exception {

        String boundary = "UploadAvatarBoundary";
        Map<Object, Object> data = new LinkedHashMap<>();
        data.put("image", portraitFile.toPath());

        final java.net.http.HttpClient client = java.net.http.HttpClient.newBuilder().build();

        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getAvatarPath() + '/' + userId))
                .header("Authorization", "Bearer " + getAccessToken())
                .header("Content-Type", "multipart/form-data;boundary=" + boundary)
                .POST(ofMimeMultipartData(data, boundary))
                .build();

        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() !=204) {
            throw new IOException("Error " + response.statusCode() + ": " + response.body());
        }
    }

    public static HttpRequest.BodyPublisher ofMimeMultipartData(Map<Object, Object> data,
                                                                String boundary) throws IOException {
        var byteArrays = new ArrayList<byte[]>();
        byte[] separator = ("\r\n--" + boundary + "\r\nContent-Disposition: form-data; name=")
                .getBytes(StandardCharsets.UTF_8);
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            byteArrays.add(separator);

            if (entry.getValue() instanceof Path path) {
                String mimeType = Files.probeContentType(path);
                byteArrays.add(("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName()
                        + "\"\r\nContent-Type: " + mimeType + "\r\n\r\n")
                        .getBytes(StandardCharsets.UTF_8));
                byteArrays.add(Files.readAllBytes(path));
            }
            else {
                byteArrays.add(("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue())
                        .getBytes(StandardCharsets.UTF_8));
            }
        }
        byteArrays.add(("\r\n--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));
        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }

    private int readResponse(HttpURLConnection urlConnection) throws IOException {
        try (InputStream ignored = urlConnection.getInputStream()) {
            return urlConnection.getResponseCode();
        } catch (Exception e) {
            int responseCode = urlConnection.getResponseCode();
            throw new IOException("Error " + responseCode + ": " + readError(urlConnection));
        } finally {
            urlConnection.disconnect();
        }
    }

    private static Properties loadProperties(String arg) {
        try (InputStream input = new FileInputStream(arg)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            String portProp = System.getProperty("jboss.http.port");
            if (portProp != null) {
                int httpPort = Integer.parseInt(portProp);
                if (httpPort != 8080) {
                    String baseUrl = prop.getProperty("keycloak.baseurl");
                    prop.setProperty("keycloak.baseurl", baseUrl.replace("8080", portProp));
                    String apiUrl = prop.getProperty("keycloak.baseapiurl");
                    prop.setProperty("keycloak.baseapiurl", apiUrl.replace("8080", portProp));
                }
            }
            return prop;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String countUsers() throws IOException {

        HttpURLConnection urlConnection = getConnection(getUsersPath() + "/count", "GET", getAccessToken(), null);
        int responseCode = urlConnection.getResponseCode();
        if (responseCode > 299) {
            throw new IOException(String.format("Error getUserCount: %s %s", responseCode, readError(urlConnection)));
        }
        try {
            return readAll(urlConnection);
        } finally {
            urlConnection.disconnect();
        }

    }

    public String getOrCreateUser(String firstName, String lastName, String username, String email) throws IOException {
        return getOrCreateUser(firstName, lastName, username, email, null);
    }

    public String getOrCreateUser(String firstName, String lastName, String username, String email, HashMap<String, List<String>> attributes) throws IOException {

        final UserRepresentation userByEmail = getUserByEmail(email);
        if (userByEmail != null) return userByEmail.getId();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(email);
        userRepresentation.setUsername(username);
        userRepresentation.setFirstName(firstName);
        userRepresentation.setLastName(lastName);
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        userRepresentation.setAttributes(attributes);
        return createUser(userRepresentation);

    }
}
