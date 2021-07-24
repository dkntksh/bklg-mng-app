package com.bklg.csvdemo.service;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bklg.csvdemo.domain.Space;
import com.bklg.csvdemo.dto.BacklogIssueDto;
import com.bklg.csvdemo.dto.ProjectResponse;
import com.bklg.csvdemo.dto.ResponseCategory;
import com.bklg.csvdemo.dto.ResponseIssueType;
import com.bklg.csvdemo.dto.ResponsePriority;
import com.bklg.csvdemo.dto.ResponseUser;
import com.bklg.csvdemo.exception.SpaceNotFoundException;
import com.bklg.csvdemo.repository.SpaceRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class BacklogAPIService {

    static final Logger LOG = LoggerFactory.getLogger(BacklogAPIService.class);

    final String HTTPS = "https://";

    final String COMMON_API_V2 = "/api/v2";

    final String GET_PROJECTS_URL = "/projects/";

    final String GET_ISSUE_TYPES_URL = "/issueTypes";

    final String APIKEY_PARAM = "?apiKey=";

    final String GET_CATEGORY_URL = "/categories";

    final String GET_PRIORITY_URL = "/priorities";

    final String GET_USER_URL = "/users";

    final String REGIST_ISSUE_URL = "/api/v2/issues";

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EncryptDecriptService encryptDecriptService;
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * プロジェクトIDのセット
     * @param spaceCreateRequestDto
     * @return projectId（nullの可能性あり）
     */
    
    public Long getProjectId(String domain, String projectKey, String apiKey){
        final String url = this.getProjectUrl(domain, projectKey, apiKey);
        try{
            ProjectResponse project = restTemplate.getForObject(url, ProjectResponse.class);
            if(project == null){
                return null;
            }
            return project.getId();
        } catch (HttpClientErrorException e) {
            // 400系エラー
            LOG.warn(e.getMessage());
        } catch (HttpServerErrorException e) {
            // 500系エラー
            LOG.warn(e.getMessage());
        } catch (Exception e){
            // その他エラー
            LOG.error(e.getMessage());
        }
        return null;
    }

    /**
     * 課題の登録
     * @param id spaceのID
     * @param csvDataList 登録対象のCSVデータ
     * @return エラーメッセージ
     * @throws SpaceNotFoundException
     * @throws MalformedURLException
     */
    public List<String> createIssue(Integer id, List<BacklogIssueDto> csvDataList) throws SpaceNotFoundException {
        Space space = this.getSpace(id);
        final String url = this.getCreateIssueUrl(space);
        List<String> errorMesseageList = new ArrayList<String>();
        int rowCount = 4;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        for(BacklogIssueDto csvData: csvDataList){
            MultiValueMap<String,String> paramMap = this.bulidParamMap(csvData, space.getProjectId());
            // 登録をリクエストする
            try{
                HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(paramMap, headers);

                ResponseEntity<String> response = restTemplate.exchange(url,
                          HttpMethod.POST,
                          entity,
                          String.class);

                LOG.info("createIssue:result:" + response.getBody());
            } catch (HttpClientErrorException | HttpServerErrorException  e) {// 通信系エラー時、エラーメッセージの追加
                // 400系,500系エラー
                LOG.warn(e.getMessage());
                LOG.warn(e.getResponseBodyAsString());
                errorMesseageList.add(rowCount + "行目:" + e.getResponseBodyAsString());
            } catch (Exception e){
                // その他エラー
                LOG.error(e.getMessage());
                errorMesseageList.add(rowCount + "行目:" +e.getMessage());
            }
            rowCount++;
            // 連続してAPIを実行しないように1秒sleepする
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // この部分のエラーは握りつぶす
                e.printStackTrace();
            }
        }
        return errorMesseageList;
    }

    /**
     * スペース情報の取得
     * @param id
     * @return
     * @throws SpaceNotFoundException
     */
    private Space getSpace(Integer id) throws SpaceNotFoundException {
        Optional<Space> spaceOp = spaceRepository.findById(id);
        if(!spaceOp.isPresent()){
            throw new SpaceNotFoundException();
        }
        return spaceOp.get();
    }

    /**
     * 種別一覧の取得
     * @param id spaceテーブルのID
     * @return　種別一覧
     * @throws SpaceNotFoundException
     */
    public List<ResponseIssueType> getIssueTypes(Integer id) throws SpaceNotFoundException {
        Space space = this.getSpace(id);
        List<ResponseIssueType> issueTypeList = null;
        final String url = this.buildGetIssueTypesUrl(space);
        try{
            ResponseEntity<ResponseIssueType[]> responseEntity = restTemplate.getForEntity(url.toString(), ResponseIssueType[].class);
            ResponseIssueType[] issueTypeArray = responseEntity.getBody();
            return Arrays.stream(issueTypeArray).collect(Collectors.toList());
        } catch (HttpClientErrorException e) {
            // 400系エラー
            LOG.warn(e.getMessage());
        } catch (HttpServerErrorException e) {
            // 500系エラー
            LOG.warn(e.getMessage());
         } catch (Exception e){
            // その他エラー
            LOG.error(e.getMessage());
        }
        return issueTypeList;
    }

    /**
     * カテゴリ一覧の取得
     * @param id spaceテーブルのID
     * @return カテゴリ一覧
     * @throws SpaceNotFoundException
     */
    public List<ResponseCategory> getCategories(Integer id) throws SpaceNotFoundException {
        Space space = this.getSpace(id);
        List<ResponseCategory> categoryList = null;
        final String url = this.bulidGetCategoriesUrl(space);
        try{
            ResponseEntity<ResponseCategory[]> responseEntity = restTemplate.getForEntity(url, ResponseCategory[].class);
            ResponseCategory[] categoryArray = responseEntity.getBody();
            return Arrays.stream(categoryArray).collect(Collectors.toList());
        } catch (HttpClientErrorException e) {
            // 400系エラー
            LOG.warn(e.getMessage());
        } catch (HttpServerErrorException e) {
            // 500系エラー
            LOG.warn(e.getMessage());
         } catch (Exception e){
            // その他エラー
            LOG.error(e.getMessage());
        }
        return categoryList;
    }

    /**
     * 優先度取得
     * @param id
     * @return 優先度一覧
     * @throws SpaceNotFoundException
     */
    public List<ResponsePriority> getPriorities(Integer id) throws SpaceNotFoundException {
        Space space = this.getSpace(id);
        List<ResponsePriority> priorityList = null;
        final String url = this.bulidGetPrioritiesUrl(space);
        try{
            ResponseEntity<ResponsePriority[]> responseEntity = restTemplate.getForEntity(url, ResponsePriority[].class);
            ResponsePriority[] priorytyArray = responseEntity.getBody();
            return Arrays.stream(priorytyArray).collect(Collectors.toList());
        } catch (HttpClientErrorException e) {
            // 400系エラー
            LOG.warn(e.getMessage());
        } catch (HttpServerErrorException e) {
            // 500系エラー
            LOG.warn(e.getMessage());
         } catch (Exception e){
            // その他エラー
            LOG.error(e.getMessage());
        }
        return priorityList;
    }

    /**
     * 担当者の取得
     * @param spaceId
     * @return 担当者一覧
     * @throws SpaceNotFoundException
     */
    public List<ResponseUser> getUsers(Integer id) throws SpaceNotFoundException {
        Space space = this.getSpace(id);
        List<ResponseUser> priorityList = null;
        final String url = this.bulidGetUserUrl(space);
        try{
            ResponseEntity<ResponseUser[]> responseEntity = restTemplate.getForEntity(url, ResponseUser[].class);
            ResponseUser[] userArray = responseEntity.getBody();
            return Arrays.stream(userArray).collect(Collectors.toList());
        } catch (HttpClientErrorException e) {
            // 400系エラー
            LOG.warn(e.getMessage());
        } catch (HttpServerErrorException e) {
            // 500系エラー
            LOG.warn(e.getMessage());
         } catch (Exception e){
            // その他エラー
            LOG.error(e.getMessage());
        }
        return priorityList;
    }

    /**
     * パラメータ用mapの作成
     * @param csvData
     * @return
     */
    private MultiValueMap<String, String> bulidParamMap(BacklogIssueDto csvData, Long projectId) {
        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("projectId", String.valueOf(projectId));
        if(!csvData.assigneeId.isBlank())map.add("assigneeId", csvData.assigneeId);
        if(!csvData.description.isBlank())map.add("description", csvData.description);
        if(!csvData.dueDate.isBlank())map.add("dueDate", csvData.dueDate);
        if(!csvData.estimatedHours.isBlank())map.add("estimatedHours", csvData.estimatedHours);
        if(!csvData.issueTypeId.isBlank())map.add("issueTypeId", csvData.issueTypeId);
        if(!csvData.priorityId.isBlank())map.add("priorityId", csvData.priorityId);
        if(!csvData.startDate.isBlank())map.add("startDate", csvData.startDate);
        if(!csvData.summary.isBlank())map.add("summary", csvData.summary);
        return map;
    }

    /**
     * スペース作成リクエストDTOからプロジェクト取得URLを作成
     * @param spaceCreateRequestDto
     * @return
     */
    private String getProjectUrl(String domain, String projectKey, String apiKey) {
        StringBuilder urlSb = new StringBuilder(HTTPS);
        urlSb.append(domain);
        urlSb.append(COMMON_API_V2);
        urlSb.append(GET_PROJECTS_URL);
        urlSb.append(projectKey);
        urlSb.append(APIKEY_PARAM);
        urlSb.append(apiKey);
        return urlSb.toString();
    }

    /**
     * スペース情報から
     */
    private String getCreateIssueUrl(Space space) {
        StringBuilder urlSb = new StringBuilder(HTTPS);
        urlSb.append(space.getDomain());
        urlSb.append(REGIST_ISSUE_URL);
        urlSb.append(APIKEY_PARAM);
        urlSb.append(space.getDecryptAPIKey(encryptDecriptService));
        LOG.debug("createIssueURL" , urlSb.toString());
        return urlSb.toString();
    }

    /**
     * スペース情報から課題のタイプ取得のURLを作成
     * @param space
     * @return
     */
    private String buildGetIssueTypesUrl(Space space) {
        StringBuilder urlSb = new StringBuilder(HTTPS);
        urlSb.append(space.getDomain());
        urlSb.append(COMMON_API_V2);
        urlSb.append(GET_PROJECTS_URL);
        urlSb.append(space.getProjectKey());
        urlSb.append(GET_ISSUE_TYPES_URL);
        urlSb.append(APIKEY_PARAM);
        urlSb.append(space.getDecryptAPIKey(encryptDecriptService));
        LOG.debug("getIssueTypesUrl:", urlSb.toString());
        return urlSb.toString();
    }

    /**
     * スペース情報から課題カテゴリ取得のURLを作成
     * @param space
     * @return
     */
    private String bulidGetCategoriesUrl(Space space) {
        StringBuilder urlSb = new StringBuilder(HTTPS);
        urlSb.append(space.getDomain());
        urlSb.append(COMMON_API_V2);
        urlSb.append(GET_PROJECTS_URL);
        urlSb.append(space.getProjectKey());
        urlSb.append(GET_CATEGORY_URL);
        urlSb.append(APIKEY_PARAM);
        urlSb.append(space.getDecryptAPIKey(encryptDecriptService));
        LOG.debug("getCategoriesUrl:", urlSb.toString());
        return urlSb.toString();
    }
    
    /**
     * スペース情報から優先度取得のURLを作成する
     * @param space
     * @return
     */
    private String bulidGetPrioritiesUrl(Space space) {
        StringBuilder urlSb = new StringBuilder(HTTPS);
        urlSb.append(space.getDomain());
        urlSb.append(COMMON_API_V2);
        urlSb.append(GET_PRIORITY_URL);
        urlSb.append(APIKEY_PARAM);
        urlSb.append(space.getDecryptAPIKey(encryptDecriptService));
        LOG.debug("getetPrioritiesUrl:", urlSb.toString());
        return urlSb.toString();
    }

    /**
     * スペース情報から担当者取得のURLを作成する
     * @param space
     * @return
     */
    private String bulidGetUserUrl(Space space) {
        StringBuilder urlSb = new StringBuilder(HTTPS);
        urlSb.append(space.getDomain());
        urlSb.append(COMMON_API_V2);
        urlSb.append(GET_USER_URL);
        urlSb.append(APIKEY_PARAM);
        urlSb.append(space.getDecryptAPIKey(encryptDecriptService));
        LOG.debug("getetUsersUrl:", urlSb.toString());
        return urlSb.toString();
    }



}
