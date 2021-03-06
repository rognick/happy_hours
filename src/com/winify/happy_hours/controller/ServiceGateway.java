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

package com.winify.happy_hours.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.winify.happy_hours.constants.Constants;
import com.winify.happy_hours.converter.JacksonConverter;
import com.winify.happy_hours.service.TrackerService;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;

public class ServiceGateway {
    private final TrackerService service;
    private Context context;

    public ServiceGateway(Context context) {
        this.context = context;
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        Endpoint endpoint = Endpoints.newFixedEndpoint("http://" + prefs.getString(Constants.KEY_IP, "") + ":" + prefs.getString(Constants.KEY_PORT, ""));
        RestAdapter.Builder builder = new RestAdapter.Builder();
        builder.setEndpoint(endpoint);
        builder.setConverter(new JacksonConverter());
        builder.setLogLevel(RestAdapter.LogLevel.FULL);
        service = builder.build().create(TrackerService.class);
    }

    public TrackerService getService() {
        return service;
    }
}
