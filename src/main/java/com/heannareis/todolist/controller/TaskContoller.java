package com.heannareis.todolist.controller;
import com.heannareis.todolist.domain.model.TaskModel;
import com.heannareis.todolist.repository.ITaskRepository;
import com.heannareis.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskContoller {
    @Autowired
    private ITaskRepository taskRepository;
    @PostMapping("/")
    public ResponseEntity created(@RequestBody TaskModel taskModel, HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        var  currentData = LocalDateTime.now();

        if(currentData.isAfter(taskModel.getStartAt()) || currentData.isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Data Inicial e Final, devem ser maiores do que data atual!");
        }
        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Data Inicial deve ser menor do que a Data Final!");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }
    @GetMapping("/")
    public List<TaskModel> list (HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }
    @PutMapping("/{id}")
    public ResponseEntity update (@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request){
        var task = this.taskRepository.findById(id).orElse(null);

        if (task == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Não encontramos sua Tarefa!! Inclua uma nova tarefa e verifique novamente S2.");
        }

        var idUser = request.getAttribute("idUser");

        if (!task.getIdUser().equals(idUser)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Sem permissão para alterar essa tarefa S2");
        }

        Utils.copyNonNullProperties(taskModel, task);
        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }
}
