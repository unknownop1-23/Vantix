package com.vtx.vantix.repo;

import com.vtx.vantix.repo.data.OtherData;

public class OtherDataAPI {

    public static long getPriceFetchInterval() {
        OtherData data = RepoHandler.get(VNTXRepo.KEY_OTHER, OtherData.class, new OtherData());
        return data.priceFetchInterval;
    }

    public static long getPriceUploadInterval() {
        OtherData data = RepoHandler.get(VNTXRepo.KEY_OTHER, OtherData.class, new OtherData());
        return data.priceUploadInterval;
    }

}
