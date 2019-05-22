package com.example.demo;

import com.example.demo.entity.db.Document;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.utils.MyApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    DocumentRepository documentRepository;

    @Test
    public void contextLoads() {
        Document document=new Document();
        document.setCreateTime(new Date());
        document.setUpdateTime(new Date());
        documentRepository.save(document);
    }

}
