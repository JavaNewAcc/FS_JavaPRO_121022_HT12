package ua.kiev.prog;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "Contacts")
@JsonInclude
public class Contact {
    @Id
    @GeneratedValue
    @JsonProperty
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonProperty
    private Group group;

    @JsonProperty
    private String name;
    @JsonProperty
    private String surname;
    @JsonProperty
    private String phone;
    @JsonProperty
    private String email;

    public Contact() {
    }

    public Contact(Group group, String name, String surname, String phone, String email) {
        this.group = group;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public String toString() {
        String groupName = null;
        if (group != null) {
            groupName = group.getName();
        }
        return "{" + "id:" + id + ",group:" + groupName + ",name:" + name + ",surname:" + surname + ",phone:" + phone + ",email:" + email + "}";
    }
}
