package kz.muminov.reservationservice.controller;

import kz.muminov.reservationservice.model.entity.Table;
import kz.muminov.reservationservice.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    private static final String TABLE = "/table";
    private static final String GET_TABLE = TABLE + "/{id}";
    private static final String GET_TABLES = TABLE + "/list";

    @GetMapping(GET_TABLE)
    public ResponseEntity<Table> getTable(@PathVariable Long id){
        return new ResponseEntity<>(tableService.getTable(id), HttpStatus.OK);
    }

    @GetMapping(GET_TABLES)
    public ResponseEntity<List<Table>> getTables(){
        return new ResponseEntity<>(tableService.getTables(), HttpStatus.OK);
    }

    @PostMapping(TABLE)
    public ResponseEntity<Table> createTable(@RequestBody Table table){
        return new ResponseEntity<>(tableService.createTable(table), HttpStatus.CREATED);
    }

}
