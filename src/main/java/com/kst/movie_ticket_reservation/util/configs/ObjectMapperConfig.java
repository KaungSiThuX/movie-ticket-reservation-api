package com.kst.movie_ticket_reservation.util.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.cfg.CoercionAction;
import tools.jackson.databind.cfg.CoercionInputShape;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.type.LogicalType;


@Configuration
public class ObjectMapperConfig
{
    @Bean
    @Primary
    public JsonMapper objectMapper()
    {

        return JsonMapper.builder()

                .withCoercionConfig(LogicalType.Textual, mutableCoercionConfig ->
                        mutableCoercionConfig.setCoercion(CoercionInputShape.Boolean, CoercionAction.Fail)
                                .setCoercion(CoercionInputShape.Integer, CoercionAction.Fail))

                .withCoercionConfig(LogicalType.Boolean, mutableCoercionConfig ->
                        mutableCoercionConfig.setCoercion(CoercionInputShape.String, CoercionAction.Fail)
                                .setCoercion(CoercionInputShape.Integer, CoercionAction.Fail))

                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                
                .build();

    }
}
