package com.example.test.repository;

import com.example.test.dto.request.WorkerForChangeRq;
import com.example.test.dto.request.WorkerRq;
import com.example.test.model.Worker;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkerRepository {
    private final JdbcTemplate jdbcTemplate;

    public int addWorker(WorkerRq workerRq) {
        String name = workerRq.getName();
        String avatar = workerRq.getAvatar();
        String position = workerRq.getPosition();
        return jdbcTemplate.update("insert into workers(name,position,avatar) values (?,?,?)",
                name, position, avatar);


    }

    public Worker getWorkerForNPA(WorkerRq workerRq) {
        return jdbcTemplate.query("select * from workers where name=? AND position=? AND avatar=?",
                workerRowMapper, workerRq.getName(), workerRq.getPosition(), workerRq.getAvatar()).stream().findAny().orElse(null);
    }

    private static final RowMapper<Worker> workerRowMapper = (resultSet, rowNum) -> {
        Worker worker = new Worker();
        worker.setId(resultSet.getInt("id"));
        worker.setName(resultSet.getString("name"));
        worker.setPosition(resultSet.getString("position"));
        worker.setAvatar(resultSet.getString("avatar"));
        return worker;
    };

    public Worker getWorkerById(Integer id) {
        return jdbcTemplate.query("select * from workers where id=?",
                workerRowMapper, id).stream().findAny().orElse(null);
    }

    public int removeWorkerById(Integer id) {
        return jdbcTemplate.update("delete from workers where id=?", id);
    }
    public int changeWorker(WorkerForChangeRq worker){
        return jdbcTemplate.update("update workers set name=?, position=?, avatar=? where id =?",
                worker.getName(),worker.getPosition(),worker.getAvatar(),worker.getId());
    }
}
