/*
 * Copyright (c) 2022. Drapuria
 */

package net.drapuria.framework.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface JacksonConfigure {

    void configure(ObjectMapper objectMapper);

}
