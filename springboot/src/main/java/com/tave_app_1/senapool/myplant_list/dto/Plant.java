package com.tave_app_1.senapool.myplant_list.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@AllArgsConstructor
public class Plant {
    private Integer plantPK;
    private String plantName;
    private String plantImage;
}
