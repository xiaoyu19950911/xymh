package com.example.demo.web.request;

import java.util.List;

/**
 * @program: xymh
 * @description:
 * @author: xyu
 * @create: 2019-05-23 14:37
 */
public class HandlerRequest {

    private List<String> documentIdList;

    public List<String> getDocumentIdList() {
        return documentIdList;
    }

    public void setDocumentIdList(List<String> documentIdList) {
        this.documentIdList = documentIdList;
    }

    @Override
    public String toString() {
        return "HandlerRequest{" +
                "documentIdList=" + documentIdList +
                '}';
    }
}
