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

package com.winify.happy_hours.activities.listeners;

import com.winify.happy_hours.activities.models.User;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;


public interface ServiceListener {

    void onSuccess(Response response);

    void onServerFail(RetrofitError error);

    void onUsersList(List<User> list);


}
