package com.tave_app_1.senapool.myplant_list.service;

import com.tave_app_1.senapool.entity.MyPlant;
import com.tave_app_1.senapool.entity.User;
import com.tave_app_1.senapool.myplant_list.dto.diary_list_response.DiaryListResponseDto;
import com.tave_app_1.senapool.myplant_list.dto.plant_register_request.PlantRegisterRequestDto;
import com.tave_app_1.senapool.myplant_list.dto.plant_list_response.PlantListResponseDto;
import com.tave_app_1.senapool.myplant_list.dto.plant_update_request.PlantUpdateRequestDto;
import com.tave_app_1.senapool.myplant_list.repository.MyPlantRepository;
import com.tave_app_1.senapool.user.repository.UserRepository;
import com.tave_app_1.senapool.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPlantService {

    private final MyPlantRepository myPlantRepository;
    private final UserRepository userRepository;
    private final FileUtil fileUtil;

    @Transactional(readOnly = true)
    public PlantListResponseDto makePlantList(Long userPK) {
        // userPK로 해당 user 정보 가져오기
        User user = userRepository.findByUserPK(userPK);
        // Entity -> Dto 변환
        PlantListResponseDto plantListResponseDto = new PlantListResponseDto(user);
        return plantListResponseDto;
    }

    /*
    세션정보 넘어온 뒤 user 정보 넣어줘야함
     */
    @Transactional
    public void joinPlant(PlantRegisterRequestDto plantRegisterRequestDto){
        /*
        dummy user
        */
        User user = User.builder()
                .userId("test")
                .password("1234")
                .email("test@naver.com")
                .userImageName("test")
                .build();

        /*
            추후 빌더 형태로 변환
         */
        // 식물 이미지 저장
        String uniqueImageName = fileUtil.savePlantImage(plantRegisterRequestDto.getFile());
        MyPlant myPlant = plantRegisterRequestDto.toEntity(uniqueImageName, user);
        myPlantRepository.save(myPlant);
    }

    @Transactional
    public void updatePlant(Long plantPK, PlantUpdateRequestDto plantUpdateRequestDto) {
        MyPlant myPlant = myPlantRepository.findByPlantPK(plantPK);

        // 기존 이미지 삭제 후, 새 이미지 저장
        String uniqueImageName = fileUtil.imageChange(plantUpdateRequestDto.getFile(), myPlant.getPlantImage());
        // dirty check 이용한 update
        myPlant.updatePlant(uniqueImageName, plantUpdateRequestDto.getPlantName(), plantUpdateRequestDto.getPlantType(), plantUpdateRequestDto.getWaterPeriod());
    }

    @Transactional
    public void deletePlant(Long plantPK) {
        // 식물삭제
        myPlantRepository.deleteById(plantPK);
    }

    @Transactional(readOnly = true)
    public DiaryListResponseDto makeDiaryList(Long plantPK, Boolean publish) {
        // plantPK로 해당 plant 정보 가져오기
        MyPlant myPlant = myPlantRepository.findByPlantPK(plantPK);
        // Entity -> Dto 변환
        DiaryListResponseDto diaryListResponseDto = new DiaryListResponseDto(myPlant);
        return diaryListResponseDto;
    }
}
