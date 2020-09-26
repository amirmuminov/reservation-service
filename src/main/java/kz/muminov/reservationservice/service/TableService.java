package kz.muminov.reservationservice.service;

import kz.muminov.reservationservice.model.entity.Table;
import kz.muminov.reservationservice.repository.TableRepository;
import kz.muminov.reservationservice.util.ExceptionUtils;
import kz.muminov.reservationservice.util.MessageCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;
    private final ExceptionUtils exceptionUtils;

    public Table createTable(Table table){
        return tableRepository.save(table);
    }

    public Table getTable(Long id){

        if(!tableRepository.existsById(id)){
            exceptionUtils.throwDefaultException(MessageCode.TABLE_DOES_NOT_EXIST);
        }

        return tableRepository.findById(id).get();

    }

    public List<Table> getTables(){
        return tableRepository.findAll();
    }

}
