package com.vtx.vantix.features.storage.data;

import com.vtx.vantix.features.storage.utils.SContainer;

import java.util.LinkedHashMap;


public class StorageData {

    public static LinkedHashMap<String, SContainer> containers = new LinkedHashMap<>();


    public static void loadContainers() {
        containers = StorageSaving.loadStorageData();
    }


    public static void saveContainers() {
        StorageSaving.saveStorageData(containers.values());
    }

}