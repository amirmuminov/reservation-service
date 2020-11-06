package kz.muminov.reservationservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import kz.muminov.reservationservice.model.entity.Employee;
import kz.muminov.reservationservice.model.entity.Reservation;
import kz.muminov.reservationservice.model.entity.Table;
import kz.muminov.reservationservice.repository.ReservationRepository;
import kz.muminov.reservationservice.util.ExceptionUtils;
import kz.muminov.reservationservice.util.MessageCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableService tableService;
    private final ExceptionUtils exceptionUtils;
    private final RestTemplate restTemplate;
    
    @HystrixCommand(
            fallbackMethod = "reserveTableFallback",
            threadPoolKey = "reserveTable",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "100"),
                    @HystrixProperty(name = "maxQueueSize", value = "50")
            }
    )
    public Reservation reserveTable(Reservation reservation){

        Employee employee = restTemplate.getForObject("http://employee-service/employee/" + reservation.getEmployee().getId(), Employee.class);

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

    public Reservation reserveTableFallback(Reservation reservation, Throwable e){
        Reservation newReservation = new Reservation();
        newReservation.setId(-1L);
        log.error(e.getMessage());
        return newReservation;
    }

}
