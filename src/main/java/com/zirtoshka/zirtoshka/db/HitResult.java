package com.zirtoshka.zirtoshka.db;

import com.zirtoshka.zirtoshka.beans.Coordinates;

import javax.persistence.*;

import lombok.*;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hit_result")
public class HitResult implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Exclude
    private Integer id;
    @Column(nullable = false, name = "session_id")
    private String sessionId;
    @Column(nullable = false)
    private Double x;
    @Column(nullable = false)
    private Double y;
    @Column(nullable = false)
    private Double r;
    @Column(nullable = false, name = "cr_time")
    private String currentTime;
    @Column(nullable = false)
    private boolean result;
    @Column(nullable = false)
    private boolean removed = false;

    //    public HitResult(String sessionId, Coordinates coordinates, String currentTime, boolean result){
    public HitResult(String sessionId, Coordinates coordinates, boolean result, String currentTime) {

        this.sessionId = sessionId;
        this.x = coordinates.getX();
        this.y = coordinates.getY();
        this.r = coordinates.getR();
        this.currentTime = currentTime;
        this.result = result;
    }

}
