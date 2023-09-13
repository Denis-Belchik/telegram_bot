package pro.sky.telegrambot.mapper;

import org.springframework.stereotype.Component;
import pro.sky.telegrambot.dto.NotificationTaskInDTO;
import pro.sky.telegrambot.dto.NotificationTaskOutDTO;
import pro.sky.telegrambot.entity.NotificationTask;

@Component
public class NotificationTaskMapper {

    public NotificationTask mapToEntity(NotificationTaskInDTO taskDTO){
        NotificationTask notificationTask = new NotificationTask();
        notificationTask.setChatId(taskDTO.getChatId());
        notificationTask.setTextNotification(taskDTO.getTextNotification());
        notificationTask.setDatetimeNotification(taskDTO.getDatetimeNotification());
        notificationTask.setDatetimeCreateNotification(taskDTO.getDatetimeCreateNotification());
        return notificationTask;
    }

    public NotificationTaskOutDTO mapToDTO(NotificationTask task){
        NotificationTaskOutDTO taskOut = new NotificationTaskOutDTO();
        taskOut.setChatId(task.getChatId());
        taskOut.setTextNotification(task.getTextNotification());
        return taskOut;
    }

}
