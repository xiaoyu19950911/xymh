package com.example.demo.entity.project;

public class Table {

    private String TableJson;//表格

    public String getTableJson() {
        return TableJson;
    }

    public void setTableJson(String tableJson) {
        TableJson = tableJson;
    }

    @Override
    public String toString() {
        return "Table{" +
                "TableJson='" + TableJson + '\'' +
                '}';
    }
}
