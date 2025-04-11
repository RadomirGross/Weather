package com.gross.weather.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "latitude", precision = 19, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 19, scale = 7)
    private BigDecimal longitude;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Location locations = (Location) o;
        return getId() != null && Objects.equals(getId(), locations.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).
                getHibernateLazyInitializer().getPersistentClass().
                hashCode() : getClass().hashCode();
    }

    public Location(String locationName, int userId, BigDecimal latitude, BigDecimal longitude) {
        this.name = locationName;
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}