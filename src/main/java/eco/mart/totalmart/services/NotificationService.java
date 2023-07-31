package eco.mart.totalmart.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class NotificationService {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final List<Noticer> noticers = new ArrayList<>();

    String json;

    private void add(MessageType type, String message, long duration) {
        noticers.add(new Noticer(type, message, duration));
    }

    private void add(MessageType type, String message) {
        add(type, message, 3000);
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

    public void addSuccess(String message, long duration) {
        add(MessageType.ERROR, message, duration);
    }

    public void addInfo(String message, long duration) {
        add(MessageType.INFO, message, duration);
    }

    public void addWarning(String message, long duration) {
        add(MessageType.WARNING, message, duration);
    }

    public void addPrimary(String message, long duration) {
        add(MessageType.PRIMARY, message, duration);
    }

    public void addError(String message, long duration) {
        add(MessageType.ERROR, message, duration);
    }


    public List<Noticer> getNotificers() {
        return noticers;
    }

    public void clear() {
//        logger.warn("clear noticers");
        noticers.clear();
    }


    public String getJson() {
        // Convert list to json
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(noticers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "[]";
        }


    }

    public void render(Model model) {
        model.addAttribute("noticer", this.getJson());
        clear();
    }

    private record Noticer(MessageType type, String message, long duration) {
    }

    public enum MessageType {
        SUCCESS("success"), INFO("info"), WARNING("warning"), ERROR("error"),

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
