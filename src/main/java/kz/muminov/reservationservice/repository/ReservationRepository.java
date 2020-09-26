package kz.muminov.reservationservice.repository;

import kz.muminov.reservationservice.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query(value = "SELECT count(*) FROM reservation\n" +
            "WHERE table_id = ?1\n" +
            "AND ((start_date <= ?2 AND end_date >= ?2)\n" +
            "OR (start_date <= ?3 AND end_date >= ?3)\n" +
            "OR (start_date >= ?2 AND end_date <= ?3))", nativeQuery = true)
    int countReservationByTableAndTime(Long tableId, LocalDateTime startDate, LocalDateTime endDate);

}
