package org.kasarab.cs_demo.service.utils;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperUtils {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
