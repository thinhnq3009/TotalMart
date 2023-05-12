package eco.mart.totalmart.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {
    final List<Notificer> notificers = new ArrayList<>();

    String json;

    private void add(MessageType type, String message) {
        notificers.add(new Notificer(type, message));
    }

    public void addSuccess(String message) {
        add(MessageType.SUCCESS, message);
    }

    public void addInfo(String message) {
        add(MessageType.INFO, message);
    }

    public void addWarning(String message) {
        add(MessageType.WARNING, message);
    }

    public void addPrimary(String message) {
        add(MessageType.PRIMARY, message);
    }

    public void addError(String message) {
        add(MessageType.ERROR, message);
    }

    public List<Notificer> getNotificers() {
        return notificers;
    }

    public void clear() {
        notificers.clear();
    }


    public String getJson() {
        // Convert list to json
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(notificers);
        } catch (JsonProcessingException e) {
            return "[]";
        }


    }

    public void render(Model model) {
        model.addAttribute("notifier", this.getJson());
        clear();
    }

    private record Notificer(MessageType type, String message) {
    }

    public enum MessageType {
        SUCCESS("success"),
        INFO("info"),
        WARNING("warning"),
        ERROR("error"),

        PRIMARY("primary");

        private final String value;

        MessageType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
