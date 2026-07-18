package com.vtx.vantix.repo;

import com.google.gson.reflect.TypeToken;
import com.vtx.vantix.repo.data.PlayerSizeData;
import com.vtx.vantix.utils.NameUtils;

import java.lang.reflect.Type;
import java.util.List;

public class PlayerSizeRepo {

    private static final Type LIST_TYPE = new TypeToken<List<PlayerSizeData>>() {
    }.getType();

    private PlayerSizeRepo() {
    }

    public static PlayerSizeData getScale(String name) {
        List<PlayerSizeData> list = RepoHandler.get(VNTXRepo.KEY_PLAYERSIZES, LIST_TYPE, null);
        if (list == null || name == null) return null;
        String normalized = NameUtils.normalize(name);
        for (PlayerSizeData d : list) {
            if (d != null && normalized.equals(NameUtils.normalize(d.name))) return d;
        }
        return null;
    }
}
