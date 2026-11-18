package com.BinarySeint.vsReportes.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.BinarySeint.vsReportes.DTO.AuditTimelineEventDTO;
import com.BinarySeint.vsReportes.DTO.CitasEventoDTO;
import com.BinarySeint.vsReportes.service.ReporteService;

@Component
public class ReporteKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(ReporteKafkaConsumer.class);
    private final ReporteService reporteService;

    public ReporteKafkaConsumer(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @KafkaListener(
            topics = "${kafka.topics.appointments-events}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumirAppointmentsEvents(
            @Payload CitasEventoDTO evento,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("📥 [Reportes - appointments.events] Partición: {} | Offset: {} | Cita ID: {}",
                partition, offset, evento.getAppointmentId());
        reporteService.procesarEventoCita(evento);
    }

    @KafkaListener(
            topics = "${kafka.topics.audit-timeline}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumirAuditTimeline(
            @Payload AuditTimelineEventDTO evento,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset) {

        log.info("📥 [Reportes - audit.timeline] Partición: {} | Offset: {} | Cita ID: {}",
                partition, offset, evento.getAppointmentId());
        reporteService.procesarEventoOperativoAudit(evento);
    }
}