package com.bklg.csvdemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bklg.csvdemo.domain.Space;
import com.bklg.csvdemo.dto.SpaceCreateRequestDto;
import com.bklg.csvdemo.dto.SpaceListResponse;
import com.bklg.csvdemo.dto.SpaceUnlessApiKeyDto;
import com.bklg.csvdemo.dto.SpaceUpdateRequestDto;
import com.bklg.csvdemo.exception.SpaceNotFoundException;
import com.bklg.csvdemo.repository.SpaceRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpaceService {
    
    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    EncryptDecriptService encryptDecriptService;

    /**
     * Spaceの登録
     * @param space 登録対象のスペース情報
     */
    @Transactional
    public Space regist(SpaceCreateRequestDto spaceCreateRequestDto){
        Space space = new Space();
        // 値のコピー
        BeanUtils.copyProperties(spaceCreateRequestDto, space);
        // APIKeyの暗号化
        space.setApiKey(encryptDecriptService.encrypt(space.getApiKey()));
        return spaceRepository.save(space);
    }

    /**
     * 論理削除されていない全てのスペースを取得する
     * order by id asc
     * @return
     */
    public SpaceListResponse getAllSpaceList(){
        List<Space> spaceList = spaceRepository.findByIdGreterThanZero();
        List<SpaceUnlessApiKeyDto> resultList = new ArrayList<SpaceUnlessApiKeyDto>();
        for(Space space : spaceList){
            SpaceUnlessApiKeyDto dto = new SpaceUnlessApiKeyDto();
            BeanUtils.copyProperties(space, dto);
            resultList.add(dto);
        }
        return new SpaceListResponse(resultList.size(), resultList);
    }

    /**
     * IDでスペース情報を取得する
     * @throws SpaceNotFoundException
     */
    public Space getSpaceById(Integer space_id) throws SpaceNotFoundException {
        Space space = this.getSpace(space_id);
        // APIKeyの復号
        space.setApiKey(encryptDecriptService.decrypt(space.getApiKey()));
        return space;
    }

    /**
     * スペースIDをキーにスペース情報を取得して、値が指定された項目のみ更新する
     * @throws SpaceNotFoundException
     */
    @Transactional
    public void update(Integer space_id, SpaceUpdateRequestDto spaceUpdateRequestDto) throws SpaceNotFoundException {
        Space targetSpace = this.getSpace(space_id);
        if(targetSpace == null || spaceUpdateRequestDto == null) return;
        if(!spaceUpdateRequestDto.getSpaceId().isBlank()) targetSpace.setSpaceId(spaceUpdateRequestDto.getSpaceId());
        if(!spaceUpdateRequestDto.getApiKey().isBlank()) {
            // APIKeyの暗号化
            targetSpace.setApiKey(encryptDecriptService.encrypt(spaceUpdateRequestDto.getApiKey()));
        }
        if(!spaceUpdateRequestDto.getDomain().isBlank()) targetSpace.setDomain(spaceUpdateRequestDto.getDomain());
        if(!spaceUpdateRequestDto.getProjectKey().isBlank()) targetSpace.setProjectKey(spaceUpdateRequestDto.getProjectKey());
        spaceRepository.save(targetSpace);
    }

    /**
     * スペース情報の削除
     * 削除は物理削除
     * @param space_id
     */
    @Transactional
    public void delete(Integer spaceId) {
       spaceRepository.deleteById(spaceId);
    }


    /**
     * スペース情報の削除
     * 論理削除
     * @param space_id
     */
    @Transactional
    public void logicalDelete(Integer spaceId) {
        spaceRepository.logicalDelete(spaceId);
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
}
