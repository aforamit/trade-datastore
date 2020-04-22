package com.amisoft.tds.tradedatastore.model;


import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TradeId implements Serializable {
    private String id;
    private Integer version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradeId tradeId = (TradeId) o;
        return id.equals(tradeId.id) &&
                version.equals(tradeId.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "TradeId{" +
                "id='" + id + '\'' +
                ", version=" + version +
                '}';
    }
}
