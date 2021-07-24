package com.bklg.csvdemo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * アップロードファイルのチェック結果
 */
@Data
@AllArgsConstructor
public class FileCheckReslt {
   private List<String> errorMessageList;

   private List<BacklogIssueDto> csvDataList;
}
