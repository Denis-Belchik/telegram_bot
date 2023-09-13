package pro.sky.telegrambot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@ToString
public class NotificationTaskInDTO {

    private long chatId;
    private String textNotification;
    private LocalDateTime datetimeNotification;
    private LocalDateTime datetimeCreateNotification;

}
