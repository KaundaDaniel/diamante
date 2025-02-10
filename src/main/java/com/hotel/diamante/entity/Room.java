package com.hotel.diamante.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Table(name = "rooms")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int roomNumber;
    private  String roomType;
    private BigDecimal price;
    private String photo;
    private String description;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomNumber=" + roomNumber +
                ", roomType='" + roomType + '\'' +
                ", price=" + price +
                ", Photo='" + photo + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
