/*
 * Copyright 2017 Apereo
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

package com.tle.web.api.interfaces;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.tle.web.api.interfaces.beans.BaseEntityBean;
import com.tle.web.api.interfaces.beans.SearchBean;
import com.tle.web.api.interfaces.beans.security.BaseEntitySecurityBean;

public interface BaseEntityResource<BEB extends BaseEntityBean, SB extends BaseEntitySecurityBean>
{
	@GET
	SearchBean<BEB> list(UriInfo uriInfo);

	@GET
	SB getAcls(UriInfo uriInfo);

	@PUT
	Response editAcls(UriInfo uriInfo, SB security);

	@GET
	BEB get(UriInfo uriInfo, String uuid);

	@DELETE
	Response delete(UriInfo uriInfo, String uuid);

	@POST
	Response create(UriInfo uriInfo, BEB bean, String stagingUuid);

	@PUT
	Response edit(UriInfo uriInfo, String uuid, BEB bean, String stagingUuid, String lockId, boolean keepLocked);

	@GET
	Response getLock(UriInfo uriInfo, String uuid);

	@POST
	Response lock(UriInfo uriInfo, String uuid);

	@DELETE
	Response unlock(UriInfo uriInfo, String uuid);
}
