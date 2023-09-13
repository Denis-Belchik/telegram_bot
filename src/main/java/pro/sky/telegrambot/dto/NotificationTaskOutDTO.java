package pro.sky.telegrambot.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class NotificationTaskOutDTO {

    private long chatId;
    private String textNotification;

}
