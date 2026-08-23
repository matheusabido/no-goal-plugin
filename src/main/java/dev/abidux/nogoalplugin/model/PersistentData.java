package dev.abidux.nogoalplugin.model;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersistentData<P, C> {
    private NamespacedKey key;
    private PersistentDataType<P, C> type;
    private C value;

    public void applyTo(PersistentDataContainer pdc) {
        pdc.set(this.getKey(), this.getType(), this.getValue());
    }
}