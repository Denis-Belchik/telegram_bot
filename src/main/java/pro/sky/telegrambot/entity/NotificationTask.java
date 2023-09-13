package pro.sky.telegrambot.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@ToString
@Table(name = "notification_task")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "chat_id")
    private long chatId;
    @Column(name = "text_notification")
    private String textNotification;
    @Column(name = "datetime_notification")
    private LocalDateTime datetimeNotification;
    @Column(name = "datetime_create_notification")
    private LocalDateTime datetimeCreateNotification;

}
