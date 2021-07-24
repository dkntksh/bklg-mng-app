package com.bklg.csvdemo.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.bklg.csvdemo.dto.BacklogIssueDto;
import com.bklg.csvdemo.dto.FileCheckReslt;
import com.bklg.csvdemo.exception.FileUploadErrorException;
import com.orangesignal.csv.CsvConfig;
import com.orangesignal.csv.manager.CsvEntityManager;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
    
    /**
     * テンプレートファイルの取得
     * @return
     * @throws IOException
     */
    public File getTemplateFIle() throws IOException{
        ClassPathResource classPathResource = new ClassPathResource("/csv/Backlog-template.csv");
        return classPathResource.getFile();
    }


    /**
     * アップロードするファイルのチェック
     * @param multipartFile
     * @return エラーメッセージのリスト
     * @throws FileUploadErrorException
     */
    public FileCheckReslt uploadFileCheck(MultipartFile multipartFile) throws FileUploadErrorException {
      InputStream stream;
      try {
        stream = multipartFile.getInputStream();
      } catch (IOException e1) {
        e1.printStackTrace();
        throw new FileUploadErrorException("ファイルが読み込めません");
      }           
      CsvConfig config = new CsvConfig();
      config.setVariableColumns(false);
      // ヘッダ＋先頭2行を読み飛ばす
      config.setSkipLines(2);
      // 区切り文字
      config.setBreakString(",");
      // 空行を無視する
      config.setIgnoreEmptyLines(true);
      List<String> validateMessageList = new ArrayList<String>();
      List<BacklogIssueDto> dtoList = null;
      try {
        dtoList = new CsvEntityManager().config(config).load(BacklogIssueDto.class).from(stream);
        if(dtoList.size() > 10){
          throw new FileUploadErrorException("データ部が11行以上あります。データ行を減らしてください。");
        }
        // Validator を構成します。
        final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        final Validator validator = validatorFactory.getValidator();
        // 3行目までスキップ済
        int csvLine = 3;
        for (final BacklogIssueDto dto : dtoList) {
          csvLine ++;
          Set<ConstraintViolation<BacklogIssueDto>> constraintViolations = validator.validate(dto);
          if(!constraintViolations.isEmpty()){
            addErrorMassages(csvLine, validateMessageList, constraintViolations);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
        throw new FileUploadErrorException("ファイルが読み込めません");
      }
      
      return new FileCheckReslt(validateMessageList, dtoList);
    }

    /**
     * エラーメッセージの追加
     * @param csvLine
     * @param validateMessageList
     * @param constraintViolations
     */
    private void addErrorMassages(int csvLine, List<String> validateMessageList,
        Set<ConstraintViolation<BacklogIssueDto>> constraintViolations) {
          String lineMessage = String.valueOf(csvLine) + "行目:";
          for (ConstraintViolation<BacklogIssueDto> violation : constraintViolations) {
            validateMessageList.add(lineMessage + violation.getMessage());
          }
    }
}
