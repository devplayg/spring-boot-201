package com.devplayg.coffee.framework;

import com.devplayg.coffee.entity.Asset;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
public class AssetManager {
    private Hashtable<Long, Asset> assetMap;

    public AssetManager(List<Asset> assets) {
        assetMap = new Hashtable<>();
        assets.stream().forEach(a -> {
            assetMap.put(a.getId(), a);

            Asset parent = assetMap.get(a.getParentId());
            if (parent != null) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(a);
            }
        });
        log.debug("{}", "# Initialized asset map");
    }

    public Asset get(Long id) {
        return assetMap.get(id);
    }

    public Collection<Asset> getAssetList(int type) {
        if (type < 1) {
            return this.assetMap.values();
        }

        return this.assetMap.values().stream()
                .filter(s -> s.getType() == type)
                .collect(Collectors.toList());
    }

    public Hashtable<Long, Asset> getAssetMap() {
        return this.assetMap;
    }
}
