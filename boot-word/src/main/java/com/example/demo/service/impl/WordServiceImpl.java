package com.example.demo.service.impl;

import com.example.demo.entity.db.Document;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.service.WordService;
import com.example.demo.sync.WordAsync;
import com.example.demo.utils.WordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class WordServiceImpl implements WordService {

    private static final String filePath="E:\\我的坚果云";

    @Autowired
    WordAsync wordAsync;

    @Autowired
    DocumentRepository documentRepository;

    @Override
    public void handler(List<String> documentIdList) throws Exception {

        for (String documentId : documentIdList) {
            wordAsync.handleWord(documentId);
        }
    }

    @Override
    public void importWords(List<MultipartFile> fileList) throws IOException {
        for (MultipartFile file : fileList) {
            Date now=new Date();
            String fileName=file.getOriginalFilename();
            String path=filePath+fileName;
            String[] subjectName=Objects.requireNonNull(fileName).split("-");
            file.transferTo(new File(path));
            Document document=new Document();
            document.setUpdateTime(now);
            document.setCreateTime(now);
            document.setPath(path);
            document.setStatus("ready");
            document.setName(file.getOriginalFilename());
            document.setType(file.getContentType());
            document.setSubjectId(WordUtil.subjectMap.get(subjectName[1]));
            documentRepository.save(document);
        }
        sendMQ(fileList);
    }

    private void sendMQ(List<MultipartFile> fileList) {
        //1.调用c#对word进行预处理
        //2.修改status状态为c#-success或者c#-failed
    }
}
