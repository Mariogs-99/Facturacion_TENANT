package sv.gov.cnr.factelectrcnrservice.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class WhatsAppMessageResponse {

    private String messaging_product;
    private List<Contact> contacts;
    private List<Message> messages;
    @JsonProperty("error")
    private WhatsAppError error;


    public void setMessaging_product(String messaging_product) {
        this.messaging_product = messaging_product;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setError(WhatsAppError error) {
        this.error = error;
    }

    // Inner classes for nested structure

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Contact {
        private String input;
        private String wa_id;


        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getWa_id() {
            return wa_id;
        }

        public void setWa_id(String wa_id) {
            this.wa_id = wa_id;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private String id;
        private String message_status;

        // Getters and Setters

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage_status() {
            return message_status;
        }

        public void setMessage_status(String message_status) {
            this.message_status = message_status;
        }
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WhatsAppError {
        @JsonProperty("message")
        private String message;
        @JsonProperty("type")
        private String type;
        @JsonProperty("code")
        private int code;
        @JsonProperty("error_data")
        private ErrorData errorData;
        @JsonProperty("fbtrace_id")
        private String fbtraceId;

        // Getters y Setters

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public ErrorData getErrorData() {
            return errorData;
        }

        public void setErrorData(ErrorData errorData) {
            this.errorData = errorData;
        }

        public String getFbtraceId() {
            return fbtraceId;
        }

        public void setFbtraceId(String fbtraceId) {
            this.fbtraceId = fbtraceId;
        }

        // Inner class for error data details
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ErrorData {
            @JsonProperty("messaging_product")
            private String messagingProduct;
            @JsonProperty("details")
            private String details;

            // Getters y Setters

            public String getMessagingProduct() {
                return messagingProduct;
            }

            public void setMessagingProduct(String messagingProduct) {
                this.messagingProduct = messagingProduct;
            }

            public String getDetails() {
                return details;
            }

            public void setDetails(String details) {
                this.details = details;
            }
        }
    }
}

