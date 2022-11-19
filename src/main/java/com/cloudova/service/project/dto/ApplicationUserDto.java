package com.cloudova.service.project.dto;

import com.cloudova.service.project.models.MetaData;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ApplicationUserDto implements Serializable {
    private final String username;
    private final String name;
    private final String password;
    private final List<MetaDataDto> metadata;

    @Data
    public static class MetaDataDto implements Serializable {
        private final String key;
        private final String value;

        public MetaData toMetaData(){
            return MetaData.builder().key(this.key).value(this.value).build();
        }

    }
}
