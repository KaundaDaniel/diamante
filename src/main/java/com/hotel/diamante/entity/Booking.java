package com.hotel.diamante.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate checkeIn;
     private LocalDate checkOut;
    private int numberOfAdults;
    private int numberOfChildren;
    private int totalNumberGuest;
    private String bookingConfirmationCOde;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="room_id")
    private Room room;

    public void setTotalNumberGuest() {
        this.totalNumberGuest = numberOfAdults + numberOfChildren;
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
        setTotalNumberGuest();
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
        setTotalNumberGuest();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", checkeIn=" + checkeIn +
                ", checkOut=" + checkOut +
                ", numberOfAdults=" + numberOfAdults +
                ", numberOfChildren=" + numberOfChildren +
                ", totalNumberGuest=" + totalNumberGuest +
                ", bookingConfirmationCOde='" + bookingConfirmationCOde + '\'' +

                '}';
    }
}
