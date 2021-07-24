package com.bklg.csvdemo.controller;

import java.util.List;

import com.bklg.csvdemo.dto.ResponseCategory;
import com.bklg.csvdemo.dto.ResponseIssueType;
import com.bklg.csvdemo.dto.ResponsePriority;
import com.bklg.csvdemo.dto.ResponseUser;
import com.bklg.csvdemo.exception.SpaceNotFoundException;
import com.bklg.csvdemo.service.BacklogAPIService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * CSV登録情報用のコントローラクラス
 */
@RestController
@RequestMapping(path = "/api/v1/csv_info")
public class CsvInfoController extends ApiControllerBase {
    
    @Autowired
    private BacklogAPIService backlogAPIService;

    /**
     * 課題の種別の取得
     * @param spaceId
     * @return
     * @throws SpaceNotFoundException
     */
    @GetMapping("/{spaceId}/issue_type")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseIssueType> getIssueTypes(@PathVariable Integer spaceId) throws SpaceNotFoundException{
        return backlogAPIService.getIssueTypes(spaceId);
    }

    /**
     * 課題のカテゴリーの取得
     * @param spaceId
     * @return
     * @throws SpaceNotFoundException
     */
    @GetMapping("/{spaceId}/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseCategory> getCategories(@PathVariable Integer spaceId) throws SpaceNotFoundException{
        return backlogAPIService.getCategories(spaceId);
    }

    /**
     * 課題の優先度の取得
     * @param spaceId
     * @return
     * @throws SpaceNotFoundException
     */
    @GetMapping("/{spaceId}/priorities")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponsePriority> getPriorities(@PathVariable Integer spaceId) throws SpaceNotFoundException{
        return backlogAPIService.getPriorities(spaceId);
    }

    /**
     * 課題の担当者の取得
     * @param spaceId
     * @return
     * @throws SpaceNotFoundException
     */
    @GetMapping("/{spaceId}/users")
    @ResponseStatus(HttpStatus.OK)
    public List<ResponseUser> getUsers(@PathVariable Integer spaceId) throws SpaceNotFoundException{
        return backlogAPIService.getUsers(spaceId);
    }
}
