/*
 * Copyright (c) 2022. Drapuria
 */

package net.drapuria.framework.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import lombok.Getter;
import net.drapuria.framework.jackson.JacksonConfigure;
import net.drapuria.framework.beans.annotation.PreInitialize;
import net.drapuria.framework.beans.annotation.Service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service(name = "jackson")
public class JacksonService {

    public static JacksonService INSTANCE;

    @Getter
    private ObjectMapper mainMapper;
    private Map<String, ObjectMapper> registeredMappers;
    private List<JacksonConfigure> jacksonConfigures;

    @PreInitialize
    public void preInit() {
        INSTANCE = this;

        this.registeredMappers = new ConcurrentHashMap<>();
        this.jacksonConfigures = new ArrayList<>();
        this.mainMapper = this.getOrCreateJacksonMapper("main");
    }

    @SafeVarargs
    public final ObjectMapper getOrCreateJacksonMapper(String name, Consumer<ObjectMapper>... onFirstInitial) {
        if (this.registeredMappers.containsKey(name)) {
            return this.registeredMappers.get(name);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        this.configureJacksonMapper(name, objectMapper);
        for (Consumer<ObjectMapper> consumer : onFirstInitial) {
            consumer.accept(objectMapper);
        }

        return objectMapper;
    }

    public void configureJacksonMapper(String name, ObjectMapper objectMapper) {
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.addHandler(new DeserializationProblemHandler() {
            @Override
            public JavaType handleUnknownTypeId(DeserializationContext ctxt, JavaType baseType, String subTypeId, TypeIdResolver idResolver, String failureMsg) throws IOException {
                return null;
            }
        });
        objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);

        this.registeredMappers.put(name, objectMapper);
        for (JacksonConfigure configure : this.jacksonConfigures) {
            configure.configure(objectMapper);
        }
    }

    public void registerJacksonConfigure(JacksonConfigure jacksonConfigure) {
        this.jacksonConfigures.add(jacksonConfigure);
        for (ObjectMapper mapper : this.registeredMappers.values()) {
            jacksonConfigure.configure(mapper);
        }
    }

}
