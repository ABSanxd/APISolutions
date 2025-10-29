package com.api.modules.advertisement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.common.enums.Status;
import com.api.common.enums.ZoneAd;
import com.api.modules.advertisement.model.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, UUID> {

    // publicidades por zona, activas y vigentes y ordenadas por prioridad
    // (COMBINACIÓN COMPLETA)
    @Query("SELECT a FROM Advertisement a WHERE a.zone = :zone " +
            "AND a.status = :status " +
            "AND a.startDate <= :today AND a.endDate >= :today " +
            "ORDER BY a.priority ASC")
    List<Advertisement> findByZoneActiveAndValid(
            @Param("zone") ZoneAd zone,
            @Param("status") Status status,
            @Param("today") LocalDate today);




    

     // actualizar estado a EXPIRADO para publicidades cuya fecha de fin ya pasó
    // se una en un Scheduled(tarea automática) que se da diariamente, es como una revisión diaria en cierta hora
    // como la copia de seguridad pero aqui revisa y actualiza
    @Modifying
    @Query("UPDATE Advertisement a SET a.status = 'EXPIRADO', a.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE a.endDate < :today AND a.status = 'ACTIVO'")
    int expireOldAdvertisements(@Param("today") LocalDate today);





    //no implementadas.. esto sirve más para el admin            
    // publicidades activas(o no activas) ordenadas por prioridad
    //List<Advertisement> findByStatusOrderByPriorityAsc(Status status);

    // publicidades por zona específica (activas obvio) ordenadas por prioridad
    //List<Advertisement> findByZoneAndStatusOrderByPriorityAsc(ZoneAd zone, Status status);

    

   

}
