package com.example.reflection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreatorController {
    List<TextField> field_data = new ArrayList<>();
    List<String> field_type = new ArrayList<>();
    @FXML
    private TextField class_name;

    @FXML
    private VBox content;

    @FXML
    private Button create;

    @FXML
    private TextArea log;

    @FXML
    private Button save;

    @FXML
    @SneakyThrows
    void onCreate(ActionEvent event) {

        Class<?> created_class = Class.forName(class_name.getText());
        Object inst = created_class.getConstructor().newInstance();
        Field[] class_fields = created_class.getDeclaredFields();

        for (int i = 0; i < class_fields.length; i++) {
            HBox line = new HBox();
            if (class_fields[i].getType().equals(boolean.class)) {
                CheckBox check = new CheckBox();
                String getter_name = "get" + StringUtils.capitalize(class_fields[i].getName());
                Method getter = created_class.getDeclaredMethod(getter_name);
                boolean def_value = (boolean) getter.invoke(inst);

                if (def_value) {
                    check.setSelected(true);
                } else {
                    check.setSelected(false);
                }

                line.getChildren().add(check);

                Label name_label = new Label();
                name_label.setText(class_fields[i].getName());

                line.getChildren().add(name_label);
                content.getChildren().add(line);
            } else if (class_fields[i].getName().contains("text")) {
                String getter_name = "get" + StringUtils.capitalize(class_fields[i].getName());
                Method getter = created_class.getDeclaredMethod(getter_name);
                String def_value = getter.invoke(inst).toString();

                TextArea input_text_area = new TextArea();
                input_text_area.setText(def_value);

                Label name_label = new Label();
                name_label.setText(class_fields[i].getName());

                input_text_area.setEditable(true);
                line.getChildren().add(input_text_area);
                line.getChildren().add(name_label);
                content.getChildren().addAll(line);
            } else {
                String getter_name = "get" + StringUtils.capitalize(class_fields[i].getName());
                Method getter = created_class.getDeclaredMethod(getter_name);
                String def_value = getter.invoke(inst).toString();

                TextField input_text = new TextField();
                input_text.setText(def_value);

                Label name_label = new Label();
                name_label.setText(class_fields[i].getName());

                input_text.setEditable(true);
                line.getChildren().add(input_text);
                line.getChildren().add(name_label);
                content.getChildren().addAll(line);
            }
        }
    }

    @FXML
    @SneakyThrows
    void onSave(ActionEvent event) {
        Class<?> choosen_class = Class.forName(class_name.getText());
        Object inst = choosen_class.getConstructor().newInstance();

        for (HBox line : content.getChildren().stream().map(el -> (HBox) el).collect(Collectors.toList())) {
            Label label_name = (Label) line.getChildren().get(1);
            Field class_field = choosen_class.getDeclaredField(label_name.getText());
            String setter_name = "set" + StringUtils.capitalize(class_field.getName());
            Method setter = choosen_class.getMethod(setter_name, class_field.getType());

            Class<?> field_type = ClassUtils.primitiveToWrapper(class_field.getType());
            System.out.println(field_type);
            Object setter_arg = null;

            if (field_type.equals(Boolean.class)) {
                CheckBox box = (CheckBox) line.getChildren().get(0);
                setter_arg = box.isSelected();
                String log_add = class_field.getName() + "=" + String.valueOf(box.isSelected()) + "\n";
                setter.invoke(inst, setter_arg);
                log.appendText(log_add);

            } else if (label_name.getText().contains("text")) {
                TextArea t_area = (TextArea) line.getChildren().get(0);
                String area_text = t_area.getText();
                setter_arg = area_text;
                String log_add = class_field.getName() + "=" + area_text + "\n";
                setter.invoke(inst, setter_arg);
                log.appendText(log_add);

            } else if (field_type.equals(String.class)) {
                TextField t_field = (TextField) line.getChildren().get(0);
                String field_text = t_field.getText();
                setter_arg = field_text;
                String log_add = class_field.getName() + "=" + field_text + "\n";
                setter.invoke(inst, setter_arg);
                log.appendText(log_add);
            } else {
                try {
                    TextField t_field = (TextField) line.getChildren().get(0);
                    Method value = field_type.getMethod("valueOf", String.class);
                    setter_arg = value.invoke(null, t_field.getText());
                    String log_add = class_field.getName() + "=" + t_field.getText() + "\n";
                    setter.invoke(inst, setter_arg);
                    log.appendText(log_add);
                } catch (Exception e) {
                    String error_info = "The property named '" + class_field.getName() + "' can't be changed! \n";
                    log.appendText(error_info);
                }
            }

        }
        log.appendText(inst.toString());

    }

}
