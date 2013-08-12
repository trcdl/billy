/**
 * Copyright (C) 2013 Premium Minds.
 * 
 * This file is part of billy core.
 * 
 * billy core is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * billy core is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with billy core. If not, see <http://www.gnu.org/licenses/>.
 */
package com.premiumminds.billy.core.services.documents.impl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.premiumminds.billy.core.persistence.entities.GenericInvoiceEntity;
import com.premiumminds.billy.core.services.Builder;
import com.premiumminds.billy.core.services.documents.DocumentIssuingHandler;
import com.premiumminds.billy.core.services.documents.DocumentIssuingService;
import com.premiumminds.billy.core.services.documents.IssuingParams;
import com.premiumminds.billy.core.services.entities.documents.GenericInvoice;
import com.premiumminds.billy.core.services.exceptions.DocumentIssuingException;

public class DocumentIssuingServiceImpl implements DocumentIssuingService {

	protected Map<Class<? extends GenericInvoiceEntity>, DocumentIssuingHandler> handlers;

	public DocumentIssuingServiceImpl() {
		this.handlers = new HashMap<Class<? extends GenericInvoiceEntity>, DocumentIssuingHandler>();
	}

	@Override
	public void addHandler(Class<? extends GenericInvoiceEntity> handledClass,
			DocumentIssuingHandler handler) {
		this.handlers.put(handledClass, handler);
	}

	@Override
	public <T extends GenericInvoice> T issue(Builder<T> documentBuilder)
			throws DocumentIssuingException {
		return this.issue(documentBuilder, new IssuingParamsImpl());
	}

	public <T extends GenericInvoice> T issue(Builder<T> documentBuilder,
			IssuingParams parameters) throws DocumentIssuingException {
		T document = documentBuilder.build();
		Type[] types = document.getClass().getGenericInterfaces();

		for (Type type : types) {
			if (this.handlers.containsKey(type)) {
				return this.handlers.get(type).issue(document, parameters);
			}
		}
		throw new RuntimeException("Cannot handle document : "
				+ document.getClass().getCanonicalName());
	}
}