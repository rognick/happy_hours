/*
 * Copyright 2014 Winify AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.winify.happy_hours.converter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import static org.apache.http.protocol.HTTP.UTF_8;

/**
 * A {@link retrofit.converter.Converter} which uses Jackson for reading and writing entities.
 *
 * @author Kai Waldron (kaiwaldron@gmail.com)
 */
public class JacksonConverter implements Converter {
    private static final String MIME_TYPE = "application/json; charset=UTF-8";
    private final ObjectMapper objectMapper;

    public JacksonConverter() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Object fromBody(TypedInput body, final Type type) throws ConversionException {
        try {
            final JavaType javaType = objectMapper.getTypeFactory().constructType(type);
            return objectMapper.readValue(body.in(), javaType);
        } catch (final JsonParseException e) {
            throw new ConversionException(e);
        } catch (final JsonMappingException e) {
            throw new ConversionException(e);
        } catch (final IOException e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public TypedOutput toBody(Object object) {
        try {
            final String json = objectMapper.writeValueAsString(object);
            return new TypedByteArray(MIME_TYPE, json.getBytes(UTF_8));
        } catch (final JsonProcessingException e) {
            throw new AssertionError(e);
        } catch (final UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
}
