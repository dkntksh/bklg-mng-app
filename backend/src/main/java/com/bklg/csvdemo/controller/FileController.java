package com.bklg.csvdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bklg.csvdemo.dto.FileCheckReslt;
import com.bklg.csvdemo.exception.FileUploadErrorException;
import com.bklg.csvdemo.exception.SpaceNotFoundException;
import com.bklg.csvdemo.service.BacklogAPIService;
import com.bklg.csvdemo.service.FileService;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;

@RestController
@RequestMapping(path = "/api/v1/file")
/**
 * ファイルのアップロード、ダウンロードに関するコントローラ
 */
public class FileController extends ApiControllerBase {
    
    @Autowired
    private FileService fileService;
    @Autowired
    private BacklogAPIService backlogAPIService;

    /**
     * テンプレートファイルのダウンロード
     */
    @GetMapping("/template_download")
    @ResponseStatus(HttpStatus.OK)
    public void templateDownload(HttpServletRequest request, HttpServletResponse response) throws IOException{
        File template = fileService.getTemplateFIle();
        InputStream in = new FileInputStream(template);
        String contentType = request.getServletContext().getMimeType(template.getAbsolutePath());
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "attachment; filename=" + template.getName());
        response.setHeader("Content-Length", String.valueOf(template.length()));
        FileCopyUtils.copy(in, response.getOutputStream());
        response.flushBuffer();
    }

    /**
     * ファイルアップロード
     * @throws JsonProcessingException
     */
    @PostMapping("/{id}/file_upload")
    @ResponseStatus(HttpStatus.OK)
    public void fileUpload(@PathVariable Integer id, @RequestParam("file") MultipartFile multipartFile) throws FileUploadErrorException, SpaceNotFoundException{
        LOG.info("Fileupload is called");
        // ファイルが空の場合は異常終了
        if(multipartFile.isEmpty()){
            // 異常終了時の処理
            throw new FileUploadErrorException("ファイルがありません");
        }
        FileCheckReslt reslut = fileService.uploadFileCheck(multipartFile);
        if(!reslut.getErrorMessageList().isEmpty()){
            LOG.info("csv file has error");
            throw new FileUploadErrorException(reslut.getErrorMessageList().toString());
        }
        LOG.info("csv file has no error");

        // 課題の登録
        List<String> errorMesseageList = backlogAPIService.createIssue(id, reslut.getCsvDataList());
        if(!errorMesseageList.isEmpty()){
            LOG.info("issue regist has error");
            throw new FileUploadErrorException(errorMesseageList.toString());   
        }
        
    }

}
