package sv.gov.cnr.factelectrcnrservice.models.dto;

import java.util.List;

public class WhatsAppMessageRequest {

    private String messaging_product;
    private String to;
    private String type;
    private Template template;

    // Getters and Setters

    public String getMessaging_product() {
        return messaging_product;
    }

    public void setMessaging_product(String messaging_product) {
        this.messaging_product = messaging_product;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    // Inner classes for nested structure

    public static class Template {
        private String name;
        private Language language;
        private List<Component> components;

        // Getters and Setters

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Language getLanguage() {
            return language;
        }

        public void setLanguage(Language language) {
            this.language = language;
        }

        public List<Component> getComponents() {
            return components;
        }

        public void setComponents(List<Component> components) {
            this.components = components;
        }
    }

    public static class Language {
        private String code;

        // Getters and Setters

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class Component {
        private String type;
        private List<Parameter> parameters;

        // Getters and Setters

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Parameter> getParameters() {
            return parameters;
        }

        public void setParameters(List<Parameter> parameters) {
            this.parameters = parameters;
        }
    }

    public static class Parameter {
        private String type;
        private String text;

        // Getters and Setters

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
