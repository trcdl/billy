/**
 * Copyright (C) 2013 Premium Minds.
 *
 * This file is part of billy platypus (PT Pack).
 *
 * billy platypus (PT Pack) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * billy platypus (PT Pack) is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with billy platypus (PT Pack). If not, see <http://www.gnu.org/licenses/>.
 */
package com.premiumminds.billy.portugal.services.builders.impl;

import com.premiumminds.billy.core.exceptions.BillyValidationException;
import com.premiumminds.billy.core.util.BillyValidator;
import com.premiumminds.billy.core.util.Localizer;
import com.premiumminds.billy.portugal.persistence.dao.DAOPTBusiness;
import com.premiumminds.billy.portugal.persistence.dao.DAOPTCustomer;
import com.premiumminds.billy.portugal.persistence.dao.DAOPTManualInvoice;
import com.premiumminds.billy.portugal.persistence.dao.DAOPTSupplier;
import com.premiumminds.billy.portugal.persistence.entities.PTManualInvoiceEntity;
import com.premiumminds.billy.portugal.services.builders.PTManualInvoiceBuilder;
import com.premiumminds.billy.portugal.services.entities.PTInvoiceEntry;
import com.premiumminds.billy.portugal.services.entities.PTManualInvoice;

public class PTManualInvoiceBuilderImpl<TBuilder extends PTManualInvoiceBuilderImpl<TBuilder, TEntry, TDocument>, TEntry extends PTInvoiceEntry, TDocument extends PTManualInvoice>
		extends PTGenericInvoiceBuilderImpl<TBuilder, TEntry, TDocument>
		implements PTManualInvoiceBuilder<TBuilder, TEntry, TDocument> {

	protected static final Localizer LOCALIZER = new Localizer(
			"com/premiumminds/billy/portugal/i18n/FieldNames");

	public PTManualInvoiceBuilderImpl(DAOPTManualInvoice daoPTManualInvoice,
			DAOPTBusiness daoPTBusiness, DAOPTCustomer daoPTCustomer,
			DAOPTSupplier daoPTSupplier) {
		super(daoPTManualInvoice, daoPTBusiness, daoPTCustomer, daoPTSupplier);
	}

	@Override
	protected PTManualInvoiceEntity getTypeInstance() {
		return (PTManualInvoiceEntity) super.getTypeInstance();
	}

	@Override
	protected void validateInstance() throws BillyValidationException {
		super.validateInstance();

		BillyValidator.mandatory(getTypeInstance().getManualInvoiceNumber(),
				PTInvoiceBuilderImpl.LOCALIZER.getString("field.man_number"));
		BillyValidator.mandatory(getTypeInstance().getManualInvoiceSeries(),
				PTInvoiceBuilderImpl.LOCALIZER.getString("field.man_series"));
	}

	@Override
	public TBuilder setManualInvoiceNumber(String number) {
		BillyValidator.mandatory(number,
				PTInvoiceBuilderImpl.LOCALIZER.getString("field.man_number"));
		this.getTypeInstance().setManualInvoiceNumber(number);
		return this.getBuilder();
	}

	@Override
	public TBuilder setManualInvoiceSeries(String series) {
		BillyValidator.mandatory(series,
				PTInvoiceBuilderImpl.LOCALIZER.getString("field.man_series"));
		this.getTypeInstance().setManualInvoiceSeries(series);
		return this.getBuilder();
	}
}