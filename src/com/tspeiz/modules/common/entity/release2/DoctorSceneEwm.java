package com.tspeiz.modules.common.entity.release2;

import javax.persistence.*;
import java.io.Serializable;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by kx on 2017/5/22.
 */
@Entity
@Table(name="doctor_scene_ewm")
public class DoctorSceneEwm implements Serializable{
    private static final long serialVersionUID = 3300464682616569936L;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    private Integer id;

    @Column(name="DoctorId")
    private Integer doctorId;

    @Column(name="TeamId")
    private Integer teamId;

    @Column(name="ErweimaUrl")
    private String erweimaUrl;

    @Column(name="RealUrl")
    private String realUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getErweimaUrl() {
        return erweimaUrl;
    }

    public void setErweimaUrl(String erweimaUrl) {
        this.erweimaUrl = erweimaUrl;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }
}
