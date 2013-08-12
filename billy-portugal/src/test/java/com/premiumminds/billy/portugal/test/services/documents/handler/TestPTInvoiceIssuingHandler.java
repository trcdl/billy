/**
 * Copyright (C) 2013 Premium Minds.
 *
 * This file is part of billy portugal (PT Pack).
 *
 * billy portugal (PT Pack) is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * billy portugal (PT Pack) is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with billy portugal (PT Pack). If not, see <http://www.gnu.org/licenses/>.
 */
package com.premiumminds.billy.portugal.test.services.documents.handler;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.premiumminds.billy.core.services.UID;
import com.premiumminds.billy.core.services.exceptions.DocumentIssuingException;
import com.premiumminds.billy.portugal.persistence.dao.DAOPTInvoice;
import com.premiumminds.billy.portugal.persistence.entities.PTInvoiceEntity;
import com.premiumminds.billy.portugal.services.documents.PTInvoiceIssuingHandler;
import com.premiumminds.billy.portugal.services.documents.exceptions.InvalidInvoiceDateException;
import com.premiumminds.billy.portugal.services.documents.exceptions.InvalidInvoiceTypeException;
import com.premiumminds.billy.portugal.services.entities.PTGenericInvoice.SourceBilling;
import com.premiumminds.billy.portugal.services.entities.PTGenericInvoice.TYPE;
import com.premiumminds.billy.portugal.services.entities.PTInvoice;
import com.premiumminds.billy.portugal.test.services.documents.PTDocumentAbstractTest;

public class TestPTInvoiceIssuingHandler extends PTDocumentAbstractTest {

	private static final TYPE DEFAULT_TYPE = TYPE.FT;
	private static final SourceBilling SOURCE_BILLING = SourceBilling.P;

	private PTInvoiceIssuingHandler handler;

	@Before
	public void setUpNewInvoice() {
		this.handler = this.getInstance(PTInvoiceIssuingHandler.class);

		try {
			PTInvoiceEntity invoice = this.newInvoice(
					TestPTInvoiceIssuingHandler.DEFAULT_TYPE,
					PTDocumentAbstractTest.INVOICE_UID,
					PTDocumentAbstractTest.PRODUCT_UID,
					PTDocumentAbstractTest.BUSINESS_UID,
					PTDocumentAbstractTest.CUSTOMER_UID,
					TestPTInvoiceIssuingHandler.SOURCE_BILLING);

			this.issueNewInvoice(this.handler, invoice,
					PTDocumentAbstractTest.DEFAULT_SERIES);
		} catch (DocumentIssuingException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testIssuedInvoiceSimple() throws DocumentIssuingException {
		PTInvoice issuedInvoice = (PTInvoice) this.getInstance(
				DAOPTInvoice.class).get(
				new UID(PTDocumentAbstractTest.INVOICE_UID));

		Assert.assertEquals(PTDocumentAbstractTest.DEFAULT_SERIES,
				issuedInvoice.getSeries());
		Assert.assertTrue(1 == issuedInvoice.getSeriesNumber());
		String formatedNumber = TestPTInvoiceIssuingHandler.DEFAULT_TYPE + " "
				+ PTDocumentAbstractTest.DEFAULT_SERIES + "/1";
		Assert.assertEquals(formatedNumber, issuedInvoice.getNumber());
		Assert.assertEquals(TestPTInvoiceIssuingHandler.SOURCE_BILLING,
				issuedInvoice.getSourceBilling());
	}

	@Test
	public void testIssuedInvoiceSameSeries() throws DocumentIssuingException {
		String UID1 = "invoice_uid_1";
		String PUID1 = "product_uid_1";
		String BUID1 = "business_uid_1";
		String CUID1 = "customer_uid_1";
		Integer nextNumber = 2;
		this.issueNewInvoice(this.handler, this.newInvoice(
				TestPTInvoiceIssuingHandler.DEFAULT_TYPE, UID1, PUID1, BUID1,
				CUID1, TestPTInvoiceIssuingHandler.SOURCE_BILLING),
				PTDocumentAbstractTest.DEFAULT_SERIES);

		PTInvoice issuedInvoice = (PTInvoice) this.getInstance(
				DAOPTInvoice.class).get(new UID(UID1));

		Assert.assertEquals(PTDocumentAbstractTest.DEFAULT_SERIES,
				issuedInvoice.getSeries());
		Assert.assertEquals(nextNumber, issuedInvoice.getSeriesNumber());
		String formatedNumber = TestPTInvoiceIssuingHandler.DEFAULT_TYPE + " "
				+ PTDocumentAbstractTest.DEFAULT_SERIES + "/" + nextNumber;
		Assert.assertEquals(formatedNumber, issuedInvoice.getNumber());
	}

	@Test
	public void testIssuedInvoiceDifferentSeries()
			throws DocumentIssuingException {
		String UID1 = "invoice_uid_1";
		String PUID1 = "product_uid_1";
		Integer nextNumber = 1;
		String newSeries = "NEW_SERIES";
		String BUID1 = "business_uid_1";
		String CUID1 = "customer_uid_1";

		this.issueNewInvoice(this.handler, this.newInvoice(
				TestPTInvoiceIssuingHandler.DEFAULT_TYPE, UID1, PUID1, BUID1,
				CUID1, TestPTInvoiceIssuingHandler.SOURCE_BILLING), newSeries);

		PTInvoice issuedInvoice = (PTInvoice) this.getInstance(
				DAOPTInvoice.class).get(new UID(UID1));

		Assert.assertEquals(newSeries, issuedInvoice.getSeries());
		Assert.assertEquals(nextNumber, issuedInvoice.getSeriesNumber());
		String formatedNumber = TestPTInvoiceIssuingHandler.DEFAULT_TYPE + " "
				+ newSeries + "/" + nextNumber;
		Assert.assertEquals(formatedNumber, issuedInvoice.getNumber());
	}

	/**
	 * Test issue of invoice of different type in same series
	 * 
	 * @throws DocumentIssuingException
	 */
	@Test(expected = InvalidInvoiceTypeException.class)
	public void testIssuedInvoiceFailure() throws DocumentIssuingException {
		String series = "NEW_SERIES";
		String UID1 = "invoice_uid_1";
		String UID2 = "invoice_uid_2";
		String PUID1 = "product_uid_1";
		String PUID2 = "product_uid_2";
		String BUID1 = "business_uid_1";
		String CUID1 = "customer_uid_1";

		this.issueNewInvoice(this.handler, this.newInvoice(
				TestPTInvoiceIssuingHandler.DEFAULT_TYPE, UID1, PUID1, BUID1,
				CUID1, TestPTInvoiceIssuingHandler.SOURCE_BILLING), series);
		this.issueNewInvoice(this.handler, this
				.newInvoice(TYPE.FS, UID2, PUID2, BUID1, CUID1,
						TestPTInvoiceIssuingHandler.SOURCE_BILLING), series);
	}

	@Test(expected = InvalidInvoiceDateException.class)
	public void testIssuedInvoiceBeforeDate() throws DocumentIssuingException {
		String UID1 = "invoice_uid_1";
		String PUID1 = "product_uid_1";
		String BUID1 = "business_uid_1";
		String CUID1 = "customer_uid_1";

		this.issueNewInvoice(this.handler, this.newInvoice(
				TestPTInvoiceIssuingHandler.DEFAULT_TYPE, UID1, PUID1, BUID1,
				CUID1, TestPTInvoiceIssuingHandler.SOURCE_BILLING),
				PTDocumentAbstractTest.DEFAULT_SERIES, new Date(0));
	}

	@Test
	public void testIssuedInvoiceSameSourceBilling()
			throws DocumentIssuingException {
		String UID1 = "invoice_uid_1";
		String PUID1 = "product_uid_1";
		String BUID1 = "business_uid_1";
		String CUID1 = "customer_uid_1";

		this.issueNewInvoice(this.handler, this.newInvoice(
				TestPTInvoiceIssuingHandler.DEFAULT_TYPE, UID1, PUID1, BUID1,
				CUID1, TestPTInvoiceIssuingHandler.SOURCE_BILLING),
				PTDocumentAbstractTest.DEFAULT_SERIES);

		PTInvoice issuedInvoice = (PTInvoice) this.getInstance(
				DAOPTInvoice.class).get(new UID(UID1));

		Assert.assertEquals(TestPTInvoiceIssuingHandler.SOURCE_BILLING,
				issuedInvoice.getSourceBilling());
	}

}