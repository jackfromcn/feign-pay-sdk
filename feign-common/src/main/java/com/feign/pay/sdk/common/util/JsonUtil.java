package com.feign.pay.sdk.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;

/**
 * ClassName: JsonUtil
 * Function:
 * date: 2015年08月19日 17:24
 *
 * @author fengyue
 * @since JDK 1.7
 */
public class JsonUtil {

    private JsonUtil() {}
    /**
     * Writes object to the writer as JSON using Jackson and adds a new-line before flushing.
     *
     * @param writer the writer to write the JSON to
     * @param object the object to write as JSON
     * @throws IOException if the object can't be serialized as JSON or written to the writer
     */
    public static void writeJson(Writer writer, Object object) throws IOException {
        MAPPER.writeValue(writer, object);
    }

    public static void writeJson(String fileName, Object object) throws IOException {
        Writer writer = new PrintWriter(fileName);
        try {
            JsonUtil.writeJson(writer, object);
        } finally {
            writer.close();
        }
    }

    /**
     * Serializes object to JSON string.
     *
     * @param object object to serialize.
     * @return json string.
     */
    public static String toJson(Object object) {
        if (object == null) {
            return "";
        }
        StringWriter writer = new StringWriter();
        try {
            writeJson(writer, object);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse  to json", e);
        }
        return writer.toString();
    }

    /**
     * Parse JSON string to object.
     *
     * @param json string containing JSON.
     * @param type type reference describing type of object to parse from json.
     * @param <T>  type of object to parse from json.
     * @return object parsed from json.
     * @throws IOException
     */
    public static <T> T toObject(String json, TypeReference<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to parse json '%s'", json), e);
        }
    }

    public static <T> T toObject(String json, JavaType type) throws IOException {
        return MAPPER.readValue(json, type);
    }

    public static <T> T toObject(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to parse json '%s'", json), e);
        }
    }

    public static <T> List<T> toList(String json, Class<T> type) {
        try {
            JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, type);

            return MAPPER.readValue(json, javaType);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to parse json '%s'", json), e);
        }
    }

    public static String readFile(String path) throws IOException {
        FileInputStream stream = new FileInputStream(new File(path));
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            return Charset.defaultCharset().decode(bb).toString();
        } finally {
            stream.close();
        }
    }

    public static String prettyJson(String jsonStr) throws IOException {
        JsonNode jsonNode = MAPPER.readValue(jsonStr, JsonNode.class);
        return jsonNode.toString();
    }

    private static final ObjectMapper MAPPER = newMapper();

    private static ObjectMapper newMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, false);
        mapper.disable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
        mapper.disable(SerializationFeature.CLOSE_CLOSEABLE);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

}
