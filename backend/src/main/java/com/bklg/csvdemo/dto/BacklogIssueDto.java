package com.bklg.csvdemo.dto;

import java.text.SimpleDateFormat;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;

import org.apache.commons.lang3.StringUtils;

/**
 * Backlog 課題登録時のパラメータのDTO
 */
@CsvEntity(header = true)
public class BacklogIssueDto {
    /**
     * 課題の件名
     */
    @NotBlank(message = "summaryは必須です")
    @CsvColumn(position = 0, name = "summary(必須)")
    public String summary;

    /**
     * 課題の詳細
     */
    @CsvColumn(position = 1, name = "description")
    public String description;

    /**
     * 課題の開始日 (yyyy-MM-dd)
     */
    @CsvColumn(position = 2, name = "startDate")
    public String startDate;

    /**
     * 課題の開始日 (yyyy-MM-dd)のフォーマットチェック
     * @return true:エラーあり false:エラーなし
     */
    @AssertTrue(message = "startDateはyyyy-MM-ddのフォーマットで設定してください。")
    public boolean isStartDateyyyyMMdd() {
        if (StringUtils.isEmpty(this.startDate)) {
            return true;
        }
        return this.isYyyyMmDd(this.startDate);
    }

    /**
     * 課題の期限日 (yyyy-MM-dd)
     */
    @CsvColumn(position = 3, name = "dueDate")
    public String dueDate;

    /**
     * 課題の期限日 (yyyy-MM-dd)のフォーマットチェック
     * @return true:エラーあり false:エラーなし
     */
    @AssertTrue(message = "dueDateはyyyy-MM-ddのフォーマットで設定してください。")
    public boolean isDueDateyyyyMMdd() {
        if (StringUtils.isEmpty(this.dueDate)) {
            return true;
        }
        return this.isYyyyMmDd(this.dueDate);
    }
    
    /**
     * 課題の予定時間
     */
    @CsvColumn(position = 4, name = "estimatedHours")
    public String estimatedHours;

    /**
     * 課題の予定時間の数値チェック
     * @return true:エラーあり false:エラーなし
     */
    @AssertTrue(message = "estimatedHoursは数値で設定してください。")
    public boolean isEstimatedHoursFloat() {
        if (StringUtils.isEmpty(this.estimatedHours)) {
            return true;
        }
        try {
            Float.parseFloat(this.estimatedHours);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * 課題の種別のID
     */
    @CsvColumn(position = 5, name = "issueTypeId(必須)")
    @NotBlank(message = "issueTypeIdは必須です")
    @Pattern(regexp="[0-9]{1,}", message = "issueTypeIdには数値を設定してください。")// 数値チェック
    public String issueTypeId;

    /**
     * 	課題の優先度のID
     */
    @CsvColumn(position = 6, name = "priorityId(必須)")
    @NotBlank(message = "priorityIdは必須です")
    @Pattern(regexp="[0-9]{1,}", message = "priorityIdには数値を設定してください。")// 数値チェック
    public String priorityId;

    /**
     * 課題の担当者のID
     */
    @CsvColumn(position = 7, name = "assigneeId")
    public String assigneeId;
    
    /**
     * 課題の予定時間の数値チェック
     * @return true:エラーあり false:エラーなし
     */
    @AssertTrue(message = "assigneeIdは数値で設定してください。")
    public boolean isAssigneeIdLong() {
        if (StringUtils.isEmpty(this.assigneeId)) {
            return true;
        }
        try {
            Long.parseLong(this.assigneeId);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * 引数の書式がyyyy-MM-ddかのチェックを行う
     * @param target チェック対象の文字列
     * @return true:yyyy-MM-ddである false:yyyy-MM-ddではない
     */
    private boolean isYyyyMmDd(String target){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            format.parse(target);
            return true;
       }
       catch(Exception e){
            return false;
       }
    }
}
