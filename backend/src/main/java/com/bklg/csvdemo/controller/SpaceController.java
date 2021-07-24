package com.bklg.csvdemo.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import com.bklg.csvdemo.domain.Space;
import com.bklg.csvdemo.dto.SpaceCreateRequestDto;
import com.bklg.csvdemo.dto.SpaceListResponse;

import com.bklg.csvdemo.dto.SpaceUpdateRequestDto;
import com.bklg.csvdemo.exception.BacklogAPIException;
import com.bklg.csvdemo.exception.SpaceNotFoundException;
import com.bklg.csvdemo.service.BacklogAPIService;
import com.bklg.csvdemo.service.SpaceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;


@RestController
@RequestMapping(path = "/api/v1/space")
@Validated
/**
 * Space情報のコントローラクラス
 */
public class SpaceController extends ApiControllerBase {

    @Autowired
    private SpaceService spaceService;

    @Autowired
    private BacklogAPIService backlogAPIService;


    @GetMapping("/space_list")
    @ResponseStatus(HttpStatus.OK)
    public SpaceListResponse spaceList() {
        return spaceService.getAllSpaceList();
    }

    @GetMapping("/{spaceId}")
    @ResponseStatus(HttpStatus.OK)
    public Space getSpace(@PathVariable Integer spaceId) throws SpaceNotFoundException{
        return spaceService.getSpaceById(spaceId);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Space registSpace(@RequestBody @Valid SpaceCreateRequestDto dto) throws BacklogAPIException {
        Long projectId = backlogAPIService.getProjectId(dto.getDomain(), dto.getProjectKey(), dto.getApiKey());
        // プロジェクトIDが取得できない場合、エラー
        if(projectId == null){
            throw new BacklogAPIException("入力された情報が不正です。値を確認してください。");
        }
        dto.setProjectId(projectId);
        return spaceService.regist(dto);
    }

    @PutMapping("/{spaceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String updateSpace(@PathVariable Integer spaceId, @RequestBody @Valid SpaceUpdateRequestDto dto) throws BacklogAPIException, SpaceNotFoundException {
        Long projectId = backlogAPIService.getProjectId(dto.getDomain(), dto.getProjectKey(), dto.getApiKey());
        // プロジェクトIDが取得できない場合、エラー
        if(projectId == null){
            throw new BacklogAPIException("入力された情報が不正です。値を確認してください。");
        }
        dto.setProjectId(projectId);
        spaceService.update(spaceId, dto);
        return "OK";
    }

    @DeleteMapping("/{spaceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteSpace(@PathVariable Integer spaceId){
        spaceService.logicalDelete(spaceId);
        return "deleted";
    }
}