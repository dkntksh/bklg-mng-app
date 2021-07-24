package com.bklg.csvdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * プロジェクト情報取得時のレスポンスクラス
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private long id;
    private String projectKey; 
    private String name;
    private boolean chartEnabled;
    private boolean subtaskingEnabled; 
    private boolean projectLeaderCanEditProjectLeader; 
    private String textFormattingRule; 
    private boolean archived;
}
