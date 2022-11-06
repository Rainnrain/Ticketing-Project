package com.cydeo.mapper;



import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TaskMapper {
    private ModelMapper modelMapper;




    public TaskMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;

    }

    public TaskDTO convertToDTO(Task entity){
        return modelMapper.map(entity, TaskDTO.class);
    }

    public Task convertToEntity(TaskDTO dto){
     return modelMapper.map(dto,Task.class);
    }




}
