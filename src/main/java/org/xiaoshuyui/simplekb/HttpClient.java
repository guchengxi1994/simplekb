package org.xiaoshuyui.simplekb;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.nio.charset.StandardCharsets;

@Slf4j
public class HttpClient {

    private static final Gson gson = new Gson();

    public static <T> T sendPostRequest(String url, Object requestBody, Class<T> responseClass) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json");

            String jsonRequest = gson.toJson(requestBody);
            log.info("Request body: {}", jsonRequest);
            post.setEntity(new StringEntity(jsonRequest, StandardCharsets.UTF_8));

            CloseableHttpResponse response = httpClient.execute(post);
            String jsonResponse = EntityUtils.toString(response.getEntity());

            log.info("Response body: {}", jsonResponse);
            return gson.fromJson(jsonResponse, responseClass);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
