package kz.muminov.reservationservice.service;

import kz.muminov.reservationservice.model.entity.Employee;
import kz.muminov.reservationservice.model.entity.Reservation;
import kz.muminov.reservationservice.model.entity.Table;
import kz.muminov.reservationservice.repository.ReservationRepository;
import kz.muminov.reservationservice.util.ExceptionUtils;
import kz.muminov.reservationservice.util.MessageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableService tableService;
    private final ExceptionUtils exceptionUtils;
    private final RestTemplate restTemplate;

    public Reservation reserveTable(Reservation reservation){

        Employee employee = restTemplate.getForObject("http://localhost:8082/employee/" + reservation.getEmployee().getId(), Employee.class);

        Table table = tableService.getTable(reservation.getTable().getId());

        if(reservation.getEndDate() == null){
            reservation.setEndDate(reservation.getStartDate().plusHours(4));
        }

        int count = reservationRepository.countReservationByTableAndTime(table.getId(),
                                                                         reservation.getStartDate(),
                                                                         reservation.getEndDate());

        if(count > 0){
            exceptionUtils.throwDefaultException(MessageCode.THIS_RESERVATION_TIME_IS_NOT_FREE);
        }

        if(reservation.getStartDate().isAfter(reservation.getEndDate())){
            exceptionUtils.throwDefaultException(MessageCode.START_DATE_IS_BIGGER_THAN_END_DATE);
        }



        reservation.setEmployee(employee);
        reservation.setTable(table);

        return reservationRepository.save(reservation);

    }

}
