package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.dto.NotificationTaskOutDTO;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {

    private long chatId;
    private String incomingCommand;

    private final NotificationTaskService notificationTaskService;

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            log.info("Processing update: {}", update);
            chatId = update.message().chat().id();
            incomingCommand = update.message().text();
            String message = notificationTaskService.incomingCommand(incomingCommand, chatId);
            sendMessageToUser(chatId, message);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessageToUser(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        SendResponse response = telegramBot.execute(sendMessage);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    private void sendTaskToUser(){
        List<NotificationTaskOutDTO> tasks = notificationTaskService.scheduledTask();
        tasks.forEach(t -> sendMessageToUser(t.getChatId(), t.getTextNotification()));
        log.info("Отправленные таски {}", tasks);
    }

}
