/**
 * Copyright (C) 2017 Premium Minds.
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
package com.premiumminds.billy.portugal.persistence.entities.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.premiumminds.billy.core.persistence.entities.ebean.JPASupplierEntity;
import com.premiumminds.billy.portugal.Config;
import com.premiumminds.billy.portugal.persistence.entities.PTSupplierEntity;

@Entity
@Table(name = Config.TABLE_PREFIX + "SUPPLIER")
public class JPAPTSupplierEntity extends JPASupplierEntity implements PTSupplierEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "REFERRAL_NAME")
    protected String referralName;

    @Override
    public String getReferralName() {
        return this.referralName;
    }

    @Override
    public void setReferralName(String referralName) {
        this.referralName = referralName;
    }
}
