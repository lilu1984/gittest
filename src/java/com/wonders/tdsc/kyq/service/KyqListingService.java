package com.wonders.tdsc.kyq.service;

import com.wonders.jdbc.BaseService;
import com.wonders.tdsc.kyq.dao.KyqListingDao;

public class KyqListingService extends BaseService {
	private KyqListingDao	listingDao;

	public void setListingDao(KyqListingDao listingDao) {
		this.listingDao = listingDao;
	}

}
