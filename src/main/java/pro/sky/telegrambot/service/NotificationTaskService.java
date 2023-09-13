package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.dto.NotificationTaskInDTO;
import pro.sky.telegrambot.dto.NotificationTaskOutDTO;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.mapper.NotificationTaskMapper;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationTaskService {

    private final String messageGood = "Напоминание добавлено";
    private final String messageError = "Я не понимаю, что Вы пишете. \n" +
                                        "Добавление напоминания происходит в формате \"ДД.ММ.ГГГГ ЧЧ:ММ Текст напоминания \"";
    private final String messageParseException = "Неверный формат даты";
    private final String messageStart = "Что я умею делать) \nВы можете добавить напоминания в формате \"ДД.ММ.ГГГГ ЧЧ:ММ Текст напоминания \"";

    private final NotificationTaskRepository taskRepository;
    private final NotificationTaskMapper taskMapper;
    private final Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");

    public String incomingCommand(String message, long chatId) {
        if (message.equals("/start")) {
            return messageStart;
        }

        Matcher matcher = pattern.matcher(message);
        if (matcher.matches()) {
            return addTask(matcher.group(1), matcher.group(3), chatId);
        }

        log.info("Не удалось распознать команду {}", message);
        return messageError;
    }

    public String addTask(String date, String textNotification, long chatId) {
        LocalDateTime datetimeNotification;

        try {
            datetimeNotification = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        } catch (DateTimeParseException e) {
            log.error("Ошибка при парсе даты {}", e.getMessage());
            return messageParseException;
        }

        LocalDateTime datetimeCreateNotification = LocalDateTime.now();
        NotificationTaskInDTO taskDTO = new NotificationTaskInDTO(chatId,
                textNotification,
                datetimeNotification,
                datetimeCreateNotification);

        taskRepository.save(taskMapper.mapToEntity(taskDTO));

        log.info("Добавлена задача для {}", taskDTO);

        return messageGood;
    }

//    @Scheduled(cron = "0 0/1 * * * *")
    public List<NotificationTaskOutDTO> scheduledTask() {
        LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> tasks = taskRepository.findByDatetimeNotification(date);
        List<NotificationTaskOutDTO> tasksDTO = tasks.stream().map(taskMapper::mapToDTO).collect(Collectors.toList());
        log.info("Найдены таски для отправки {}", tasksDTO);
        return tasksDTO;
    }

}