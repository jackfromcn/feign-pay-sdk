package com.feign.pay.sdk.common.util.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by wencheng on 2020/10/27.
 * @author wencheng
 */
@Slf4j
public class FeignUtils {

    public static String formBody(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, ?> entry: map.entrySet()) {
                if(sb.length() > 0){
                    sb.append("&");
                }
                sb.append(URLEncoder.encode(entry.getKey(),"UTF-8"))
                        .append("=")
                        .append(entry.getValue().toString());
            }
        } catch (Exception e) {
            log.error("FeignUtils#formBody 异常, e:", e);
        }
        return sb.toString();
    }

    public static void put(Map<String, Collection<String>> queries, String key, String value) throws UnsupportedEncodingException {
        queries.put(URLEncoder.encode(key,"UTF-8"), Collections.singletonList(URLEncoder.encode(value, "UTF-8")));
    }

    public static MultiValueMap<String, String> toMultiValueMap(byte[] body) throws UnsupportedEncodingException {
        if (Objects.isNull(body)) {
            return new LinkedMultiValueMap<>(0);
        }
        String[] pairs = StringUtils.tokenizeToStringArray(new String(body, "UTF-8"), "&");
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>(pairs.length);
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            if (idx == -1) {
                result.add(URLDecoder.decode(pair, "UTF-8"), null);
            } else {
                String name = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                result.add(name, value);
            }
        }
        return result;
    }

    public static Map<String, String> convert(Map<String, Collection<String>> queries) {
        Map<String, String> map = new HashMap<>(16);
        for (Map.Entry<String, Collection<String>> entry: queries.entrySet()) {
            try {
                map.put(URLDecoder.decode(entry.getKey(), "UTF-8"), URLDecoder.decode(entry.getValue().iterator().next(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {}
        }
        return map;
    }

    public static HttpHeaders getHttpHeaders(Map<String, Collection<String>> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        for (Map.Entry<String, Collection<String>> entry : headers.entrySet()) {
            httpHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return httpHeaders;
    }

    public static Map<String, Collection<String>> getHeaders(HttpHeaders httpHeaders) {
        LinkedHashMap<String, Collection<String>> headers = new LinkedHashMap<>();

        for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
            headers.put(entry.getKey(), entry.getValue());
        }

        return headers;
    }

}
