package com.feign.pay.sdk.alipay.spi.context;

import com.alipay.api.AlipayConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.feign.pay.sdk.common.util.JsonUtil;
import feign.Request;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author wencheng
 * @date 2021/5/11
 */
public class SignUtil {

    public static String undecryptResponse(Request request, String body) {
        String methodKey = getHeader(request, AlipayConstants.METHOD).get();
        String rootNode = methodKey.replace('.', '_')
                + AlipayConstants.RESPONSE_SUFFIX;
        String errorRootNode = AlipayConstants.ERROR_RESPONSE;

        int indexOfRootNode = body.indexOf(rootNode);
        int indexOfErrorRoot = body.indexOf(errorRootNode);

        Map<String, Object> map = JsonUtil.toObject(body, new TypeReference<Map<String, Object>>() {});
        if (indexOfRootNode > 0) {
            return JsonUtil.toJson(map.get(rootNode));
        } else if (indexOfErrorRoot > 0) {
            return JsonUtil.toJson(map.get(errorRootNode));
        } else {
            return null;
        }
    }

    public static String decryptResponse(AlipayClient client, Request request, String body) {
        String methodKey = getHeader(request, AlipayConstants.METHOD).get();
        String rootNode = methodKey.replace('.', '_')
                + AlipayConstants.RESPONSE_SUFFIX;
        String errorRootNode = AlipayConstants.ERROR_RESPONSE;

        int indexOfRootNode = body.indexOf(rootNode);
        int indexOfErrorRoot = body.indexOf(errorRootNode);

        ResponseParseItem parseItem;
        if (indexOfRootNode > 0) {
            parseItem = parseJSONSignSourceData(body, rootNode, indexOfRootNode);
        } else if (indexOfErrorRoot > 0) {
            parseItem = parseJSONSignSourceData(body, errorRootNode, indexOfErrorRoot);
        } else {
            return null;
        }
        return client.getDecryptor().decrypt(parseItem.getEncryptContent(), client.getEncryptType(), request.charset().toString());
    }

    /**
     * @param body
     * @param rootNode
     * @param indexOfRootNode
     * @return
     */
    private static ResponseParseItem parseJSONSignSourceData(String body, String rootNode,
                                                      int indexOfRootNode) {

        int signDataStartIndex = indexOfRootNode + rootNode.length() + 2;
        int signDataEndIndex = extractJsonBase64ValueEndPosition(body, signDataStartIndex);

        String encryptContent = body.substring(signDataStartIndex + 1, signDataEndIndex - 1);

        return new ResponseParseItem(signDataStartIndex, signDataEndIndex, encryptContent);
    }

    private static int extractJsonBase64ValueEndPosition(String responseString, int beginPosition) {
        for (int index = beginPosition; index < responseString.length(); ++index) {
            //找到第2个双引号作为终点，由于中间全部是Base64编码的密文，所以不会有干扰的特殊字符
            if (responseString.charAt(index) == '"' && index != beginPosition) {
                return index + 1;
            }
        }
        //如果没有找到第2个双引号，说明验签内容片段提取失败，直接尝试选取剩余整个响应字符串进行验签
        return responseString.length();
    }

    public static Optional<String> getHeader(Request request, String key) {
        Collection<String> collection = request.headers().get(key);
        if (CollectionUtils.isEmpty(collection)) {
            return Optional.empty();
        }
        return collection.stream().findFirst();
    }


    static class ResponseParseItem implements Serializable {

        /**  */
        private static final long serialVersionUID = -27707404159419651L;

        /**
         * 加密节点开始
         */
        private int startIndex = -1;

        /**
         * 加密节点结束
         */
        private int endIndex = -1;

        /**
         * 加密内容
         */
        private String encryptContent = null;

        /**
         * @param startIndex
         * @param endIndex
         * @param encryptContent
         */
        public ResponseParseItem(int startIndex, int endIndex, String encryptContent) {
            super();
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.encryptContent = encryptContent;
        }

        /**
         * Getter method for property <tt>startIndex</tt>.
         *
         * @return property value of startIndex
         */
        public int getStartIndex() {
            return startIndex;
        }

        /**
         * Getter method for property <tt>endIndex</tt>.
         *
         * @return property value of endIndex
         */
        public int getEndIndex() {
            return endIndex;
        }

        /**
         * Getter method for property <tt>encryptContent</tt>.
         *
         * @return property value of encryptContent
         */
        public String getEncryptContent() {
            return encryptContent;
        }

    }

}
