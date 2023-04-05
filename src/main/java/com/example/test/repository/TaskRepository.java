package com.example.test.repository;

import com.example.test.dto.response.TaskByWorkerRs;
import com.example.test.model.Status;
import com.example.test.model.Task;
import com.example.test.model.Worker;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public boolean addWorker(Worker worker, Integer id) {

        int result = jdbcTemplate.update("update tasks set performer_id=?, time=NOW() where id =?", worker.getId(), id);
        return result == 1;

    }


    public void saveTasks(Task task) {
        jdbcTemplate.update("insert into tasks(title,description,time,status,performer_id) values (?,?,?,?,?)",
                task.getTitle(),
                task.getDescription(),
                task.getTime(),
                task.getStatus().toString(),
                task.getPerformer());
    }

    public List<Task> findAll() {
        return jdbcTemplate.query("select * from tasks left join workers w on performer_id = w.id", (rs, rn) -> {
            Task task = new Task();
            task.setId(rs.getInt("id"));
            task.setDescription(rs.getString("description"));
            task.setTitle(rs.getString("title"));
            task.setTime(rs.getTime("time"));
            task.setStatus(Status.valueOf(rs.getString("status")));
            //task.setPerformer();
            return task;
        });
    }

    public List<Task> findAllTasksShort() {
        return jdbcTemplate.query("select * from tasks", (rs, rn) -> {
            com.example.test.model.Task task = new com.example.test.model.Task();
            task.setId(rs.getInt("id"));
            task.setTitle(rs.getString("title"));
            task.setStatus(Status.valueOf(rs.getString("status")));
            return task;
        });
    }

    public Task findById(int id) {
        return jdbcTemplate.queryForObject("select * from tasks as t " +
                "left join workers w on t.performer_id = w.id where t.id = ?", taskRowMapper, id);
    }

    private final RowMapper<Task> taskRowMapper = (resultSet, rowNum) -> {
        Task task = new Task();
        Worker worker = new Worker();
        worker.setAvatar(resultSet.getString("avatar"));
        worker.setName(resultSet.getString("name"));
        worker.setPosition(resultSet.getString("position"));
        worker.setId(resultSet.getInt("performer_id"));
        task.setPerformer(worker);
        task.setId(resultSet.getInt("id"));
        task.setDescription(resultSet.getString("description"));
        task.setTitle(resultSet.getString("title"));
        task.setTime(resultSet.getTime("time"));
        task.setStatus(Status.valueOf(resultSet.getString("status")));
        return task;
    };

    public boolean saveTask(Task task) {
        int result = jdbcTemplate.update("update tasks set title=?, time=NOW(), description=?, status=? where id =?",
                task.getTitle(), task.getDescription(), task.getStatus().toString(), task.getId());
        return result == 1;
    }

    public TaskByWorkerRs findTaskByWorker(Integer workerId) {
        return jdbcTemplate.query("select * from tasks join workers w on performer_id = w.id where w.id=?", (rs, rn) -> {
            Worker worker = new Worker();
            worker.setId(rs.getInt("performer_id"));
            worker.setName(rs.getString("name"));
            worker.setPosition(rs.getString("position"));
            worker.setAvatar(rs.getString("avatar"));
            TaskByWorkerRs task = new TaskByWorkerRs();
            task.setId(rs.getInt("id"));
            Status status = Status.valueOf(rs.getString("status"));
            task.setStatus(status);
            task.setTitle(rs.getString("title"));
            task.setWorker(worker);
            return task;
        },workerId).stream().findAny().orElse(null);
    }

}
