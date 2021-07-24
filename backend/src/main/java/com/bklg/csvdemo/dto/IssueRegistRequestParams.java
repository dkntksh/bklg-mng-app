package com.bklg.csvdemo.dto;

import lombok.Data;

@Data
public class IssueRegistRequestParams {
    /**
     * 課題を登録するプロジェクトのID
     */
    private String projectId;

    /**
     * 課題の件名
     */
    private String summary;

    /**
     * 課題の親課題のID
     */
    // @CsvColumn(position = 2, name = "parentIssueId")
    // public Number parentIssueId;

    /**
     * 課題の詳細
     */
    private String description;

    /**
     * 課題の開始日 (yyyy-MM-dd)
     */
    private String startDate;

    /**
     * 課題の期限日 (yyyy-MM-dd)
     */
    private String dueDate;
    
    /**
     * 課題の予定時間
     */
    private String estimatedHours;

    /**
     * 課題の種別のID
     */
    private String issueTypeId;

    /**
     * 課題のカテゴリーのID
     */
    // @CsvColumn(position = 8, name = "categoryId")
    // public List<Number> categoryId;

    /**
     * 	課題の発生バージョンのID
     */
    // @CsvColumn(position = 9, name = "versionId")
    // public List<Long> versionId;

    /**
     * 課題のマイルストーンのID
     */
    // @CsvColumn(position = 10, name = "milestoneId")
    // public List<Number> milestoneId;

    /**
     * 	課題の優先度のID
     */
    private String priorityId;

    /**
     * 課題の担当者のID
     */
    private String assigneeId;

    // /**
    //  * 課題の登録の通知を受け取るユーザーのID
    //  */
    // @CsvColumn(position = 13, name = "notifiedUserId")
    // public List<Number> notifiedUserId;

}
